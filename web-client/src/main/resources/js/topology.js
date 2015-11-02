Booker.Actions.Topology = Reflux.createAction();

Booker.State.Topology = Reflux.createStore({
  init: function() {
    this.listenTo( Booker.Actions.Topology, this.output );
    this.sse = new EventSource( "/ribbon/system/stream" );
    this.sse.onmessage = Booker.Actions.Topology;
    this.sse.onerror = function(e) {
      console.log( "SSE error", e );
    }
    this.sse.onopen = function() {
      console.log( "SEE opened" );
    }
    this.topology = {};
  },

  output: function(topology) {
    console.log( "update topology", topology );
    this.topology = JSON.parse( topology.data );
    this.trigger(this.topology);
  },

  services: function() {
    return Object.getOwnPropertyNames( this.topology ).sort( function(l,r) {
      return l.localeCompare(r);
    });
  },

  servers: function(serviceName) {
    return this.topology[serviceName] || [];
  }

});

Booker.Topology = React.createClass({
  mixins: [Reflux.connect(Booker.State.Topology,"topology")],

  render: function() {
    var services = Booker.State.Topology.services();
    return (
      <div>
        <h1>Topology</h1>
        <div className="explanation">
          <p>The <b>topology</b> is the layout of the services,
          and how they connect together. WildFly node discovery
          is used to link up the NetflixOSS Ribbon components.
          </p>
          <p>
          This topology is then made available to the web-app,
          and kept up-to-date using Server-Sent Events (SSE)
          to the browser.
          </p>
        </div>
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
              <li key={e}>{e}</li>
            )
          })
        }
        </ul>
      </div>

    )
  }
});