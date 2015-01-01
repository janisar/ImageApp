package com.example.imagevrakapp.core;

import android.graphics.Bitmap;

public class Entry {
	
	private Bitmap bitmap;
	private String comment;
	private String header;
	
	public Entry(Bitmap bitmap, String comment, String header) {
		this.bitmap = bitmap;
		this.comment = comment;
		this.header = header;
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
