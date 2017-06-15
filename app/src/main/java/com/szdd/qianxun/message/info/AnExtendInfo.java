package com.szdd.qianxun.message.info;

public class AnExtendInfo {

	private String sign = "";// 签名
	private String hobby = "";// 爱好
	private String work = "";// 工作
	// 这两个暂时不要
	private String hope = "";// 希望
	private String introduce = "";// 介绍

	public AnExtendInfo(String sign, String hobby, String work) {
		super();
		this.sign = sign;
		this.hobby = hobby;
		this.work = work;
	}

	// getter setter
	public String getSign() {
		return sign;
	}

	public String getHobby() {
		return hobby;
	}

	public String getWork() {
		return work;
	}

	public String getHope() {
		return hope;
	}

	public String getIntroduce() {
		return introduce;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public void setHobby(String hobby) {
		this.hobby = hobby;
	}

	public void setWork(String work) {
		this.work = work;
	}

	public void setHope(String hope) {
		this.hope = hope;
	}

	public void setIntroduce(String introduce) {
		this.introduce = introduce;
	}

}
