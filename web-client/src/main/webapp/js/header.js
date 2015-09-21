
Booker.Actions.UserLoggedIn = Reflux.createAction();

Booker.State.UserInfo = Reflux.createStore({
  init: function() {
    this.listenTo( Booker.Actions.UserLoggedIn, this.output );
  },

  output: function(info) {
    this.trigger(info);
  }

});

Booker.Header = React.createClass({
  render: function() {
    return (
      <div id="header" className="row">
        <div className="large-3 columns">
          <span id="booker-logo"><Link to="home">Booker!</Link></span>
        </div>
        <div className="large-6 columns">
          <Booker.MenuHeader/>
        </div>
        <div className="large-3 columns">
          <Booker.AuthHeader/>
        </div>
      </div>
    );
  }
});

Booker.MenuHeader = React.createClass({
  render: function() {
    return (
      <ul className="inline-list">
        <li><Link to="about">About</Link></li>
        <li><Link to="account">Account</Link></li>
        <li><Link to="library">Your Library</Link></li>
        <li><Link to="store">The Store</Link></li>
      </ul>
    );
  }
});

Booker.AuthHeader = React.createClass({
  render: function() {
    if ( keycloak.authenticated ) {
      return (
        <Booker.Authenticated/>
      )
    } else {
      return (
        <Booker.Unauthenticated/>
      )
    }
  }
});

Booker.Unauthenticated = React.createClass({
  onClick: function() {
    keycloak.login();
    return false;
  },
  render: function() {
    return (
      <a href="" onClick={this.onClick}>Login</a>
    )
  }
});

Booker.Authenticated = React.createClass({
  render: function() {
    return (
      <ul className="inline-list">
        <li><Booker.UserInfo/></li>
        <li><a href={keycloak.createLogoutUrl({redirectUri: 'http://localhost:8080/'})}>Logout</a></li>
      </ul>
    )
  }
});

Booker.UserInfo = React.createClass({
  mixins: [Reflux.connect(Booker.State.UserInfo,"info")],
  getInitialState: function() {
    return {
      info: {
        name: 'Booker User',
      }
    };
  },

  render: function()  {
    return (
      <span>
        <span>{this.state.info.name}</span>
      </span>
    )
  }

})
