package com.quya.model;

import java.io.Serializable;

public class Shop implements Serializable{
	private int id;
	private int businessmanId;
	private String indroduce;
	private int addressId;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getBusinessmanId() {
		return businessmanId;
	}
	public void setBusinessmanId(int businessmanId) {
		this.businessmanId = businessmanId;
	}
	public String getIndroduce() {
		return indroduce;
	}
	public void setIndroduce(String indroduce) {
		this.indroduce = indroduce;
	}

	
	public int getAddressId() {
		return addressId;
	}
	public void setAddressId(int addressId) {
		this.addressId = addressId;
	}
	@Override
	public String toString() {
		return "Shop [id=" + id + ", businessmanId=" + businessmanId
				+ ", indroduce=" + indroduce + ", addressId=" + addressId + "]";
	}
	

}
