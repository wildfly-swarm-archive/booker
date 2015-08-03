
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
      <div id="header">
        <span>Booker!</span>
        <Booker.AuthHeader/>
      </div>
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
  onClick: function() {
    keycloak.logout();
    return false;
  },
  render: function() {
    return (
      <span>
        <Booker.UserInfo/>
        <a id="" href={keycloak.createLogoutUrl()}>Logout</a>
      </span>
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
      <span>{this.state.info.name}</span>
    )
  }

})
