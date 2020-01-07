package me.kickscar.practices.jpa03.model11.domain;

import javax.persistence.*;

@Entity
@Table(name = "cartitem")
public class CartItem {
    @EmbeddedId
    public CartItemId catrtItemId = new CartItemId();

    @MapsId("bookNo")
    @ManyToOne
    @JoinColumn(name="book_no")
    private Book book;

    @MapsId("userNo")
    @ManyToOne
    @JoinColumn(name="user_no")
    private User user;

    @Column(name = "amount", nullable = false)
    private Integer amount;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "CartItem{" +
                "user=" + user +
                ", book=" + book +
                ", amount=" + amount +
                '}';
    }
}
