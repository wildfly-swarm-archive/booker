

Booker.Books = React.createClass({
  render: function() {
    return (
      <div>Your Books</div>
    );
  }
});

Booker.Book = React.createClass({
  render: function() {
    var id = this.props.params.id;
    return (
      <div>Book {id}</div>
    );
  }
});
