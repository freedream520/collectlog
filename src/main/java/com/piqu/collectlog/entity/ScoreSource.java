package com.piqu.collectlog.entity;

public class ScoreSource extends BaseEntity {
	
	private int userid;
	private int score;
	private int bullScore;
	private int amount;
	private String comment;
	private String date;
	public int getAmount() {
		return amount;
	}
	public void setAmount(int amount) {
		this.amount = amount;
	}
	public int getUserid() {
		return userid;
	}
	public void setUserid(int userid) {
		this.userid = userid;
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
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}

}
