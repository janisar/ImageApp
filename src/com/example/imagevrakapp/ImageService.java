package com.example.imagevrakapp;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.os.AsyncTask;

public class ImageService extends AsyncTask<Void, Void, String>{

	@Override
	protected String doInBackground(Void... params) {
		String data = null;
		try {
			data = getData();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return data;
	}

	private String getData() throws ClientProtocolException, IOException {
		HttpClient client = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet("http://image-click.appspot.com/serve");
		
		HttpResponse response = client.execute(httpGet);
		HttpEntity httpEntity = response.getEntity();
        String result = EntityUtils.toString(httpEntity);
        return result;
	}

}
