zpackage com.datumdroid.android.ocr.simple;
//see http://stackoverflow.com/questions/7942378/android-camera-will-not-work-startpreview-fails

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Rect;
import android.hardware.Camera;
import android.hardware.Camera.Area;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.Size;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class CustomCameraPreview extends SurfaceView implements SurfaceHolder.Callback {
	private static final String TAG = "CustomCameraPreview";
	private SurfaceHolder mHolder;
	private Camera mCamera;

	public CustomCameraPreview(Context context, Camera camera) {
		super(context);
		mCamera = camera;

		// Install a SurfaceHolder.Callback so we get notified when the
		// underlying surface is created and destroyed.
		mHolder = getHolder();
		mHolder.addCallback(this);
		// deprecated setting, but required on Android versions prior to 3.0
		mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

		String msg = " version parametrage preview dans oncreated ";
		Log.d(TAG, msg);

	}

	// http://developer.android.com/training/camera/cameradirect.html
	public void setCamera(Camera camera) {
		if (mCamera == camera) { return; }

		stopPreviewAndFreeCamera(camera);

		mCamera = camera;

		if (mCamera != null) {
//			List<Size> localSizes = mCamera.getParameters().getSupportedPreviewSizes();
//			List<Camera.Size>  mSupportedPreviewSizes = localSizes;
			requestLayout();

			try {
				mCamera.setPreviewDisplay(mHolder);
			} catch (IOException e) {
				e.printStackTrace();
			}

			// Important: Call startPreview() to start updating the preview
			// surface. Preview must be started before you can take a picture.
			mCamera.startPreview();
		}
	}

	private void stopPreviewAndFreeCamera(Camera camera) {

		if (camera != null) {
			/*
               Call stopPreview() to stop updating the preview surface.
			 */
			camera.stopPreview();

			/*
               Important: Call release() to release the camera for use by other applications. 
               Applications should release the camera immediately in onPause() (and re-open() it in
               onResume()).
			 */
			//           camera.release();

			camera = null;
		}
	}

	@SuppressWarnings("deprecation")
	public void surfaceCreated(SurfaceHolder holder) {
		// The Surface has been created, now tell the camera where to draw the preview.
		// METTRE EN PLACE les Ã©lÃ©ments pour favoriser la prise de vue du numss
		// 	Verifier prÃ©sence holder


		String msg= ">>>> surfaceCreated ";
		Log.d(TAG, msg);
		if (mHolder.getSurface() == null){
			// preview surface does not exist
			return;
		}

		Parameters parameters = null;
		try {
			// mCamera = Camera.open();	INUTILE CAR DEJA FAIT
			// Mise en pace du zoom ad hoc pour optimiser la prise de vue de la CV
			// Zoom dans le preview
			// FIXME ne fait pas le taf

			if (mCamera != null) { // Camera DISPO

				boolean inPreview = true;
				parameters = mCamera.getParameters();
				Thread th = new Thread();
				try {
					th.sleep(2000);
					mCamera.stopPreview();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				int mZoom =parameters.getMaxZoom();
				Integer i_mZoom = Integer.valueOf(mZoom);

				msg = " zoom max vaut : " + i_mZoom.toString();
				Log.d(TAG, msg );

				// supprimer mode autofocus, pour forcer mon zoom idéalement sur une zone donnée: (Le centre de la scene, ou en fontion d'un motif particulier de la scene que la caméra visualise.
				// 	http://stackoverflow.com/questions/18712785/samsung-s4-zoom-does-not-support-smooth-zoom-issmoothzoomsupported-false
				//	http://stackoverflow.com/questions/19577299/android-camera-preview-stretched
				String valueM = "FOCUS_MODE_CONTINUOUS_PICTURE";
				// NOK crash parameters.setFocusMode(valueM);
				int maxFcsAr =parameters.getMaxNumFocusAreas();
				int maxMtrAr = parameters.getMaxNumMeteringAreas();


				//choix de la zone centrale	http://developer.android.com/reference/android/hardware/Camera.Area.html
				List<Area> focusAreas = new ArrayList<Camera.Area>();


				Rect rect = new Rect(getLeft(), getTop(), getRight(), getBottom());
				int weight = 1000;
				Area focusCenter =  new Area(rect, weight);
				focusAreas.add(focusCenter);

				//parameters.setFocusAreas(focusAreas);
				int value = 1;
				if (parameters.isSmoothZoomSupported()) {	// smooth OK
					mCamera.stopPreview();
					//parameters.setZoom(6);
					mCamera.setParameters(parameters);
					mCamera.startSmoothZoom(value);
					mCamera.startPreview();
					msg = " smmothZoom OK supporte ";
					Log.d(TAG, msg);
				} else {	// smooth NOK
					msg = " smmothZoom NOK supporte ";
					Log.d(TAG, msg);
					if (parameters.isZoomSupported()) {
						int zoomCurrent = parameters.getZoom();
						msg = " zoom courant vaut : "+ zoomCurrent;	//cast implicit ?!
						Log.d(TAG, msg);
						//parameters.setZoom(value);
						msg = " Zoom OK supporte ";
						Log.d(TAG, msg);
					} else {
						msg = " Zoom NOK supporte ";
						Log.d(TAG, msg);
					}
				}
			}

			// Affichage du preview
			String info_cam_params =parameters.flatten();			
			Log.d(TAG, " params camera = "+info_cam_params);
			mCamera.setParameters(parameters);
			mCamera.setPreviewDisplay(holder);
			mCamera.startPreview();
		} catch (IOException e) {
			Log.d(TAG, "Error setting camera preview: " + e.getMessage());
		}

		String all_params = parameters.flatten();
		msg = " all parametres de la camera ";
		Log.d(TAG, msg + all_params);
		requestLayout();

		msg = " <<<< surfaceCreated ";
		Log.d(TAG, msg);
	}

	public void surfaceDestroyed(SurfaceHolder holder) {
		// empty. Take care of releasing the Camera preview in your activity.

		String msg = " >>>> surfaceDestroyed ";
		Log.d(TAG, msg);


		msg = " <<<< surfaceDestroyed ";
		Log.d(TAG, msg);

	}

	// http://developer.android.com/training/camera/cameradirect.html cf Modify Camera Settings
	public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
		// If your preview can change or rotate, take care of those events here.
		// Make sure to stop the preview before resizing or reformatting it.
		String msg = ">>>> surfaceChanged ";
		Log.d(TAG, msg);
		if (mHolder.getSurface() == null){
			// preview surface does not exist
			return;
		}


		// set preview size and make any resize, rotate or
		// reformatting changes here



		// start preview with new settings
		Parameters parameters = mCamera.getParameters();

		try {
			if (parameters != null){
				mCamera.stopPreview();

				// Pour tester la maj du preview
				//holder.setFixedSize(getWidth()/2, getHeight()/4);
				// parameters.setPreviewSize(getWidth()/4, getHeight()/2);
				// mCamera.setParameters(parameters);
				String all_params = parameters.flatten();
				String msg = " all parametres de la camera ";
				Log.d(TAG, msg + all_params);
				requestLayout();

				mCamera.startPreview();
			}
			String msg = " L198 surfaceChanged ==> start preview with new settings ";
			Log.d(TAG, msg );

		} catch (Exception e){
			Log.d(TAG, "Error starting camera preview: " + e.getMessage());
		}
		msg = "<<<< surfaceChanged ";
		Log.d(TAG, msg);
	}


}// FIN CLASS