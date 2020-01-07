package me.kickscar.practices.jpa03.model06.domain;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name="user")
public class User {
	@Id
	@Column(name="id", nullable=false, length=24)
	private String id;

	@Column(name="name", nullable=false, length=24)
	private String name;

	@Column(name="password", nullable=false, length=64)
	private String password;

	@Column(name="join_date", nullable=false)
	@Temporal(value = TemporalType.TIMESTAMP)
	private Date joinDate = new Date();

	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="blog_no", nullable=true)
	private Blog blog;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Date getJoinDate() {
		return joinDate;
	}

	public void setJoinDate(Date joinDate) {
		this.joinDate = joinDate;
	}

	public Blog getBlog() {
		return blog;
	}

	public void setBlog(Blog blog) {
		this.blog = blog;
	}

	@Override
	public String toString() {
		return "User{" +
				"id='" + id + '\'' +
				", name='" + name + '\'' +
				", password='" + password + '\'' +
				", joinDate=" + joinDate +
				", blog=" + blog +
				'}';
	}
}
