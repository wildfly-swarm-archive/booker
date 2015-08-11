package org.wildfly.swarm.booker.store;

/**
 * @author Bob McWhirter
 */
public class Book {

    private final String id;

    private final String title;

    private final String author;

    private final String url;

    private int price;

    public Book(String id, String title, String author, String url) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.url = url;
    }

    public String getId() {
        return this.id;
    }

    public String getTitle() {
        return this.title;
    }

    public String getAuthor() {
        return this.author;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getPrice() {
        return this.price;
    }

    public String toString() {
        return "[" + this.id + ": " + this.title + "]";
    }
}
