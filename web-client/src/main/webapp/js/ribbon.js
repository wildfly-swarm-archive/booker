
Booker.Actions.Topology = Reflux.createAction();

Booker.State.Topology = Reflux.createStore({
  init: function() {
    this.listenTo( Booker.Actions.Topology, this.output );
    this.sse = new EventSource( "/system/ribbon/stream" );
    this.sse.onmessage = Booker.Actions.Topology;
    this.topology = {};
  },

  output: function(topology) {
    this.topology = JSON.parse( topology.data );
    this.trigger(this.topology);
  },

  servers: function(serviceName) {
    return this.topology[serviceName] || [];
  }
});


Booker.Topology = React.createClass({
  mixins: [Reflux.connect(Booker.State.Topology,"topology")],

  render: function() {
    var services = [ 'books', 'store' ];
    return (
      <div>
        <h1>Topology</h1>
        {
          services.map( function(e) {
            return (
              <Booker.TopologyService name={e} key={e} servers={Booker.State.Topology.servers(e)}/>
            )
          })
        }
      </div>
    );
  }
});

Booker.TopologyService = React.createClass({
  render: function() {
    return (
      <div>
        <h3>{this.props.name}</h3>
        <ul>
        {
          this.props.servers.map(function(e){
            return (
              <li key={e}><a href={"//" + e}>{e}</a></li>
            )
          })
        }
        </ul>
      </div>

    )
  }
});


