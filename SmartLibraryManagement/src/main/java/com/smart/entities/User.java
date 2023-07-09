package com.smart.entities;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
public class User {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;
	
	@NotBlank(message="Name cannot be empty !!")
	@Size(min=2,max=20, message="Minimum 2 and maximum 20 characters are allowed .")
	private String name;
	
	@Column(unique=true)
	private String email;
	private String role;
	private String password;
	
	@Column(length=500)
	private String about;
	private boolean enabled;
	private String imageUrl;
	
	@OneToMany(cascade=CascadeType.ALL,mappedBy="user",orphanRemoval=true)
	private List<Contact> contacts;
	
	public User() {
		super(); 
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getAbout() {
		return about;
	}

	public void setAbout(String about) {
		this.about = about;
	}

	public boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(boolean b) {
		this.enabled = b;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public List<Contact> getContacts() {
		return contacts;
	}

	public void setContacts(List<Contact> contacts) {
		this.contacts = contacts;
	}

	/*
	 * @Override public String toString() { return "User [id=" + id + ", name=" +
	 * name + ", email=" + email + ", role=" + role + ", password=" + password +
	 * ", about=" + about + ", enabled=" + enabled + ", imageUrl=" + imageUrl +
	 * ", contacts=" + contacts + "]"; }
	 */

	

	
}
