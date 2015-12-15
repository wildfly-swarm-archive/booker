package org.wildfly.swarm.booker.common;

import com.openshift.restclient.ClientFactory;
import com.openshift.restclient.IClient;
import com.openshift.restclient.NoopSSLCertificateCallback;
import com.openshift.restclient.ResourceKind;
import com.openshift.restclient.authorization.TokenAuthorizationStrategy;
import com.openshift.restclient.model.IProject;
import com.openshift.restclient.model.IService;
import com.openshift.restclient.model.route.IRoute;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Discoverer {

    private static final boolean IN_OPENSHIFT = System.getenv("KUBERNETES_SERVICE_HOST") != null;

    public static String serviceHost(String serviceName, String defaultHost) {
        String host = serviceHostFromEnv(serviceName);

        if (host == null) {
            try {
                host = serviceHostFromOpenshift(serviceName);
            } catch (IOException ex) {
                System.err.println("Error discovering service host via OpenShift:");
                ex.printStackTrace();
            }
        }

        if (host == null) {
            host = defaultHost;
        }

        return host;
    }

    public static int servicePort(String serviceName, int defaultPort) {
        int port = servicePortFromEnv(serviceName);

        if (port < 0) {
            try {
                port = servicePortFromOpenshift(serviceName);
            } catch (IOException ex) {
                System.err.println("Error discovering service port via OpenShift:");
                ex.printStackTrace();
            }
        }

        if (port < 0) {
            port = defaultPort;
        }

        return port;
    }

    public static String serviceHostFromEnv(String serviceName) {
        String envName = serviceName.replace("-", "_").toUpperCase() + "_SERVICE_HOST";
        return System.getenv(envName);
    }

    public static int servicePortFromEnv(String serviceName) {
        String envName = serviceName.replace("-", "_").toUpperCase() + "_SERVICE_PORT";
        String envPort = System.getenv(envName);
        if (envPort == null) {
            return -1;
        }

        return Integer.parseInt(envPort);
    }

    public static String serviceHostFromOpenshift(String serviceName) throws IOException {
        if (!IN_OPENSHIFT) {
            return null;
        }
        IClient client = openshiftClient();
        List<IProject> projects = client.list(ResourceKind.PROJECT);
        for (IProject project : projects) {
            List<IService> services = client.list(ResourceKind.SERVICE, project.getName());
            for (IService service : services) {
                if (service.getName().equals(serviceName)) {
                    return service.getPortalIP();
                }
            }
        }
        return null;
    }

    public static int servicePortFromOpenshift(String serviceName) throws IOException {
        if (!IN_OPENSHIFT) {
            return -1;
        }
        IClient client = openshiftClient();
        List<IProject> projects = client.list(ResourceKind.PROJECT);
        for (IProject project : projects) {
            List<IService> services = client.list(ResourceKind.SERVICE, project.getName());
            for (IService service : services) {
                if (service.getName().equals(serviceName)) {
                    return service.getPort();
                }
            }
        }
        return -1;
    }

    public static String serviceHostToExternalHost(String serviceHost) throws IOException {
        if (!IN_OPENSHIFT) {
            return serviceHost;
        }
        IClient client = openshiftClient();
        List<IProject> projects = client.list(ResourceKind.PROJECT);
        for (IProject project : projects) {
            // We translate serviceHost to service to make sure we're getting
            // the route from the correct project
            List<IService> services = client.list(ResourceKind.SERVICE, project.getName());
            IService matchingService = null;
            for (IService service : services) {
                if (service.getPortalIP().equals(serviceHost)) {
                    matchingService = service;
                    break;
                }
            }
            if (matchingService == null) {
                continue;
            }
            List<IRoute> routes = client.list(ResourceKind.ROUTE, project.getName());
            for (IRoute route : routes) {
                if (route.getServiceName().equals(matchingService.getName())) {
                    return route.getHost();
                }
            }
        }
        return serviceHost;

    }

    public static String externalKeycloakUrl(int externalHttpPort) throws IOException {
        String keycloakHost = serviceHost("booker-keycloak", "localhost");
        int keycloakPort = Discoverer.servicePort("booker-keycloak", 9090);

        String externalKeycloakHost = serviceHostToExternalHost(keycloakHost);
        if (!externalKeycloakHost.equals(keycloakHost)) {
            keycloakHost = externalKeycloakHost;
            keycloakPort = externalHttpPort;
        }

        String url = "http://" + keycloakHost;
        if (keycloakPort != 80) {
            url += ":" + keycloakPort;
        }
        return url;
    }

    protected static IClient openshiftClient() throws IOException {
        String kubeHost = serviceHostFromEnv("kubernetes");
        int kubePort = servicePortFromEnv("kubernetes");

        String tokenFile = "/var/run/secrets/kubernetes.io/serviceaccount/token";
        String token = new String(Files.readAllBytes(Paths.get(tokenFile)));

        IClient client = new ClientFactory().create("https://" + kubeHost + ":" + kubePort, new NoopSSLCertificateCallback());
        client.setAuthorizationStrategy(new TokenAuthorizationStrategy(token));
        return client;
    }
}
