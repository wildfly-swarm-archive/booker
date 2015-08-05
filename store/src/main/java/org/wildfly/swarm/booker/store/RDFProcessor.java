package org.wildfly.swarm.booker.store;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.nio.file.Path;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;

import static javax.xml.stream.XMLStreamReader.*;

/**
 * @author Bob McWhirter
 */
public class RDFProcessor {

    public static final String DC_TERMS = "http://purl.org/dc/terms/";

    public static final String PG_TERMS = "http://www.gutenberg.org/2009/pgterms/";

    public static final String RDF = "http://www.w3.org/1999/02/22-rdf-syntax-ns#";

    public static final QName EBOOK = new QName(PG_TERMS, "ebook");

    public static final QName TITLE = new QName(DC_TERMS, "title");

    public static final QName HAS_FORMAT = new QName(DC_TERMS, "hasFormat");

    public static final QName FILE = new QName(PG_TERMS, "file");

    public static final QName FORMAT = new QName(DC_TERMS, "format");

    public static final QName DESCRIPTION = new QName(RDF, "Description");

    public static final QName VALUE = new QName(RDF, "value");

    public static final QName CREATOR = new QName(DC_TERMS, "creator");

    public static final QName AGENT = new QName(PG_TERMS, "agent");

    public static final QName NAME = new QName(PG_TERMS, "name");

    private final XMLStreamWriter out;

    private String title;

    private String url;

    private String creator;

    private String format;

    private String id;


    public RDFProcessor(Path outputPath) throws FileNotFoundException, XMLStreamException {
        XMLOutputFactory f = XMLOutputFactory.newFactory();
        this.out = f.createXMLStreamWriter(new FileOutputStream(outputPath.toFile()));
        this.out.writeStartDocument();
        this.out.writeCharacters("\n");
        this.out.writeStartElement("books");
    }

    public void close() throws XMLStreamException {
        this.out.writeEndElement();
        this.out.writeEndDocument();
        this.out.close();
    }

    public void process(Path path) throws FileNotFoundException, XMLStreamException {
        this.title = null;
        this.url = null;
        this.creator = null;
        this.format = null;

        XMLInputFactory f = XMLInputFactory.newInstance();
        XMLStreamReader r = f.createXMLStreamReader(new FileInputStream(path.toFile()));
        while (r.hasNext()) {
            r.next();
            switch (r.getEventType()) {
                case START_DOCUMENT:
                    break;
                case END_DOCUMENT:
                    break;

                case START_ELEMENT:
                    if (r.getName().equals(EBOOK)) {
                        parseEbook(r);
                    }
                    break;
                case END_ELEMENT:
                    break;

            }
        }

        r.close();

        if (this.title != null) {
            this.out.writeStartElement("book");
            this.out.writeAttribute( "id", this.id );
            this.out.writeCharacters("\n");

            this.out.writeCharacters("  ");
            this.out.writeStartElement("title");
            this.out.writeCharacters(this.title);
            this.out.writeEndElement();
            this.out.writeCharacters("\n");

            this.out.writeCharacters("  ");
            this.out.writeStartElement("author");
            this.out.writeCharacters(this.creator);
            this.out.writeEndElement();
            this.out.writeCharacters("\n");

            this.out.writeCharacters("  ");
            this.out.writeStartElement("url");
            this.out.writeCharacters(this.url);
            this.out.writeEndElement();
            this.out.writeCharacters("\n");

            this.out.writeEndElement();
            this.out.writeCharacters("\n");
        }

        /*
        System.err.println( "---" );
        System.err.println( this.title );
        System.err.println( this.creator );
        System.err.println( this.url );
        System.err.println( this.format );
        */
    }

    protected void parseEbook(XMLStreamReader r) throws XMLStreamException {

        String about = r.getAttributeValue(RDF, "about");
        String[] parts = about.split("/");

        this.id = parts[1];

        this.url = "http://www.gutenberg.org/cache/epub/" + this.id + "/pg" + this.id + ".html";
        while (r.hasNext()) {
            r.next();
            switch (r.getEventType()) {
                case START_ELEMENT:
                    /*
                    if (r.getName().equals(HAS_FORMAT)) {
                        if ( this.format == null ) {
                            parseHasFormat(r);
                        }
                    }
                    */
                    if (r.getName().equals(TITLE)) {
                        parseTitle(r);
                    }
                    if (r.getName().equals(CREATOR)) {
                        parseCreator(r);
                    }
                    break;
                case END_ELEMENT:
                    if (r.getName().equals(EBOOK)) {
                        return;
                    }
                    break;
            }
        }
    }

    protected void parseTitle(XMLStreamReader r) throws XMLStreamException {
        this.title = r.getElementText();
    }

    protected void parseCreator(XMLStreamReader r) throws XMLStreamException {

        while (r.hasNext()) {
            r.next();
            switch (r.getEventType()) {
                case START_ELEMENT:
                    if (r.getName().equals(AGENT)) {
                        parseAgent(r);
                    }
                    break;
                case END_ELEMENT:
                    if (r.getName().equals(CREATOR)) {
                        return;
                    }
                    break;
            }
        }
    }

    protected void parseAgent(XMLStreamReader r) throws XMLStreamException {
        while (r.hasNext()) {
            r.next();
            switch (r.getEventType()) {
                case START_ELEMENT:
                    if (r.getName().equals(NAME)) {
                        parseName(r);
                    }
                    break;
                case END_ELEMENT:
                    if (r.getName().equals(AGENT)) {
                        return;
                    }
                    break;
            }
        }
    }

    protected void parseName(XMLStreamReader r) throws XMLStreamException {
        this.creator = r.getElementText();
    }

    protected void parseHasFormat(XMLStreamReader r) throws XMLStreamException {

        while (r.hasNext()) {
            r.next();
            switch (r.getEventType()) {
                case START_ELEMENT:
                    if (r.getName().equals(FILE)) {
                        parseFile(r);
                    }
                    break;
                case END_ELEMENT:
                    if (r.getName().equals(HAS_FORMAT)) {
                        return;
                    }
                    break;
            }
        }
    }

    protected void parseFile(XMLStreamReader r) throws XMLStreamException {
        String url = r.getAttributeValue(RDF, "about");

        while (r.hasNext()) {
            r.next();
            switch (r.getEventType()) {
                case START_ELEMENT:
                    if (r.getName().equals(FORMAT)) {
                        parseFormat(r);
                    }
                    break;
                case END_ELEMENT:
                    if (r.getName().equals(FILE)) {
                        if (this.format.startsWith("text/html")) {
                            this.url = url;
                        } else {
                            this.format = null;
                        }
                        return;
                    }
                    break;
            }
        }
    }

    protected void parseFormat(XMLStreamReader r) throws XMLStreamException {
        if (this.format != null) {
            return;
        }

        while (r.hasNext()) {
            r.next();
            switch (r.getEventType()) {
                case START_ELEMENT:
                    if (r.getName().equals(DESCRIPTION)) {
                        parseDescription(r);
                    }
                    break;
                case END_ELEMENT:
                    if (r.getName().equals(FORMAT)) {
                        return;
                    }
                    break;
            }
        }
    }

    protected void parseDescription(XMLStreamReader r) throws XMLStreamException {

        while (r.hasNext()) {
            r.next();
            switch (r.getEventType()) {
                case START_ELEMENT:
                    if (r.getName().equals(VALUE)) {
                        parseValue(r);
                    }
                    break;
                case END_ELEMENT:
                    if (r.getName().equals(DESCRIPTION)) {
                        return;
                    }
                    break;
            }
        }
    }

    protected void parseValue(XMLStreamReader r) throws XMLStreamException {
        this.format = r.getElementText();
    }

    /*
    public void process(Path path) throws FileNotFoundException, XMLStreamException {

        XMLInputFactory f = XMLInputFactory.newInstance();
        XMLStreamReader r = f.createXMLStreamReader(new FileInputStream(path.toFile()));
        while (r.hasNext()) {
            int event = r.next();
            switch (event) {
                case XMLStreamReader.START_DOCUMENT:
                    break;
                case XMLStreamReader.END_DOCUMENT:
                    break;
                case XMLStreamReader.START_ELEMENT:
                    System.err.println( r.getName() );
                    if (r.getName().equals(TITLE)) {
                        parseTitle(r);
                        break;
                    }
                    if (r.getName().equals(HAS_FORMAT)) {
                        if ( this.format == null ) {
                            parseHasFormat(r);
                        }
                        if ( ! this.format.startsWith( "text/html" ) ) {
                            this.format = null;
                        }
                        break;
                    }
                    if (r.getName().equals(CREATOR)) {
                        parseCreator(r);
                    }
                    break;
                case XMLStreamReader.END_ELEMENT:
                    break;
            }


            //if (this.format != null && this.format.startsWith("text/html")) {
                //break;
            //}
        }

        r.close();

        if (this.url != null) {
            System.err.println( "---" );
            System.err.println("  title: " + this.title);
            System.err.println("    url: " + this.url);
            System.err.println("creator: " + this.creator);
        }
    }

    protected void parseTitle(XMLStreamReader r) throws XMLStreamException {
        this.title = r.getElementText();
        System.err.println("parse title: " + this.title);
    }

    protected void parseCreator(XMLStreamReader r) throws XMLStreamException {

        while (r.hasNext()) {
            int event = r.next();
            switch (event) {
                case XMLStreamReader.START_ELEMENT:
                    if (r.getNamespaceURI().equals(PG_TERMS) && r.getLocalName().equals("agent")) {
                        parseAgent(r);
                    }
            }
        }
    }

    protected void parseAgent(XMLStreamReader r) throws XMLStreamException {
        while (r.hasNext()) {
            int event = r.next();
            switch (event) {
                case XMLStreamReader.START_ELEMENT:
                    if (r.getNamespaceURI().equals(PG_TERMS) && r.getLocalName().equals("name")) {
                        this.creator = r.getElementText();
                        return;
                    }
            }
        }
    }

    protected void parseHasFormat(XMLStreamReader r) throws XMLStreamException {
        LOOP:
        while (r.hasNext()) {
            int event = r.next();

            switch (event) {
                case XMLStreamReader.START_ELEMENT:
                    if (r.getNamespaceURI().equals(PG_TERMS)) {
                        if (r.getLocalName().equals("file")) {
                            parseFile(r);
                        }
                    }
                    break;

                case XMLStreamReader.END_ELEMENT:
                    if (r.getNamespaceURI().equals(DC_TERMS)) {
                        if (r.getLocalName().equals("hasFormat")) {
                            return;
                        }
                    }
                    break;
            }
        }
    }

    protected void parseFile(XMLStreamReader r) throws XMLStreamException {
        this.url = r.getAttributeValue(RDF, "about");
        this.format = null;

        LOOP:
        while (r.hasNext()) {
            int event = r.next();

            switch (event) {
                case XMLStreamReader.START_ELEMENT:
                    if (r.getNamespaceURI().equals(DC_TERMS)) {
                        if (r.getLocalName().equals("format")) {
                            parseFormat(r);
                            break LOOP;
                        }
                    }
                    break;
                case XMLStreamReader.END_ELEMENT:
                    if (r.getNamespaceURI().equals(PG_TERMS) && r.getLocalName().equals("hasFormat")) {
                        return;
                    }
            }
        }

    }

    protected void parseFormat(XMLStreamReader r) throws XMLStreamException {

        while (r.hasNext()) {
            int event = r.next();

            switch (event) {
                case XMLStreamReader.START_ELEMENT:
                    if (r.getNamespaceURI().equals(RDF)) {
                        if (r.getLocalName().equals("Description")) {
                            parseDescription(r);
                        }
                    }
                    break;
                case XMLStreamReader.END_ELEMENT:
                    if (r.getNamespaceURI().equals(DC_TERMS) && r.getLocalName().equals("format")) {
                        return;
                    }
            }
        }
    }

    protected void parseDescription(XMLStreamReader r) throws XMLStreamException {
        if (this.format != null) {
            return;
        }

        while (r.hasNext()) {
            int event = r.next();

            switch (event) {
                case XMLStreamReader.START_ELEMENT:
                    if (r.getNamespaceURI().equals(RDF)) {
                        if (r.getLocalName().equals("value")) {
                            this.format = r.getElementText();
                            return;
                        }
                    }
                    break;
                case XMLStreamReader.END_ELEMENT:
                    if (r.getNamespaceURI().equals("RDF") && r.getLocalName().equals("Description")) {
                        return;
                    }
            }
        }
    }
    */
}
