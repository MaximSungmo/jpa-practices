package me.kickscar.practices.jpa03.model13.dto;

import com.querydsl.core.annotations.QueryProjection;

public class CartItemDto {
    private Long bookNo;
    private String bookTitle;
    private Integer bookPrice;
    private Integer amount;

    public CartItemDto(){
    }

    @QueryProjection
    public CartItemDto(Long bookNo, String bookTitle, Integer bookPrice, Integer amount){
        this.bookNo = bookNo;
        this.bookTitle = bookTitle;
        this.bookPrice = bookPrice;
        this.amount = amount;
    }

    public Long getBookNo() {
        return bookNo;
    }

    public void setBookNo(Long bookNo) {
        this.bookNo = bookNo;
    }

    public String getBookTitle() {
        return bookTitle;
    }

    public void setBookTitle(String bookTitle) {
        this.bookTitle = bookTitle;
    }

    public Integer getBookPrice() {
        return bookPrice;
    }

    public void setBookPrice(Integer bookPrice) {
        this.bookPrice = bookPrice;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "CartItemDto{" +
                "bookNo=" + bookNo +
                ", bookTitle='" + bookTitle + '\'' +
                ", bookPrice=" + bookPrice +
                ", amount=" + amount +
                '}';
    }
}