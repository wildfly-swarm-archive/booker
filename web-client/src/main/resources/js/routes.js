var Router        = ReactRouter;
var RouteHandler  = Router.RouteHandler;
var DefaultRoute  = Router.DefaultRoute;
var Link          = Router.Link;
var Route         = Router.Route;
var NotFoundRoute = Router.Route;

(function() {
  var routes = (
    <Route handler={Booker.App} path="/">
      <DefaultRoute name="home" handler={Booker.Home} />
      <Route name="topology" path="topology" handler={Booker.Topology}/>
      <Route name="about" handler={Booker.About} />
      <Route name="library" handler={Booker.CheckAuth}>
        <Route name="library-item" path=":id" handler={Booker.Library.Item}/>
        <DefaultRoute handler={Booker.Library}/>
      </Route>
      <Route name="store">
        <Route name="store-item" path=":id" handler={Booker.StoreItem}/>
        <DefaultRoute handler={Booker.Store}/>
      </Route>
      <Route path="account" handler={Booker.CheckAuth}>
        <DefaultRoute name="account" handler={Booker.Account} />
      </Route>
    </Route>
  );


  if (keycloak) {
    keycloak.init({ onLoad: 'check-sso' }).success( function() {
      if ( keycloak.authenticated ) {
        keycloak.loadUserInfo().success( function(info) {
          Booker.Actions.UserLoggedIn( info );
        });
      }
      Router.run(routes, Router.HistoryLocation, function (Handler) {
        React.render(<Handler/>, document.getElementById('app'));
      });
    });
  }
})();
