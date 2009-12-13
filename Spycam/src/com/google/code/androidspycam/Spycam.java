package com.google.code.androidspycam;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.gesture.Gesture;
import android.gesture.GestureLibraries;
import android.gesture.GestureLibrary;
import android.gesture.GestureOverlayView;
import android.gesture.Prediction;
import android.gesture.GestureOverlayView.OnGesturePerformedListener;
import android.graphics.drawable.Drawable;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.code.androidspycam.listeners.SpycamSensorEventListener;
import com.google.code.androidspycam.threads.ImageFetchThread;
import com.google.code.androidspycam.threads.OrientationThread;

public class Spycam extends Activity implements OnClickListener, OnGesturePerformedListener {
	
    public static final boolean DEBUG = false;	
    
    private static final int MOVE_LEFT = 1;
    private static final int MOVE_RIGHT = 2;
    private static final int MOVE_UP = 3;
    private static final int MOVE_DOWN = 4;
    
    private int prefAngleH = 20;
    private int prefAngleV = 5;
    
    private SensorManager sensorManager;
    private Sensor sensor;
    private SpycamSensorEventListener spycamSensorEventListener;
    public boolean isRunning = true;
    public boolean isKilled = false;
    private Drawable drawable;

	public static final String TAG = "spycam";

	private static final int REQUEST_CODE_PREFERENCES = 1000;
	
	private Button leftButton;
	private Button rightButton;
	private Button upButton;
	private Button downButton;

    private GestureLibrary mLibrary;
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        
        sensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
        spycamSensorEventListener=new SpycamSensorEventListener();
        
        enableOrientationSensors();
        imageFetchThread.start();
        orientationThread.start();
        
        leftButton = (Button) findViewById(R.id.b_left);
        rightButton = (Button) findViewById(R.id.b_right);
        upButton = (Button) findViewById(R.id.b_up);
        downButton = (Button) findViewById(R.id.b_down);
        
        leftButton.setOnClickListener(this);
        rightButton.setOnClickListener(this);
        upButton.setOnClickListener(this);
        downButton.setOnClickListener(this);

        mLibrary = GestureLibraries.fromRawResource(this, R.raw.gestures);
        if (!mLibrary.load()) {
        	finish();
        }
        
        GestureOverlayView gestures = (GestureOverlayView) findViewById(R.id.gestures);
        gestures.addOnGesturePerformedListener(this);
    }
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu)
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);		

		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu, menu);
		
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
			case R.id.menu_about: {
				// Show the about dialog for this app.
				showAboutDialog();
				return true;
			}
			case R.id.menu_preferences: {
				// Show the preferences.
		        Intent launchPreferencesIntent = new Intent().setClass(this, PreferenceActivity.class);
		        
		        //Make it a subactivity so we know when it returns.
		        startActivityForResult(launchPreferencesIntent, REQUEST_CODE_PREFERENCES);
				return true;
			}
		}
		return super.onOptionsItemSelected(item);
	}

	public void onClick(View v) {
		if(leftButton.equals(v)){
			left();
		}else if(rightButton.equals(v)){
			right();
		}else if(upButton.equals(v)){
			up();
		}else if(downButton.equals(v)){
			down();
		}else{
			throw new IllegalArgumentException("What was clicked?");
		}
	}

	public void onGesturePerformed(GestureOverlayView overlay, Gesture gesture) {
		ArrayList<Prediction> predictions = mLibrary.recognize(gesture);

		// We want at least one prediction
		if (predictions.size() > 0) {
			Prediction prediction = predictions.get(0);
			//Toast.makeText(this, "pred: "+prediction.score, Toast.LENGTH_SHORT).show();
			// We want at least some confidence in the result
			if (prediction.score > 1.0) {
				if(prediction.name.equals("left")){
					left();
				}else if(prediction.name.equals("right")){
					right();
				}else if(prediction.name.equals("up")){
					up();
				}else if(prediction.name.equals("down")){
					down();
				}else if(prediction.name.equals("rotate360")){
					rotate360();
				}else{
					throw new IllegalArgumentException("What was gestured?");
				}
			}
		}
	}

	private void rotate360() {
		// TODO Auto-generated method stub
		Toast.makeText(this, "rotate 360", Toast.LENGTH_SHORT).show();
		
	}

	private void down() {
		move(MOVE_DOWN);
	}

	private void up() {
		move(MOVE_UP);
	}

	private void right() {
		move(MOVE_RIGHT);
	}

	private void left() {
		move(MOVE_LEFT);
	}
	
	private void move(int direction) {
		URL url;
		String options = null;
		
		switch(direction) {
			case MOVE_LEFT: 
				options = "rpan=-"+prefAngleH;
				break;
			case MOVE_RIGHT:
				options = "rpan="+prefAngleH;
				break;
			case MOVE_UP: 
				options = "rtilt="+prefAngleV;
				break;
			case MOVE_DOWN:
				options = "rtilt=-"+prefAngleV;
				break;
				
		}
		
		// TODO move getContent in a Thread
		try {
			if(options != null) {
				url = new URL("http://192.168.100.116/axis-cgi/com/ptz.cgi?"+options);
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

	private void showAboutDialog() {
		Intent intent=new Intent("org.openintents.action.SHOW_ABOUT_DIALOG");

		//Supply the image name and package.
		intent.putExtra("org.openintents.extra.ICON_RESOURCE", getResources().getResourceName(R.drawable.icon));
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
		
		try{
			startActivityForResult(intent, 0);
		}catch(ActivityNotFoundException e){
			try{
				Toast.makeText(this, getString(R.string.about_backup), Toast.LENGTH_LONG).show();
				startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.link_about_dialog))));
			}catch(ActivityNotFoundException e2){
				Toast.makeText(this, getString(R.string.market_backup), Toast.LENGTH_LONG).show();
			}
		}
	}

	private void enableOrientationSensors(){
		sensorManager.registerListener(spycamSensorEventListener, sensor, SensorManager.SENSOR_DELAY_NORMAL);	
	}
	
	private void disableOrientationSensors(){
		sensorManager.unregisterListener(spycamSensorEventListener);
	}
	
	private Thread imageFetchThread = new ImageFetchThread(this);
	
	private Thread orientationThread = new OrientationThread(this);
	
	protected void onDestroy() {
		disableOrientationSensors();
		isRunning=false;
		isKilled=true;
		super.onDestroy();
	};
	
	@Override
	protected void onResume() {
		isRunning=true;
		enableOrientationSensors();
		super.onResume();
	}
	
	@Override
	protected void onPause() {
		isRunning=false;
		disableOrientationSensors();
		super.onPause();
	}
	
	protected Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			((ImageView)findViewById(R.id.image)).setImageDrawable(drawable);
		};
	};
	
	

	public Handler getHandler() {
		return handler;
	}

	public SpycamSensorEventListener getSpycamSensorEventListener() {
		return spycamSensorEventListener;
	}

	public void setDrawable(Drawable drawable) {
		this.drawable = drawable;
	}
	
	
	
}
