package me.kickscar.practices.jpa03.model01.domain;

import java.util.Date;

import javax.persistence.*;

@Entity
@Table( name = "guestbook" )
public class Guestbook {
	@Id
	@Column(name = "no")
	@GeneratedValue( strategy = GenerationType.IDENTITY  )
	private Long no;

	@Column( name = "name", nullable = false, length = 100 )
	private String name;

	@Column( name = "contents", nullable = false )
	@Lob
	private String contents;

	@Column( name = "password", nullable = false, length = 32 )
	private String password;

	@Column( name = "reg_date", nullable = false )
	@Temporal( value = TemporalType.TIMESTAMP )
	private Date regDate = new Date();

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

	public String getContents() {
		return contents;
	}

	public void setContents(String contents) {
		this.contents = contents;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Date getRegDate() {
		return regDate;
	}

	public void setRegDate(Date regDate) {
		this.regDate = regDate;
	}

	@Override
	public String toString() {
		return "Guestbook{" +
				"no=" + no +
				", name='" + name + '\'' +
				", contents='" + contents + '\'' +
				", password='" + password + '\'' +
				", regDate=" + regDate +
				'}';
	}
}
