package com.google.code.androidspycam;

import android.os.Bundle;

public class PreferenceActivity extends android.preference.PreferenceActivity {

	/**
	 * Id for the preferences registry.
	 */
	public static final String PREFS =Spycam.APP_NAME+" Prefs";
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        
        //Make sure these preferences are coupled to the right preferences file and mode.
        getPreferenceManager().setSharedPreferencesMode(MODE_PRIVATE);
		getPreferenceManager().setSharedPreferencesName(PREFS);
		
        //Load the preferences from an XML resource.
		addPreferencesFromResource(R.xml.preferences);
	}

}
