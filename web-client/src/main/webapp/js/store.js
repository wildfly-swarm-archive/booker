
Booker.Actions.SearchStore = Reflux.createActions({
    "search": {children: ["completed","failed"]}
});

Booker.Actions.SearchStore.search.listen( function(term) {

  Ribbon.ajax( "store", "/search", { q: term } )
    .then( function(data) {
      Booker.Actions.SearchStore.search.completed(data);
    })
    .fail( function() {
      console.log( "search failed" );
    })
})

Booker.State.StoreSearchResults = Reflux.createStore({
  init: function() {
    this.listenTo( Booker.Actions.SearchStore.search.completed, this.output );
  },

  output: function(results) {
    console.log( results );
    this.trigger(results);
  }
});

Booker.Store = React.createClass({
  mixins: [Router.Navigation, Router.State],

  getInitialState: function() {
    return { q: '', page: 1 };
  },

  onSubmit: function(event) {
    event.preventDefault();
    var searchTerm = $('#store-search').val();
    this.transitionTo('store', {}, { q: searchTerm } );
    Booker.Actions.SearchStore.search(searchTerm);
  },

  handleChange: function(event) {
      this.setState({q: event.target.value});
  },

  initSearch: function() {
    if ( this.getQuery().q != this.state.q ) {
      this.setState({q: this.getQuery().q } );
      Booker.Actions.SearchStore.search(this.getQuery().q);
    }
  },

  componentWillReceiveProps: function() {
    this.initSearch();
  },

  componentWillMount: function() {
    this.initSearch();
  },

  render: function() {

    return (
      <div className="large-12 column">
        <h1>Search</h1>
        <form onSubmit={this.onSubmit}>
          <input id="store-search" type="text" value={this.state.q} onChange={this.handleChange}/>
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
      results: { page: 0, numberOfPages: 0, results: [] },
    };
  },

  render: function() {
    return (
      <div>
        <ul>
        {
          this.state.results.results.map( function(e) {
            return (
              <li key={e.id}>{e.title}</li>
            );
          })
        }
        </ul>
        <Booker.Pagination pagination={this.state.results}/>
      </div>
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


