package com.example.imagevrakapp.service.web.download;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.imagevrakapp.service.ImageProcessor;
import com.example.imagevrakapp.service.web.ImageService;

import android.annotation.SuppressLint;
import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class ImageLoader extends Thread {

	private static final int IMAGES_PER_REQUEST = 23;
	protected static int imagesProcessedCount = 0;
	private Context context;
	private LinearLayout imagesLayout;
	private volatile boolean running;

	public ImageLoader(Context context, LinearLayout imagesLayout) {
		running = true;
		this.context = context;
		this.imagesLayout = imagesLayout;
	}
	
	@Override
	public void run() {
		loadImages();
		running = false;
	}
	private void loadImages() {
			String images;
			try {
				images = new ImageService().execute().get();
				prepare(images);
			} catch (Exception e) {
				Log.e("ImageLoader", "Error occured while processing images" + e.getMessage());
			}
	}
	
	protected void prepare(String images) throws JSONException {
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
	protected void processImage(JSONObject[] objects) {
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
