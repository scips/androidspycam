package com.google.code.androidspycam.threads;

import java.io.IOException;
import java.net.MalformedURLException;

import android.util.Log;

import com.google.code.androidspycam.Spycam;
import com.google.code.androidspycam.Util;

public class ImageFetchThread extends Thread {
	protected Spycam parent;
	
	public ImageFetchThread(Spycam parent) {
		super();
		this.parent=parent;
	}
	
	@Override
	public void run() {
		while(!parent.isKilled){
			if(parent.isRunning){
    			try {
					parent.setDrawable(Util.fetchImage("http://192.168.100.116/axis-cgi/jpg/image.cgi"));
					parent.getHandler().sendEmptyMessage(0);
					Log.v(getClass().getName(),"imageFetchThread");
				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
			}
			try{
				Thread.sleep(200);
			}
			catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
