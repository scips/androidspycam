package com.google.code.androidspycam.listeners;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;

public class SpycamSensorEventListener implements SensorEventListener{

	private Float azimuth;
	private Float pitch;
	private Float roll;
	
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub
		
	}

	public void onSensorChanged(SensorEvent event) {
		// TODO Auto-generated method stub
		azimuth=event.values[0];
		pitch=event.values[1];
		roll=event.values[2];
	}

	public Float getAzimuth() {
		return azimuth;
	}

	public Float getPitch() {
		return pitch;
	}

	public Float getRoll() {
		return roll;
	}
}
