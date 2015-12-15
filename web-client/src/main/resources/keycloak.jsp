<%@ page contentType="application/json"
    import="org.wildfly.swarm.booker.common.*" %>
<%
  String keycloakUrl = Discoverer.externalKeycloakUrl(request.getServerPort());
%>
{
  "realm": "booker",
  "realm-public-key": "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAm/yXPhm3xlNsYOLQGM4YxqjAe5mjBxKcxJCYyQGzz36DQFO59IclAxaFEsgN3OorVL2W9wLLGUveoV47s8rOdq4+obHiO2C/bgDSVcvg+X8RRAngZDR04iPPdD+cjMdxAYb/WsGjlOKju+U8Pk2o/TnRHNmZgbwE9JDnhGrmFycCgu7oQGk6KDVpsp3zVIsr2qrah0ujBwUbPti8NN4OZBupMzgR3oOjzJ9dhkh9qaQN6SErnRj3lENAh7rbizKEWnIxImgi6m6ogLNWMxNKJlrRbER2LCSegDXhhO3zuBhHb0xZ1P+dO+4LL478SCQStutrGSoO0Yc0GiEVFBIncQIDAQAB",
  "auth-server-url": "<%= keycloakUrl %>/auth",
  "ssl-required": "external",
  "resource": "web-client",
  "credentials": {
    "secret": "deec63e4-d242-4180-b402-80fba0a9187e"
  }
}
