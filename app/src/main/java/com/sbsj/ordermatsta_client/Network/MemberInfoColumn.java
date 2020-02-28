package com.sbsj.ordermatsta_client.Network;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class MemberInfoColumn implements Serializable {
	@SerializedName("id")
	private int id;
	@SerializedName("directory")
	private String directory;
	@SerializedName("sns_id")
	private String sns_id;
	@SerializedName("name")
	private String name;
	@SerializedName("phone")
	private String phone;
	@SerializedName("birthday")
	private String birthday;
	@SerializedName("gender")
	private String gender;
	@SerializedName("date")
	private String date;

	public MemberInfoColumn(int id, String directory, String sns_id) {
		this.id = id;
		this.directory = directory;
		this.sns_id = sns_id;
	}

	public int getId() {
		return id;
	}

	public String getDirectory() {
		return directory;
	}

	public String getSns_id() {
		return sns_id;
	}

	public String getName() {
		return name;
	}

	public String getPhone() {
		return phone;
	}

	public String getBirthday() {
		return birthday;
	}

	public String getGender() {
		return gender;
	}

	public String getDate() {
		return date;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setDirectory(String directory) {
		this.directory = directory;
	}

	public void setSns_id(String sns_id) {
		this.sns_id = sns_id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public void setDate(String date) {
		this.date = date;
	}
}