//versus ggle ==>
// MODIF TEST de git from asus + st3 camera_ocr vers github/camera_ocr
// Modif from st3 on temp44/../CameraPreview
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

public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback {
	 private static final String TAG = " CameraPreview ";
	private SurfaceHolder mHolder;
	    private Camera mCamera;
		private List<Size> mSupportedPreviewSizes;

	    public CameraPreview(Context context, Camera camera) {
	        super(context);
	        mCamera = camera;

	        // Install a SurfaceHolder.Callback so we get notified when the
	        // underlying surface is created and destroyed.
	        mHolder = getHolder();
	        mHolder.addCallback(this);
	        // deprecated setting, but required on Android versions prior to 3.0
	        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
	    }

	    public void surfaceCreated(SurfaceHolder holder) {
	        // The Surface has been created, now tell the camera where to draw the preview.
	    	String msg = " >>>> surfaceCreated ";
	    	  Log.d(TAG, msg);
	        try {
	        	setCamera(mCamera);
	            mCamera.setPreviewDisplay(holder);
	            mCamera.startPreview();
	        } catch (IOException e) {
	            Log.d(TAG, "Error setting camera preview: " + e.getMessage());
	        }
	        
	         msg = " <<<< surfaceCreated ";
	    	  Log.d(TAG, msg);
	    }

	    public void surfaceDestroyed(SurfaceHolder holder) {
	        // empty. Take care of releasing the Camera preview in your activity.
	    }

	    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
	        // If your preview can change or rotate, take care of those events here.
	        // Make sure to stop the preview before resizing or reformatting it.
	    	String msg = " >>>> surfaceChanged ";
	    	  Log.d(TAG, msg);

	        if (mHolder.getSurface() == null){
	          // preview surface does not exist
	          return;
	        }

	        // stop preview before making changes
	        try {
	            mCamera.stopPreview();
	            
	            
	            // Param du zoom
	            Parameters mParameters = mCamera.getParameters();
	            int value = 2;
	    		mParameters.setZoom(value);
	    		
	            requestLayout();
	            mCamera.setParameters(mParameters);
	            
	            String   params = mCamera.getParameters().flatten();
	            
	            Log.d(TAG, " params camera "+ params);
	            
	        } catch (Exception e){
	          // ignore: tried to stop a non-existent preview
	        }

	        // set preview size and make any resize, rotate or
	        // reformatting changes here

	        // start preview with new settings
	        try {
	            mCamera.setPreviewDisplay(mHolder);
	            mCamera.startPreview();

	        } catch (Exception e){
	            Log.d(TAG, "Error starting camera preview: " + e.getMessage());
	        }
	        
	         msg = " <<<< surfaceChanged ";
	    	  Log.d(TAG, msg);
	    }

		@Override
		protected void onLayout(boolean changed, int l, int t, int r, int b) {
			// TODO Auto-generated method stub
			
		}

		public void setCamera(Camera camera) {
			String msg = " >>>> setCamera ";
	    	  Log.d(TAG, msg);
   // if (mCamera == camera) { return; }
    
    //stopPreviewAndFreeCamera();
    
    mCamera = camera;
    
    if (mCamera != null) {
        List<Size> localSizes = mCamera.getParameters().getSupportedPreviewSizes();
        mSupportedPreviewSizes = localSizes;
        
        // Param du zoom
        Parameters mParameters = mCamera.getParameters();
        int value = 2;
		mParameters.setZoom(value);
		
		//Point de focus
		
		
		// Exposition lumiere
		
        requestLayout();
        mCamera.setParameters(mParameters);
      
        try {
            mCamera.setPreviewDisplay(mHolder);
        } catch (IOException e) {
            e.printStackTrace();
        }
      
        // Important: Call startPreview() to start updating the preview
        // surface. Preview must be started before you can take a picture.
        mCamera.startPreview();
    }
     msg = " <<<< setCamera ";
	  Log.d(TAG, msg);
}
		
		/**
		 * When this function returns, mCamera will be null.
		 */
		private void stopPreviewAndFreeCamera() {

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
		
}