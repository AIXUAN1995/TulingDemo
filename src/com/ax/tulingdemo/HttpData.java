package com.ax.tulingdemo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.os.AsyncTask;

public class HttpData extends AsyncTask<String, Void, String>{

	private HttpClient mHttpClient;
	private HttpGet mHttpGet;
	private HttpResponse mHttpResponse;
	private HttpEntity mHttpEntity;
	private InputStream in;
	private HttpGetDataListener listener;
	
	private String url;
	public HttpData(String url, HttpGetDataListener listener) {
		// TODO Auto-generated constructor stub
		this.url = url;
		this.listener = listener;
	}
	@Override
	protected String doInBackground(String... params) {
		// TODO Auto-generated method stub
		try {
			mHttpClient = new DefaultHttpClient();
			mHttpGet = new HttpGet(url);
			mHttpResponse = mHttpClient.execute(mHttpGet);
			mHttpEntity = mHttpResponse.getEntity();
			in = mHttpEntity.getContent();
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			StringBuffer sb = new StringBuffer();
			String line = null;
			while ((line=br.readLine())!=null) {
				sb.append(line);			
			}
			return sb.toString();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@Override
	protected void onPostExecute(String result) {
		// TODO Auto-generated method stub
		listener.getDataUrl(result);
		super.onPostExecute(result);
	}
}
