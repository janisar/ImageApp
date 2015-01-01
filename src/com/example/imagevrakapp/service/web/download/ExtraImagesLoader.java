package com.example.imagevrakapp.service.web.download;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;



import android.content.Context;
import android.util.Log;
import android.widget.LinearLayout;

public class ExtraImagesLoader extends ImageLoader {

	private static final int IMAGES_TO_ADD = 6;
	
	public ExtraImagesLoader(Context context, LinearLayout imagesLayout) {
		super(context, imagesLayout);
	}
	
	@Override
	protected void prepare(String images) throws JSONException {
		JSONObject reader = new JSONObject(images);
		JSONArray array = reader.getJSONArray("entities");
		int imagesToLoad = imagesProcessedCount + IMAGES_TO_ADD;
		
		for (int i = imagesProcessedCount; i < array.length() && i < imagesToLoad; i += 3) {
			Log.i("Extra images loader", "Loading new image" + array.getString(i));
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
}
