package com.example.imagevrakapp.service.web.download;

import java.io.InputStream;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

public class DownloadImageTask extends AsyncTask<String, Void, Bitmap>{

	 private ImageView imageView;

	 public DownloadImageTask(ImageView bmImage) {
		 this.imageView = bmImage;
	 }
	
	 @Override
	 protected Bitmap doInBackground(String... urls) {
		 String urldisplay = urls[0];
	        Bitmap bitmap = null;
	        try {
	            InputStream in = new java.net.URL(urldisplay).openStream();
	            bitmap = BitmapFactory.decodeStream(in);
	        } catch (Exception e) {
	            Log.e("Error", e.getMessage());
	        }
	        return bitmap;
	 }
	 
	@Override
 	protected void onPostExecute(Bitmap result) {
		if (imageView != null) {
			imageView.setImageBitmap(result);
		}
	}
}
