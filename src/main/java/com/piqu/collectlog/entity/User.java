package com.piqu.collectlog.entity;

import java.util.List;

public class User extends BaseEntity {
	
	private String ip;
	private int userid;
	private String date;
	private String deviceid;
	private int gameid;
	private String channel;
	private String rechargechannel;
	private int amount;
	private int score;
	private int bullScore;
	private Object ext;
	private List<String> logininfo;
	public int getAmount() {
		return amount;
	}
	public void setAmount(int amount) {
		this.amount = amount;
	}
	public int getScore() {
		return score;
	}
	public void setScore(int score) {
		this.score = score;
	}
	public int getBullScore() {
		return bullScore;
	}
	public void setBullScore(int bullScore) {
		this.bullScore = bullScore;
	}
	public List<String> getLogininfo() {
		return logininfo;
	}
	public void setLogininfo(List<String> logininfo) {
		this.logininfo = logininfo;
	}
	public int getUserid() {
		return userid;
	}
	public void setUserid(int userid) {
		this.userid = userid;
	}
	public int getGameid() {
		return gameid;
	}
	public void setGameid(int gameid) {
		this.gameid = gameid;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getDeviceid() {
		return deviceid;
	}
	public void setDeviceid(String deviceid) {
		this.deviceid = deviceid;
	}
	public String getChannel() {
		return channel;
	}
	public void setChannel(String channel) {
		this.channel = channel;
	}
	public String getRechargechannel() {
		return rechargechannel;
	}
	public void setRechargechannel(String rechargechannel) {
		this.rechargechannel = rechargechannel;
	}
	public Object getExt() {
		return ext;
	}
	public void setExt(Object ext) {
		this.ext = ext;
	}

}
