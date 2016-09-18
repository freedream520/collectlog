package com.piqu.collectlog.entity;

public class Fish extends BaseEntity {
	
	private int fishtype;
	private int userid;
	private int fishid;
	private int canno;
	private int score;
	private int bull_score;
	private String date;
	private String servername;
	public String getServername() {
		return servername;
	}
	public void setServername(String servername) {
		this.servername = servername;
	}
	public int getFishtype() {
		return fishtype;
	}
	public void setFishtype(int fishtype) {
		this.fishtype = fishtype;
	}
	public int getUserid() {
		return userid;
	}
	public int getScore() {
		return score;
	}
	public void setScore(int score) {
		this.score = score;
	}
	public void setUserid(int userid) {
		this.userid = userid;
	}
	public int getFishid() {
		return fishid;
	}
	public void setFishid(int fishid) {
		this.fishid = fishid;
	}
	public int getCanno() {
		return canno;
	}
	public void setCanno(int canno) {
		this.canno = canno;
	}
	public int getBull_score() {
		return bull_score;
	}
	public void setBull_score(int bull_score) {
		this.bull_score = bull_score;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}

}
