


Booker.Actions.Library = Reflux.createActions({
    "load": {children: ["completed","failed"]}
});

Booker.Actions.Library.load.listen( function(term) {
  Ribbon.ajax( "library", "/items" )
    .then( function(data) {
      Booker.Actions.Library.load.completed(data);
    })
    .fail( function(err) {
      console.log( "search failed", err );
    })
})

Booker.State.Library = Reflux.createStore({
  init: function() {
    this.listenTo( Booker.Actions.Library.load.completed, this.output );
  },

  output: function(results) {
    console.log( results );
    this.trigger(results);
  }
});

Booker.Library = React.createClass({
  render: function() {
    Booker.Actions.Library.load()
    return (
      <div>Your Library</div>
    );
  }
});

Booker.Library.Item = React.createClass({
  render: function() {
    var id = this.props.params.id;
    return (
      <div>Book {id}</div>
    );
  }
});
