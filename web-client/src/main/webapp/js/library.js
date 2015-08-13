


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
    this.trigger(results);
  }
});

Booker.Library = React.createClass({
  mixins: [Reflux.connect(Booker.State.Library,"items")],

  componentWillMount: function() {
    Booker.Actions.Library.load()
  },

  getInitialState: function() {
    return {
      items: [],
    }
  },

  render: function() {
    return (
      <div>
        <ul>
        {
          this.state.items.map( function(e) {
            return (
              <Booker.Library.Item key={e.bookId} item={e}/>
            );
          })
        }
        </ul>
      </div>
    )
  }
});

Booker.Library.Item = React.createClass({
  render: function() {
    return (
      <ul>Book {this.props.item.bookId}</ul>
    );
  }
});
