
Booker.Actions.SearchStore = Reflux.createActions({
    "search": {children: ["completed","failed"]}
});

Booker.Actions.SearchStore.search.listen( function(term) {
  console.log( "searching for [" + term + "]" );
  this.completed([]);
})

Booker.State.StoreSearchResults = Reflux.createStore({
  init: function() {
    this.listenTo( Booker.Actions.SearchStore.search.completed, this.output );
  },

  output: function(results) {
    console.log( "search results", results );
    this.trigger(results);
  }
});

Booker.Store = React.createClass({
  onSubmit: function(event) {
    event.preventDefault();
    var searchTerm = $('#store-search').val();
    Booker.Actions.SearchStore.search(searchTerm);
  },

  render: function() {
    return (
      <div className="large-12 column">
        <h1>Search</h1>
        <form onSubmit={this.onSubmit}>
          <input id="store-search" type="text"/>
        </form>
        <Booker.SearchResults/>
      </div>
    );
  }
});

Booker.SearchResults = React.createClass({
  mixins: [Reflux.connect(Booker.State.StoreSearchResults,"results")],

  getInitialState: function() {
    return {
      results: [],
    };
  },

  render: function() {
    console.log( "render search results", this.state.results );
    return (
      <ul>
      {
        this.state.results.map( function(e) {
          return (
            <li>{e}</li>
          );
        })
      }
      </ul>
    )
  }
})

Booker.StoreItem = React.createClass({
  render: function() {
    return (
      <div className="large-12 column">
        Store Item {this.props.params.id}
      </div>
    );
  }
})


