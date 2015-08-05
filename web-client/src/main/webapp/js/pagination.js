

Booker.Pagination = React.createClass({
  render: function() {
    if ( this.props.pagination.numberOfPages == 0 ) {
      return (<div></div>);
    }
    return (
      <div>
        <div>{this.props.pagination.page}</div>
      </div>
    )
  }
})