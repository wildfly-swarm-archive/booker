var Router        = ReactRouter;
var RouteHandler  = Router.RouteHandler;

Booker = {};
Booker.State = {};
Booker.Actions = {};

var keycloak= new Keycloak('http://localhost:8080/keycloak.json');

Booker.App = React.createClass({

  render: function() {
    return (
      <div>
        <Booker.Header/>
        <div className="row">
          <RouteHandler/>
        </div>
      </div>
    );
  }
});

Booker.CheckAuth = React.createClass({
  render: function() {
    if ( ! keycloak.authenticated ) {
      keycloak.login();
    }
    return (
      <RouteHandler/>
    );
  }
})

Booker.Home = React.createClass({
  render: function() {
    return (
      <div>Welcome to Booker</div>
    );
  }
});

Booker.About = React.createClass({
  render: function() {
    return (
      <div>About Booker</div>
    );
  }
});


Booker.NotFound = React.createClass({
  render: function() {
    console.log( "render NotFound" );
    return (
      <div>Not Found</div>
    );
  }
});
