package com.example.imagevrakapp;

import java.util.concurrent.ExecutionException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class ImageLoader extends Thread {

	private static final int IMAGES_PER_REQUEST = 28;
	private int imagesProcessedCount;
	private Context context;
	private LinearLayout imagesLayout;
	private volatile boolean running;

	public ImageLoader(Context context, LinearLayout imagesLayout) {
		running = true;
		this.context = context;
		this.imagesLayout = imagesLayout;
		this.imagesProcessedCount = 0;
	}
	
	@Override
	public void run() {
		loadImages();
		running = false;
	}
	private void loadImages() {
		try {
			String images = new ImageService().execute().get();
			prepare(images);
		} catch (InterruptedException e) {
		} catch (ExecutionException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		} 
	}
	private void prepare(String images) throws JSONException {
		Log.i("MainActivity", images);
		JSONObject reader = new JSONObject(images);
		JSONArray array = reader.getJSONArray("entities");
		
		for (int i = 0; i < array.length(); i += 3) {
			if (i > IMAGES_PER_REQUEST) {
				break;
			}
			JSONObject[] objects = new JSONObject[3];
			objects[0] = (JSONObject) array.get(i);
			if (i + 1 < array.length()) {
				objects[1] = (JSONObject) array.get(i + 1);	
			}
			if (i + 2 < array.length()) {
				objects[2] = (JSONObject) array.get(i + 2);		
			}
			processImage(objects);
			imagesProcessedCount += 3;
		}
	}

	@SuppressLint("NewApi")
	private void processImage(JSONObject[] objects) {
		LinearLayout horizontal = new LinearLayout(context);
		horizontal.setOrientation(LinearLayout.HORIZONTAL);
		LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		layoutParams.setMargins(200, 0, 0, 0);
		horizontal.setLayoutParams(layoutParams);
		
		for (JSONObject o : objects) {
			if (o != null) {
				ImageProcessor t = new ImageProcessor(context, o);
				t.start();
				ImageView image = t.getCurrentImage();
				image.setLeft(100);
				
				LayoutParams params = new LayoutParams(150, 150);
				params.setMargins(50, 0, 50, 0);
				image.setLayoutParams(params);
				horizontal.addView(image);
			}
		}
		imagesLayout.addView(horizontal);
	}
	
	public boolean isRunning() {
		return running;
	}
}
