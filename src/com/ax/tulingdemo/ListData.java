/*
 * 封装数据
 */
package com.ax.tulingdemo;

import android.text.Spanned;

public class ListData {
	public static int SEND = 1;
	public static int RECEIVE = 2;
	String content;
	Spanned html;
	private int flag;
	private String time;
	
	public ListData(String content, int flag, String time, Spanned html) {
		setContent(content);
		this.flag = flag;
		setTime(time);
		this.html = html;
	}
	public String getContent(){
		return content;
	}
	public void setContent(String content){
		this.content = content;
	}
	
	public int getFlag(){
		return this.flag;
	}
	public void setFlag(int flag){
		this.flag = flag;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public Spanned getHtml() {
		return html;
	}
	public void setHtml(Spanned html) {
		this.html = html;
	}
}
