package me.kickscar.practices.jpa03.model05.domain;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table( name = "orders" )
public class Orders {
    @Id
    @Column( name = "no" )
    @GeneratedValue( strategy = GenerationType.IDENTITY  )
    private Long no;

    @Column(name = "name", nullable = false, length = 200)
    private String name;

    @Column(name = "address", nullable = false, length = 200)
    private String address;

    @Column(name = "total_price", nullable = false )
    private Integer totalPrice;

    @Column( name = "reg_date", nullable = false )
    @Temporal( value = TemporalType.TIMESTAMP )
    private Date regDate = new Date();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_no",  nullable = true, insertable = false, updatable = false)
    private User user;

    public Orders(){
    }

    public Orders(String name){
        this.name = name;
    }

    public Long getNo() {
        return no;
    }

    public void setNo(Long no) {
        this.no = no;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Integer totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Date getRegDate() {
        return regDate;
    }

    public void setRegDate(Date regDate) {
        this.regDate = regDate;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
//      f(!user.getOrders().contains(this)) {
//         user.getOrders().add(this);
//      }
    }

    @Override
    public String toString() {
        return "Order{" +
                "no=" + no +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", totalPrice=" + totalPrice +
                ", regDate=" + regDate +
//무한루프조심!!!  ", user=" + user +
                '}';
    }
}
