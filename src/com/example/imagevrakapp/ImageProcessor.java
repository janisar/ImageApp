package com.example.imagevrakapp;

import java.util.concurrent.ExecutionException;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class ImageProcessor implements Runnable {

	Context context;
	JSONObject object;
	LinearLayout imagesLayout;
	Thread t;
	
	public ImageProcessor(Context context, LinearLayout imagesView, JSONObject o) {
		this.context = context;
		this.imagesLayout = imagesView;
		object = o;
	}

	@SuppressLint("NewApi")
	@Override
	public void run() {
		String imageUrl = null;
		try {
			imageUrl = object.getString("image");
		} catch (JSONException e1) {
			e1.printStackTrace();
		}
		ImageView currentImage = new ImageView(context);
		try {
			loadBitmapToImageView(currentImage, imageUrl);
		} catch (Exception e) {
			Log.e("Processing image", "Something went wrong while processing image");
			e.printStackTrace();
		} 
		LayoutParams params = new LayoutParams(150, 150);
		params.setMargins(50, 0, 50, 0);
		try {
			currentImage.setOnClickListener(imageClickListener(currentImage, object));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		currentImage.setLeft(100);
		imagesLayout.addView(currentImage, params);
	}
	
	public Bitmap loadBitmapToImageView(ImageView currentImage, String url) throws InterruptedException, ExecutionException {
		return new DownloadImageTask(currentImage).execute(url).get();
	}
	
	private OnClickListener imageClickListener(final ImageView currentImage,
			JSONObject object) throws JSONException {
		final String comment = object.getString("comment");
		final String header = object.getString("header");
		final String date = object.getString("date");
		
		return new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				ImageDetailsView imageDetailsView = new ImageDetailsView(context, currentImage, comment, header, date);
				AlertDialog alertDialog = new AlertDialog.Builder(context).create();
    			alertDialog.setView(imageDetailsView);
    			alertDialog.show();
			}
		};
	}
	
	public void start () {
		if (t == null) {
			t = new Thread(this, "Thread");
	        t.start();
	    }
	}
}
