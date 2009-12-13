package com.google.code.androidspycam;

import java.io.File;
import java.io.IOException;

import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;

public class TwitPic {
	
	private String username;
	private String password;
	
	public TwitPic(String username, String password){
		this.username = username;
		this.password = password;
	}
	
	public int upload(String message){
		return 1;
	}
	
	private void sendRequest(){
		DefaultHttpClient httpClient = new DefaultHttpClient();
		HttpContext localContext = new BasicHttpContext();
		HttpPost httpPost = new HttpPost("http://twitpic.com/api/uploadAndPost");
		File f = new File("/path/fileToUpload.txt");
	}

}
