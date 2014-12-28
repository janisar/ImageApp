package com.example.imagevrakapp;

import android.graphics.Bitmap;

public class Entry {
	
	private Bitmap bitmap;
	private String comment;
	private String header;
	private String date;
	
	public Entry(Bitmap bitmap, String comment, String header, String date) {
		this.bitmap = bitmap;
		this.comment = comment;
		this.header = header;
		this.date = date;
	}
	
	public String getDate() {
		return date;
	}
	
	public Bitmap getBitmap() {
		return bitmap;
	}
	
	public String getHeader() {
		return header;
	}
	
	public String getComment() {
		return comment;
	}
}
