package com.smart.entities;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;


@Entity
public class Contact {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int cid;
	private String name;
	private String work;
	private String email;
	private String imageUrl; 
	
	@Column(length=50000)
	private String description;
	private String nickName;
	private String address;
	
	@Temporal(TemporalType.DATE)
	private Date joinedDate;
	
	private String phone;
	
	@ManyToOne
	@JsonIgnore
	private User user;
	
	
	public Contact() {
		super();
		// TODO Auto-generated constructor stub
	}


	public int getCid() {
		return cid;
	}


	public void setCid(int cid) {
		this.cid = cid;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getWork() {
		return work;
	}


	public void setWork(String work) {
		this.work = work;
	}


	public String getEmail() {
		return email;
	}


	public void setEmail(String email) {
		this.email = email;
	}


	public String getImageUrl() {
		return imageUrl;
	}


	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}


	public String getDescription() {
		return description;
	}


	public void setDescription(String description) {
		this.description = description;
	}


	public String getNickName() {
		return nickName;
	}


	public void setNickName(String nickName) {
		this.nickName = nickName;
	}


	public String getPhone() {
		return phone;
	}


	public void setPhone(String phone) {
		this.phone = phone;
	}


	public User getUser() {
		return user;
	}


	public void setUser(User user) {
		this.user = user;
	}
	
	
	
	//To be studied

	public Date getJoinedDate() {
		return joinedDate;
	}


	public void setJoinedDate(Date joinedDate) {
		this.joinedDate = joinedDate;
	}


	@Override
	public boolean equals(Object obj) {
		// TODO Auto-generated method stub
		return this.cid==((Contact)obj).getCid();
	}


	@Override
	public String toString() {
		return "Contact [cid=" + cid + ", name=" + name + ", work=" + work + ", email=" + email + ", imageUrl="
				+ imageUrl + ", description=" + description + ", nickName=" + nickName + ", joinedDate=" + joinedDate
				+ ", phone=" + phone + ", user=" + user + "]";
	}


	
}
