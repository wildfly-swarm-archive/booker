var Router        = ReactRouter;
var RouteHandler  = Router.RouteHandler;
var DefaultRoute  = Router.DefaultRoute;
var Link          = Router.Link;
var Route         = Router.Route;
var NotFoundRoute = Router.Route;

(function() {
  var routes = (
    <Route handler={Booker.App} path="/">
      <DefaultRoute handler={Booker.Home} />
      <Route name="about" handler={Booker.About} />
      <Route name="books">
        <Route name="book" path=":id" handler={Booker.Book}/>
        <DefaultRoute handler={Booker.Books}/>
      </Route>
      <Route name="account" handler={Booker.Account} />
    </Route>
  );


  keycloak.init({ onLoad: 'check-sso' }).success( function() {
    if ( keycloak.authenticated ) {
      keycloak.loadUserInfo().success( function(info) {
        Booker.Actions.UserLoggedIn( info );
      });
    }
    Router.run(routes, Router.HistoryLocation, function (Handler) {
      React.render(<Handler/>, document.getElementById('app'));
    });
  })
})();
