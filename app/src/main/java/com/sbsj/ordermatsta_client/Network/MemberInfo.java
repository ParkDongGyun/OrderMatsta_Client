package com.sbsj.ordermatsta_client.Network;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class MemberInfo {
	@SerializedName("memberlist")
	private ArrayList<MemberInfoColumn> memberlist = new ArrayList<>();

	public ArrayList<MemberInfoColumn> getMemberlist() {
		return memberlist;
	}
}
