package me.kickscar.practices.jpa03.model11.domain;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "user")
public class User {

	@Id
	@Column(name = "no")
	@GeneratedValue( strategy = GenerationType.IDENTITY  )
	private Long no;

	@Column(name = "name", nullable = false, length = 20)
	private String name;

	@Column(name = "email", nullable = false, length = 200)
	private String email;

	@Column(name = "password", nullable = false, length = 128)
	private String password;

	@OneToMany(mappedBy = "user", fetch = FetchType.LAZY )
	private List<CartItem> cart = new ArrayList<CartItem>();

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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public List<CartItem> getCart() {
		return cart;
	}

	public void setCart(List<CartItem> cart) {
		this.cart = cart;
	}

	@Override
	public String toString() {
		return "User{" +
				"no=" + no +
				", name='" + name + '\'' +
				", email='" + email + '\'' +
				", password='" + password + '\'' +
				// for debugging
				// ", cart=" + cart +
				'}';
	}
}