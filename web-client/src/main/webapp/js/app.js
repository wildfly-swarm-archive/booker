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
        <Booker.Footer/>
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
      <div id="welcome">
        <h1>Welcome to Booker</h1>
        <p>
        Booker is an electronic bookstore powered by WildFly Swarm.
        </p>
        <img src="/images/logo.png" width="400px"/>
      </div>
    );
  }
});

Booker.About = React.createClass({
  render: function() {
    return (
      <div>
      <h1>About Booker!</h1>

      <p>
      <b>Booker!</b> is an electronic bookstore that demonstrates
      how many WildFly Swarm-based microservices can play together.
      </p>

      <p>
      The services are wired together using WildFly node discovery and
      invoked using NetflixOSS Ribbon.
      </p>

      <p>
      You can <Link to="topology">view the topology</Link> to understand
      which services are up (and where) at any point in time.
      </p>

      <p>
      <img src="/images/diagram.png"/>
      </p>

      </div>

    );
  }
});

Booker.Footer = React.createClass({
  render: function() {
    return (
      <div id="footer" className="row">
        <div className="large-12 columns">
          Powered by <img src="/images/logo-small.png"/> WildFly Swarm | <Link to="topology">View topology</Link>
        </div>
      </div>
    )
  }
})


Booker.NotFound = React.createClass({
  render: function() {
    console.log( "render NotFound" );
    return (
      <div>Not Found</div>
    );
  }
});
