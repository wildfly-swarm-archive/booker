<%@page contentType="text/html"%>

<%
  String keycloakHost = System.getenv("KEYCLOAK_SERVICE_HOST");
  if (keycloakHost == null) {
    keycloakHost = "localhost";
  }

  String keycloakPort = System.getenv("KEYCLOAK_SERVICE_PORT");
  if (keycloakPort == null) {
    keycloakPort = "9090";
  }
%>

<html>
  <head>
    <title>Booker</title>
    <link rel="stylesheet" href="/webjars/foundation/5.5.2/css/foundation.css"/>
    <link rel="stylesheet" href="/css/app.css"/>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.0.0-alpha1/jquery.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/react/0.13.3/JSXTransformer.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/react/0.13.3/react-with-addons.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/react-router/0.13.3/ReactRouter.js"></script>
    <script src="//cdn.jsdelivr.net/refluxjs/0.2.11/reflux.min.js"></script>
    <script src="http://<%= keycloakHost %>:<%= keycloakPort %>/auth/js/keycloak.js"></script>
    <script type="text/jsx" src="/js/app.js"></script>

    <script src="/ribbon/ribbon.js"></script>
    <script language="javascript">
      if (typeof Keycloak === 'function') {
        keycloak = new Keycloak('/keycloak.json');
      }

      var Ribbon = ribbon({keycloak: keycloak});
    </script>

    <script type="text/jsx" src="/js/topology.js"></script>
    <script type="text/jsx" src="/js/pagination.js"></script>
    <script type="text/jsx" src="/js/header.js"></script>
    <script type="text/jsx" src="/js/library.js"></script>
    <script type="text/jsx" src="/js/store.js"></script>
    <script type="text/jsx" src="/js/account.js"></script>
    <script type="text/jsx" src="/js/routes.js"></script>
  </head>

  <body>
    <div id="app"></div>
  </body>
</html>
