package org.wildfly.swarm.booker.store;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.enterprise.context.ApplicationScoped;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import static javax.xml.stream.XMLStreamConstants.END_DOCUMENT;
import static javax.xml.stream.XMLStreamConstants.END_ELEMENT;
import static javax.xml.stream.XMLStreamConstants.START_DOCUMENT;
import static javax.xml.stream.XMLStreamConstants.START_ELEMENT;

/**
 * @author Bob McWhirter
 */
@ApplicationScoped
public class Store {

    public static final QName BOOKS = new QName("books");
    public static final QName BOOK = new QName("book");
    public static final QName TITLE = new QName("title");
    public static final QName AUTHOR = new QName("author");
    public static final QName URL = new QName("url");

    private Map<String,Book> booksById = new HashMap<>();
    private Map<String,List<Book>> booksByAuthor = new TreeMap<>();
    private Map<String,List<Book>> booksByTitle = new TreeMap<>();

    public Store() throws FileNotFoundException, XMLStreamException {
        XMLInputFactory f = XMLInputFactory.newFactory();
        XMLStreamReader r = f.createXMLStreamReader(new FileInputStream("books.xml"));

        while (r.hasNext()) {
            r.next();
            switch (r.getEventType()) {
                case START_DOCUMENT:
                    break;
                case END_DOCUMENT:
                    break;

                case START_ELEMENT:
                    if (r.getName().equals(BOOKS)) {
                        parseBooks(r);
                    }
                    break;
                case END_ELEMENT:
                    break;

            }
        }
    }

    protected void parseBooks(XMLStreamReader r) throws XMLStreamException {
        while (r.hasNext()) {
            r.next();
            switch (r.getEventType()) {
                case START_ELEMENT:
                    if (r.getName().equals(BOOK)) {
                        parseBook(r);
                    }
                    break;
                case END_ELEMENT:
                    if ( r.getName().equals(BOOKS) ) {
                        return;
                    }
            }
        }
    }

    protected void parseBook(XMLStreamReader r) throws XMLStreamException {

        String id = r.getAttributeValue( null, "id" );
        String title = null;
        String author = null;
        String url = null;

        while (r.hasNext()) {
            r.next();
            switch (r.getEventType()) {
                case START_ELEMENT:
                    if (r.getName().equals(TITLE)) {
                        title = r.getElementText();
                    }
                    if ( r.getName().equals(AUTHOR) ) {
                        author = r.getElementText();
                    }
                    if (r.getName().equals(URL ) ) {
                        url = r.getElementText();
                    }
                    break;
                case END_ELEMENT:
                    if ( r.getName().equals(BOOK) ) {
                        addBook( id, title, author, url );
                        return;
                    }
            }
        }
    }

    protected void addBook(String id, String title, String author, String url) {
        Book book = new Book(id, title, author, url );

        List<Book> list = this.booksByAuthor.get(author);
        if ( list == null ) {
            list = new ArrayList<Book>();
            this.booksByAuthor.put( author.toLowerCase(), list );
        }
        list.add( book );

        list = this.booksByTitle.get(title);
        if ( list == null ) {
            list = new ArrayList<>();
            this.booksByTitle.put( title.toLowerCase(), list );
        }
        list.add( book );

        this.booksById.put( id, book );

    }

    public Book get(String id) {
        return this.booksById.get(id);
    }

    public static class SearchResult {
        public static SearchResult EMPTY = new SearchResult( Collections.emptyList(), 0, 0 );

        private final Collection<Book> results;

        private final int page;

        private final int numPages;

        public SearchResult(Collection<Book> results, int page, int numPages) {
            this.results = results;
            this.page = page;
            this.numPages = numPages;
        }

        public Collection<Book> getResults() {
            return this.results;
        }

        public int getPage() {
            return this.page;
        }

        public int getNumberOfPages() {
            return this.numPages;
        }
    }

    public SearchResult search(String q, int page) {
        int PAGE_SIZE = 20;
        Set<Book> seen = new HashSet<>();

        List<Book> results = new ArrayList<>();

        q = q.toLowerCase();

        List<Book> chunk = null;

        for (Map.Entry<String, List<Book>> each : this.booksByTitle.entrySet()) {
            if ( each.getKey().contains(q) ) {
                chunk = new ArrayList<>(each.getValue());
                chunk.removeAll( seen );
                results.addAll(chunk);
                seen.addAll( chunk );
            }
        }

        for (Map.Entry<String, List<Book>> each : this.booksByAuthor.entrySet()) {
            if ( each.getKey().contains(q) ) {
                chunk = new ArrayList<>(each.getValue());
                chunk.removeAll(seen);
                results.addAll(chunk);
                seen.addAll( chunk );
            }
        }

        int numPages = ( results.size() / PAGE_SIZE ) + 1;

        List<Book> returnedResults;

        int start = page * PAGE_SIZE;
        int end = page * PAGE_SIZE + PAGE_SIZE;

        if ( start > results.size() ) {
            returnedResults =  Collections.emptyList();
        } else if ( end > results.size() ) {
            returnedResults = results.subList( start, results.size() );
        } else {
            returnedResults = results.subList(start, end);
        }

        if ( returnedResults.isEmpty() ) {
            return SearchResult.EMPTY;
        }

        return new SearchResult(returnedResults, page + 1, numPages );
    }
}
