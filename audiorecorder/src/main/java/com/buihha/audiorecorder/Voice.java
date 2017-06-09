package com.buihha.audiorecorder;

import android.graphics.drawable.AnimationDrawable;

public class Voice {
	private String timeString;	//录音时间
	private String url;  //存储路径
	private AnimationDrawable ad;
	
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getTimeString() {
		return timeString;
	}
	public void setTimeString(String timeString) {
		this.timeString = timeString;
	}
	public AnimationDrawable getAd() {
		return ad;
	}
	public void setAd(AnimationDrawable ad) {
		this.ad = ad;
	}
	
}
