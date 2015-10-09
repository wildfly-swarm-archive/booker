var Router        = ReactRouter,
    RouteHandler  = Router.RouteHandler,
    keycloak;

Booker = {};
Booker.State = {};
Booker.Actions = {};

if (typeof Keycloak === 'function')
  keycloak= new Keycloak('keycloak.json');

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

        <p>
        For more information see <Link to="about">the About page</Link>
        </p>
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

      <div className="row">
      <div className="large-12 columns">
      <h3>Keycloak</h3>
      <p>
      <b>Keycloak</b> is an independent authentication and authorization server that
      runs apart from any app or microservice.  It provides integrations to social-login
      and other enterprise backing-stores, such as LDAP and Kerberos. Some of the <b>Booker!</b>
      services require authenticated users to operate, some just behave differently if the
      user is authenticated or anonymous.
      </p>
      </div>
      </div>

      <div className="row">
      <div className="large-6 columns">

      <h3>Web Client</h3>
      <p>
      The <b>web client</b> is implemented as a single-page app, with most
      of the logic occurring within the browser. The client is packaged as
      a <b>.war</b> file, and then wrapped with WildFly Swarm.  The only
      server-side component is the Server-Sent Events async servlet which
      provides the topology of the microservices to the single-page app running in the browser.
      </p>

      <h3>Library</h3>
      <p>
      The <b>library</b> service is used to track which titles have been purchased
      by a user. This service uses JPA and an SQL datasource to track combinations
      of <b>user-id</b> and <b>book-id</b>.  Since simple identifiers are not very useful,
      it subsequently invokes the <b>store</b> service to fill in details about your
      library, including the title and author of any book in your library.
      </p>
      </div>

      <div className="large-6 columns">
      <h3>Store</h3>
      <p>
      The <b>store</b> service provides the ability to search and query the
      inventory of books, which is kept in-memory (using data from Project Gutenberg).
      </p>

      <h3>Pricing</h3>
      <p>
      The <b>pricing</b> service is used by the store to provide the price of each
      book.  It uses very simple logic but mostly demonstrates how authentication
      information can be propagated from the browser, across another service, and
      on to an even further-away service.  If authentication information is provided,
      the pricing service prices a book at <b>$9</b>, otherwise, it prices it at <b>$10</b>.
      </p>
      </div>

      </div>


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
