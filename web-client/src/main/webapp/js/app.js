var Router        = ReactRouter;
var RouteHandler  = Router.RouteHandler;

Booker = {};
Booker.State = {};
Booker.Actions = {};

var keycloak= new Keycloak('http://localhost:8080/keycloak.json');
//keycloak.init();

Booker.App = React.createClass({
  render: function() {
    return (
      <div>
        <div>App</div>
        <Booker.Header/>
        <RouteHandler/>
      </div>
    );
  }
});

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

Booker.Books = React.createClass({
  render: function() {
    return (
      <div>Your Books</div>
    );
  }
});

Booker.Book = React.createClass({
  render: function() {
    var id = this.props.params.id;
    return (
      <div>Book {id}</div>
    );
  }
});

Booker.Account = React.createClass({
  render: function() {
    return (
      <div>Your Account</div>
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
