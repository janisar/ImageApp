package com.example.imagevrakapp.service;

import java.util.concurrent.ExecutionException;

import org.json.JSONException;
import org.json.JSONObject;

import com.example.imagevrakapp.form.ImageDetailsView;
import com.example.imagevrakapp.service.web.download.DownloadImageTask;

import android.annotation.SuppressLint;
import android.app.ActionBar.LayoutParams;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

public class ImageProcessor implements Runnable {

	private Context context;
	private JSONObject object;
	private Thread t;
	private volatile ImageView currentImage;
	
	public ImageProcessor(Context context, JSONObject o) {
		this.context = context;
		this.currentImage = new ImageView(context);
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
	
	public void start() {
		if (t == null) {
			t = new Thread(this);
	        t.start();
	    }
	}
	
	public ImageView getCurrentImage() {
		return currentImage;
	}
}
