package com.datumdroid.android.ocr.simple;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

//import com.datumdroid.android.ocr.simple.*;









import com.datumdroid.android.ocr.simple.R.id;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.PictureCallback;
import android.os.Bundle;
import android.os.Environment;
//import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

public class CustomCameraActivity extends Activity {
	protected static final String TAG = null;
	private static String tag;
	private static String msg;
	private Camera mCamera;
	
	private boolean inPreview=false;	//Etat de la camera en preview.
	private CameraPreview mPreview;
	public static final int MEDIA_TYPE_IMAGE = 1;
	public static final int MEDIA_TYPE_VIDEO = 2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.custom_cam_preview);
		
		// Creer le zone de num ss. Cest le layout qui se charge de l'afficher
		CustomDrawableView mcdv = new CustomDrawableView(getApplicationContext(), null);
		// mcdv.getDisplay();
		//int screen_w = mcdv.getDisplay().getWidth();
		if (checkCameraHardware(getBaseContext())) {
			// Create an instance of Camera
			mCamera = getCameraInstance();

			if (mCamera != null) { // Camera DISPO
				inPreview=true;
				Parameters parameters = mCamera.getParameters();
				// creer preview
				CameraPreview mCameraPreview = new CameraPreview(this , mCamera);
				FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
				
				preview.addView(mCameraPreview);
				Thread th = new Thread();
				try {
					th.sleep(500);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				//Autofocus
				List<String> p_focus = parameters.getSupportedFocusModes();
				String focus_current = parameters.getFocusMode();

				// FIXME PICTURE DONE il faut API_14 minimum
				if (p_focus.contains(Parameters.FOCUS_MODE_CONTINUOUS_PICTURE)) {
					parameters.setFocusMode(Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
				} else {
					tag = " check camera ";
					msg = " focus continuous NOK ";
					Log.d(tag, msg);
				}
				
				mCamera.setParameters(parameters);
				
				
			} else { 	// Camera INDISPO
				inPreview=false;
				msg = " camera  indisponible";
				Log.d(tag, msg);
			}
		}

		// Add a listener to the Capture button
		Button captureButton = (Button) findViewById(id.button_capture);
		captureButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// get an image from the camera
				if (mCamera != null) {
					
					
					// get an image from the camera 				
					mCamera.takePicture(null, null, mPicture);
//					
//					Restart the Preview
//					After a picture is taken, you must restart the preview before the user can take another picture. In this example, the restart is done by overloading the shutter button.
				
					tag = "Capture photo ";
					msg =  " cliik bouton Capture photo ";
					Log.d(tag, msg);


				} else {
					msg = " camera est NULL ";
					Log.d(TAG, msg);
				}
			}
		});
	}	// Fin onCreate()



	// FIXME Si je libère onResume je crash 
	// @Override
	// public void onResume() {
	// 	super.onResume();
	// 	if (mCamera != null) {
	// 		mCamera = Camera.open();
	// 	inPreview=true;
	// 	}
		




	// }






	@SuppressWarnings("deprecation")
	@Override
	public void onPause() {
		super.onPause();
		msg = " je pause";
		Log.d(TAG, msg);
		
		if (inPreview) {
		   // NOK Source Path Look Up mCamera.stopPreview();
		    inPreview = false;
		}
		releaseCameraAndPreview();	}

	private void releaseCameraAndPreview() {
		// ce cde crash
//	    mPreview.setCamera(null);
//	    if (mCamera != null) {
//	        mCamera.release();
//	        mCamera = null;
//	    }
		
		// cde de http://developer.android.com/training/camera/cameradirect.html
		 if (mCamera != null) {
		        // Call stopPreview() to stop updating the preview surface.
		        mCamera.stopPreview();
		    
		        // Important: Call release() to release the camera for use by other
		        // applications. Applications should release the camera immediately
		        // during onPause() and re-open() it during onResume()).
		        mCamera.release();
		    
		        mCamera = null;
		    }
	}

	private PictureCallback mPicture = new PictureCallback() {

		@Override
		public void onPictureTaken(byte[] data, Camera camera) {
			

			File pictureFile = getOutputMediaFile(MEDIA_TYPE_IMAGE);
			if (pictureFile == null){
				msg = "Error creating media file, check storage permissions: ";
				Log.d(TAG, msg );
				return;
			}

			try {
				FileOutputStream fos = new FileOutputStream(pictureFile);
				fos.write(data);
				fos.close();
				
			} catch (FileNotFoundException e) {
				Log.d(TAG, "File not found: " + e.getMessage());	
			} catch (IOException e) {
				Log.d(TAG, "Error accessing file: " + e.getMessage());
			}
			msg = " je onPictureTaken ";
			Log.d(TAG, msg);
			releaseCameraAndPreview();
			finish();  // !Important sinon pas de retour vers apctivity appelante
		}		
	};

	
	@SuppressWarnings({ "unused", "static-access" })
	private boolean safeCameraOpen(Camera c) {
	    boolean qOpened = false;
	  
	    try {
	        releaseCameraAndPreview();
	        mCamera = getCameraInstance().open();
	        qOpened = (mCamera != null);
	    } catch (Exception e) {
	        Log.e(getString(R.string.app_name), "failed to open Camera");
	        e.printStackTrace();
	    }

	    return qOpened;    
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
// FIXME
		//getMenuInflater().inflate(R.menu.camera, menu);

		return true;
	}

	/**
	 * Creates a media file in the {@code Environment.DIRECTORY_PICTURES} directory. The directory
	 * is persistent and available to other applications like gallery.
	 *
	 * @param type Media type. Can be video or image.
	 * @return A file object pointing to the newly created file.
	 */
	public  static File getOutputMediaFile(int type){
		// To be safe, you should check that the SDCard is mounted
		// using Environment.getExternalStorageState() before doing this.
		if (!Environment.getExternalStorageState().equalsIgnoreCase(Environment.MEDIA_MOUNTED)) {
			return  null;
		}

		// File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
		// 		Environment.DIRECTORY_PICTURES), "CameraSample");
				File mediaStorageDir = new File(CarteVitaleOCRActivity.DATA_PATH, "");
		// This location works best if you want the created images to be shared
		// between applications and persist after your app has been uninstalled.

		// Create the storage directory if it does not exist
		if (! mediaStorageDir.exists()){
			if (! mediaStorageDir.mkdirs()) {
				Log.d("CameraSample", "failed to create directory");
				return null;
			}
		}

		// Create a media file name
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
		File mediaFile;
		if (type == MEDIA_TYPE_IMAGE){
			mediaFile = new File(mediaStorageDir.getPath() + File.separator +

					CarteVitaleOCRActivity.FILE_NAME);
		} else if(type == MEDIA_TYPE_VIDEO) {
			mediaFile = new File(mediaStorageDir.getPath() + File.separator +
					"VID_"+ timeStamp + ".mp4");
		} else {
			return null;
		}

		return mediaFile;
	}


	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();

		// FIXME
		//if (id == R.id.action_settings) {
//			return true;
//
//		}
		return super.onOptionsItemSelected(item);
	}

	/** Check if this device has a camera */
	private boolean checkCameraHardware(Context context) {
		if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)){
			// this device has a camera
			return true;
		} else {
			// no camera on this device
			return false;
		}
	}

	/** A safe way to get an instance of the Camera object. */
	public static Camera getCameraInstance(){
		Camera c = null;
		try {
			msg = " camera exists on device ";
			c = Camera.open(); // attempt to get a Camera instance
			
			Log.d(tag, msg);
		}
		catch (Exception e){
			// Camera is not available (in use or does not exist)
			msg = " no camera or already in use ";
			Log.d(tag, msg);
		}
		return c; // returns null if camera is unavailable
	}


}// Fin de la class
