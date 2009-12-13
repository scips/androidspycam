package com.google.code.androidspycam.threads;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import android.util.Log;

import com.google.code.androidspycam.Spycam;

public class OrientationThread extends Thread {
	protected Spycam parent;
	
	public OrientationThread(Spycam parent){
		this.parent=parent;
	}
	
	public void run() {
		while(!parent.isKilled){
    		if(parent.isRunning){
    			Log.v(getClass().getName(),"orientationThread");
    			URL url;
				try {
					if(parent.getSpycamSensorEventListener().getAzimuth()!=null){
						int multiplier=1;
						url = new URL("http://192.168.100.116/axis-cgi/com/ptz.cgi?pan="+(-1 *Math.round(180-parent.getSpycamSensorEventListener().getAzimuth()))+"&tilt="+(90-parent.getSpycamSensorEventListener().getRoll())*2);
						Object content = url.getContent();
					}
					
				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
    		}
			
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	};
}
