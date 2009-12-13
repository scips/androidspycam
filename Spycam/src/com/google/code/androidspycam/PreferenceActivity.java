package com.google.code.androidspycam;

import android.os.Bundle;

public class PreferenceActivity extends android.preference.PreferenceActivity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
//		addPreferencesFromResource(R.xml.preferences);
		setContentView(R.xml.preferences);
	}

}
