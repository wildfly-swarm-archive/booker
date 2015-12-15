<%@page contentType="text/html"
  import="org.wildfly.swarm.booker.common.*"%>

<%
  String keycloakUrl = Discoverer.externalKeycloakUrl(request.getServerPort());
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
    <script src="<%= keycloakUrl %>/auth/js/keycloak.js"></script>
    <script type="text/jsx" src="/js/app.js"></script>

    <script src="/ribbon/ribbon.js"></script>
    <script language="javascript">
      if (typeof Keycloak === 'function') {
        keycloak = new Keycloak('/keycloak.json');
        var Ribbon = ribbon({keycloak: keycloak});
      } else {
        alert('No keycloak is running. Cannot continue.');
      }


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
