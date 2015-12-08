package org.wildfly.swarm.booker.library;

import javax.persistence.*;

/**
 * @author Bob McWhirter
 */
@Entity
@Table(name="LibraryItem")
public class LibraryItem {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column
    private String userId;

    @Column
    private String bookId;

    @Transient
    private String title;

    @Transient
    private String author;

    public LibraryItem() {

    }

    public LibraryItem(String userId, String bookId) {
        this.userId = userId;
        this.bookId = bookId;
    }

    public String getUserId() {
        return this.userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getBookId() {
        return this.bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return this.author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

}
