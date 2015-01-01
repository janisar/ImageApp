package com.example.imagevrakapp.service.web.upload;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import com.example.imagevrakapp.core.Entry;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;


public class Uploader extends AsyncTask<Entry, Void, String>{
	
	private static final String UPLOAD_URL = "http://image-click.appspot.com/url";
	
	@Override
	protected String doInBackground(Entry... params) {
		Log.i("UPLOADER", "Comment is  " + params[0].getComment());
		Log.i("UPLOADER", "Header is " + params[0].getHeader());
		try {
			return postData(params[0]);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private String getUploadUrl(HttpClient httpclient) {
		HttpGet httpPost = new HttpGet(UPLOAD_URL);
		HttpResponse urlResponse;
		InputStream in = null;
		try {
			urlResponse = httpclient.execute(httpPost);
			HttpEntity resultEntity = urlResponse.getEntity();
			in = resultEntity.getContent();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		String str = "";
		while (true) {
		    int ch = -1;
			try {
				ch = in.read();
			} catch (IOException e) {
				e.printStackTrace();
			}
		    if (ch == -1) {
		    	break;
		    }
		    str += (char) ch;
		}
		return str;
	}
	
	public String postData(Entry entry) throws UnsupportedEncodingException {
		HttpClient httpclient = new DefaultHttpClient();
		String url = getUploadUrl(httpclient);
		HttpPost httpPost = new HttpPost(url);
		MultipartEntityBuilder builder = MultipartEntityBuilder.create();
		builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
		
		ByteArrayBody bab = getImageByteArrayBody(entry);
		builder.addPart("comment", new StringBody(entry.getComment(), ContentType.DEFAULT_TEXT));
		builder.addPart("header", new StringBody(entry.getHeader(), ContentType.DEFAULT_TEXT));
		builder.addPart("myFile", bab);
		
		httpPost.setEntity(builder.build());
		String result = null;
		try {
			HttpResponse response = httpclient.execute(httpPost);
			HttpEntity httpEntity = response.getEntity();
	        result = EntityUtils.toString(httpEntity);
			Log.i("UPLOADER result", result);

		} catch (IOException e) {
			Log.i("UPLOADER", "Failed to do http post " + e.getCause());
		}
		return result;
	}

	private ByteArrayBody getImageByteArrayBody(Entry entry) {
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		Bitmap bitmap = entry.getBitmap();
		bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
		byte[] byteArray = stream.toByteArray();
		ByteArrayBody bab = new ByteArrayBody(byteArray, "image.jpg");
		return bab;
	}	
}
