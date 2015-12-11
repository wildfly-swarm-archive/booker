Booker.Account = React.createClass({
  mixins: [Reflux.connect(Booker.State.UserInfo,"user")],
  getInitialState: function() {
    return {
      user: {
        name: 'Booker User',
      }
    };
  },

  render: function() {
    return (
      <div className='account-panel'>
        <h2> Your Account</h2>
        <div className='user-info'>
          <ul>
        <li>Name: {this.state.user.name}</li>
        <li>Username: {this.state.user.preferred_username}</li>
          </ul>
          </div>
      </div>
    );
  }
});
