package com.google.code.androidspycam;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;

public class Spycam extends Activity {
	
    public static final boolean DEBUG = false;

	/**
	 * Menu item id's.
	 */
	public static final int MENU_ITEM_PREFS = Menu.FIRST;
	public static final int MENU_ITEM_ABOUT = Menu.FIRST+1;

	public static final String TAG = "spycam";
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu)
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		
		//Show preferences action
		menu.add(ContextMenu.NONE, MENU_ITEM_PREFS, ContextMenu.NONE, R.string.menu_preferences).setIcon(android.R.drawable.ic_menu_preferences);

		//About action
		menu.add(ContextMenu.NONE, MENU_ITEM_ABOUT, ContextMenu.NONE, R.string.menu_about).setIcon(android.R.drawable.ic_menu_info_details);

		// Generate any additional actions that can be performed on the
		// overall list. In a normal install, there are no additional
		// actions found here, but this allows other applications to extend
		// our menu with their own actions.
		Intent intent = new Intent(null, getIntent().getData());
		intent.addCategory(Intent.CATEGORY_ALTERNATIVE);
		menu.addIntentOptions(Menu.CATEGORY_ALTERNATIVE, 0, 0,
				new ComponentName(this, Spycam.class), null,
				intent, 0, null);

		return true;
	}
	/* (non-Javadoc)
	 * @see android.app.Activity#onOptionsItemSelected(android.view.MenuItem)
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case MENU_ITEM_ABOUT: {
				// Show the about dialog for this app.
				showAboutDialog();
				return true;
			}
			case MENU_ITEM_PREFS: {
				/*// Show the preferences.
		        Intent launchPreferencesIntent = new Intent().setClass(this, PreferencesActivity.class);
		        
		        //Make it a subactivity so we know when it returns.
		        startActivityForResult(launchPreferencesIntent, REQUEST_CODE_PREFERENCES);
				return true;*/
			}
		}
		return super.onOptionsItemSelected(item);
	}

	private void showAboutDialog() {
		Intent intent=new Intent("org.openintents.action.SHOW_ABOUT_DIALOG");

		//Supply the image name and package.
		//intent.putExtra("org.openintents.extra.ICON_RESOURCE", getResources().getResourceName(R.drawable.logo_192));
		intent.putExtra("org.openintents.extra.PACKAGE_NAME", getPackageName());
		
		intent.putExtra("org.openintents.extra.APPLICATION_LABEL", getString(R.string.app_name));
		
		//Get the app version
		String version = "?";
		try {
		        PackageInfo pi = getPackageManager().getPackageInfo(getPackageName(), 0);
		        version = pi.versionName;
		} catch (PackageManager.NameNotFoundException e) {
		        Log.e(TAG, "Package name not found", e);
		};
		intent.putExtra("org.openintents.extra.VERSION_NAME", version);
		intent.putExtra("org.openintents.extra.COMMENTS", getString(R.string.about_comments));
		intent.putExtra("org.openintents.extra.COPYRIGHT", getString(R.string.about_copyright));
		intent.putExtra("org.openintents.extra.WEBSITE_LABEL", getString(R.string.about_website_label));
		intent.putExtra("org.openintents.extra.WEBSITE_URL", getString(R.string.about_website_url));
		intent.putExtra("org.openintents.extra.AUTHORS", getResources().getStringArray(R.array.about_authors));
		intent.putExtra("org.openintents.extra.DOCUMENTERS", getResources().getStringArray(R.array.about_documenters));
		intent.putExtra("org.openintents.extra.TRANSLATORS", getResources().getStringArray(R.array.about_translators));
		intent.putExtra("org.openintents.extra.ARTISTS", getResources().getStringArray(R.array.about_artists));
		
		// Supply resource name of raw resource that contains the license:
		intent.putExtra("org.openintents.extra.LICENSE_RESOURCE", getResources()
				.getResourceName(R.raw.license_short));
		//intent.putExtra(AboutIntents.EXTRA_WRAP_LICENSE, false);
		
		startActivityForResult(intent, 0);
	}
}