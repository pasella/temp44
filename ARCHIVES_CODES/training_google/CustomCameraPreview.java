//versus ggle ==> no crash no taf
package com.datumdroid.android.ocr.simple;

import java.io.IOException;
import java.util.List;

import android.content.Context;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.Size;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.ViewGroup;

public class CustomCameraPreview extends ViewGroup implements SurfaceHolder.Callback {
	private static final String TAG = "CustomCameraPreview";
	private SurfaceHolder mHolder;
	private Camera mCamera;
	  SurfaceView mSurfaceView;
	  private CustomCameraPreview mCustomCameraPreview;


	public CustomCameraPreview(Context context, Camera camera) {
		 super(context);

	        mSurfaceView = new SurfaceView(context);
	        
	       // FIXME  
	       addView(mSurfaceView);

	        // Install a SurfaceHolder.Callback so we get notified when the
	        // underlying surface is created and destroyed.
	        mHolder = mSurfaceView.getHolder();
	        mHolder.addCallback(this);
	        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

	}

	
	private boolean safeCameraOpen(int id) {
	    boolean qOpened = false;
	  
	    try {
	        releaseCameraAndPreview();
	        mCamera = Camera.open(id);
	        qOpened = (mCamera != null);
	    } catch (Exception e) {
	        Log.e(" Simple_OCR", "failed to open Camera");
	        e.printStackTrace();
	    }

	    return qOpened;    
	}

	private void releaseCameraAndPreview() {
		mCustomCameraPreview.setCamera(null);
	    if (mCamera != null) {
	        mCamera.release();
	        mCamera = null;
	    }
	}
	
	
	// http://developer.android.com/training/camera/cameradirect.html
	public void setCamera(Camera camera) {
		if (mCamera == camera) { return; }

		stopPreviewAndFreeCamera(camera);

		mCamera = camera;

		if (mCamera != null) {
			List<Size> localSizes = mCamera.getParameters().getSupportedPreviewSizes();
			List<Camera.Size>  mSupportedPreviewSizes = localSizes;
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
		String msg= ">>>> surfaceCreated ";
		Log.d(TAG, msg);
		Parameters parameters = null;
		
		msg= "<<<< surfaceCreated ";
		Log.d(TAG, msg);

	}

	public void surfaceDestroyed(SurfaceHolder holder) {
		// empty. Take care of releasing the Camera preview in your activity.
	}

	// http://developer.android.com/training/camera/cameradirect.html cf Modify Camera Settings
	public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
		String msg= ">>>> surfaceChanged ";
		Log.d(TAG, msg);
		 // Now that the size is known, set up the camera parameters and begin
	    // the preview.
	    Camera.Parameters parameters = mCamera.getParameters();
	    parameters.setPreviewSize(getWidth(), getHeight());
	    requestLayout();
	    mCamera.setParameters(parameters);

	    // Important: Call startPreview() to start updating the preview surface.
	    // Preview must be started before you can take a picture.
	    mCamera.startPreview();

		 msg= "<<<< surfaceChanged ";
		Log.d(TAG, msg);

	}


	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		// TODO Auto-generated method stub
		
	}
}