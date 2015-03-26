package com.quya.model;

import java.io.Serializable;

public class User implements Serializable{
	private int id;
	private String name;
	private String password;
	private int credits;
	private String sex;
	private String email;
	private String phone;
	private int photoId;
	private String photoUrl;
	private int power;
	private Address address;
	private String indroduce;
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
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public int getCredits() {
		return credits;
	}
	public void setCredits(int credits) {
		this.credits = credits;
	}
	
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public int getPhotoId() {
		return photoId;
	}
	public void setPhotoId(int photoId) {
		this.photoId = photoId;
	}
	public String getPhotoUrl() {
		return photoUrl;
	}
	public void setPhotoUrl(String photoUrl) {
		this.photoUrl = photoUrl;
	}
	public int getPower() {
		return power;
	}
	public void setPower(int power) {
		this.power = power;
	}
	
	public Address getAddress() {
		return address;
	}
	public void setAddress(Address address) {
		this.address = address;
	}
	public String getIndroduce() {
		return indroduce;
	}
	public void setIndroduce(String indroduce) {
		this.indroduce = indroduce;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	@Override
	public String toString() {
		return "User [id=" + id + ", name=" + name + ", password=" + password
				+ ", credits=" + credits + ", sex=" + sex + ", email=" + email
				+ ", phone=" + phone + ", photoId=" + photoId + ", photoUrl="
				+ photoUrl + ", power=" + power + ", address=" + address
				+ ", indroduce=" + indroduce + "]";
	}
	
	
}
