package com.datumdroid.android.ocr.simple;

import java.io.IOException;
import java.util.List;

import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.hardware.Camera.Size;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Display;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;

public class CustomCameraPreview extends ViewGroup implements SurfaceHolder.Callback
{
	private Size mPreviewSize;
	private List<Size> mSupportedPreviewSizes;        
	private Context mContext;
	private SurfaceView mSurfaceView;
	private SurfaceHolder mHolder;
	private final static String TAG = "CustomCameraPreview";
	private Camera mCamera = null;
	private List<String> mSupportedFlashModes;
	protected CustomCameraPreview mPreview;

//	public CustomCameraPreview(Context context)
//	{
//		super(context);
//		mContext = context;
//		
//		if (checkCameraHardware(getContext())) {
//			// Create an instance of Camera
//			
//			mCamera = getCameraInstance();
//		}
//	// nk 	mCamera = Camera.open();        
//		
//		mCamera.open();
//		setCamera(mCamera);
//
//		mSurfaceView = new SurfaceView(context);
//		addView(mSurfaceView, 0);        
//		
//		mHolder = mSurfaceView.getHolder();
//		mHolder.addCallback(this);
//		mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
//		mHolder.setKeepScreenOn(true);
//	}

	// cnsruceur den mn archive
	public CustomCameraPreview(Context context, Camera camera) {
		super(context);
		mCamera = camera;
		
		// Create our Preview view and set it as the content of our activity.
		setCamera(mCamera);
//		FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
		
		  mSurfaceView = new SurfaceView(context);
	        //addView(mSurfaceView);
		 

		//Parametrer la camera	http://developer.android.com/reference/android/hardware/Camera.Parameters.html

		// Install a SurfaceHolder.Callback so we get notified when the
		// underlying surface is created and destroyed.
		mHolder = mSurfaceView.getHolder();
		mHolder.addCallback(this);
		// deprecated setting, but required on Android versions prior to 3.0
		mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		

		

		String msg = " version parametrage preview dans oncreated ";
		Log.d(TAG, msg);

	}
	
	
	/** A safe way to get an instance of the Camera object. */
	public static Camera getCameraInstance(){
		Camera c = null;
		try {
		String	msg = " camera exists on device ";
			c = Camera.open(); // attempt to get a Camera instance
			Log.d(TAG, msg);
		}
		catch (Exception e){
			// Camera is not available (in use or does not exist)
		String	msg = " no camera or already in use ";
			Log.d(TAG, msg);
		}
		return c; // returns null if camera is unavailable
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

	public CustomCameraPreview(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		mContext = context;            
	}

	public void setSupportedPreviewSizes(List<Size> supportedPreviewSizes)
	{
		mSupportedPreviewSizes = supportedPreviewSizes;
	}

	public Size getPreviewSize()
	{
		return mPreviewSize;
	}

	public void setCamera2(Camera camera)
	{
		mCamera = camera;
		if (mCamera != null)
		{
			mSupportedPreviewSizes = mCamera.getParameters().getSupportedPreviewSizes();                
			mSupportedFlashModes = mCamera.getParameters().getSupportedFlashModes();
			// Set the camera to Auto Flash mode.
			if (mSupportedFlashModes.contains(Camera.Parameters.FLASH_MODE_AUTO))
			{
				Camera.Parameters parameters = mCamera.getParameters();
				parameters.setFlashMode(Camera.Parameters.FLASH_MODE_AUTO);
				//Le ziim
				
				int value = 2;
				parameters.setZoom(value);
				
				
				mCamera.setParameters(parameters);
			}                   
		}
		requestLayout();
	}

	//ggle
	public void setCamera(Camera camera) {
	  //  if (mCamera == camera) { return; }
	    
	  //  stopPreviewAndFreeCamera();
	    
	    mCamera = camera;
	    
	    if (mCamera != null) {
	    	String paramsCam = mCamera.getParameters().flatten();
	    	
	    	
	    	String msg = " params camera ";
			Log.d("uu", msg);
	        List<Size> localSizes = mCamera.getParameters().getSupportedPreviewSizes();
	        mSupportedPreviewSizes = localSizes;
	        msg = localSizes.get(0).toString();
	        Log.d(TAG, msg);
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
	
	
	//ggle
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
	
	
	@Override
	public void surfaceDestroyed(SurfaceHolder holder)
	{
		// Surface will be destroyed when we return, so stop the preview.
		if (mCamera != null)
		{
			mCamera.stopPreview();
		}
	}

	public void surfaceChanged2(SurfaceHolder holder, int format, int width, int height)
	{
		// Now that the size is known, set up the camera parameters and begin
		// the preview.
		if (mCamera != null)
		{
			Camera.Parameters parameters = mCamera.getParameters();        
			Size previewSize = getPreviewSize();
			parameters.setPreviewSize(previewSize.width, previewSize.height);                

			mCamera.setParameters(parameters);
			mCamera.startPreview();
		}

	}

	//ggle
	public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
	    // Now that the size is known, set up the camera parameters and begin
	    // the preview.
	    Camera.Parameters parameters = mCamera.getParameters();
	    parameters.setPreviewSize(mPreviewSize.width, mPreviewSize.height);
	    requestLayout();
	    mCamera.setParameters(parameters);

	    // Important: Call startPreview() to start updating the preview surface.
	    // Preview must be started before you can take a picture.
	    mCamera.startPreview();
	}
	
	@Override
	public void surfaceCreated(SurfaceHolder holder)
	{
		// The Surface has been created, acquire the camera and tell it where
		// to draw.
		try
		{
			if (mCamera != null)
			{
				mCamera.setPreviewDisplay(holder);
			}
		}
		catch (IOException exception)
		{
			Log.e(TAG, "IOException caused by setPreviewDisplay()", exception);
		}
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
	{        
		final int width = resolveSize(getSuggestedMinimumWidth(), widthMeasureSpec);
		final int height = resolveSize(getSuggestedMinimumHeight(), heightMeasureSpec);
		setMeasuredDimension(width, height);

		if (mSupportedPreviewSizes != null)
		{
			mPreviewSize = getOptimalPreviewSize(mSupportedPreviewSizes, width, height);
		}
	}

	@Override
	protected void onLayout(boolean changed, int left, int top, int right, int bottom)
	{
		if (changed)
		{                            
			//final View cameraView = getChildAt(0);          
			final View cameraView = mSurfaceView;
			
			final int width = right - left;
			final int height = bottom - top;

			int previewWidth = width;
			int previewHeight = height;
			if (mPreviewSize != null)
			{
				Display display = ((WindowManager)mContext.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();

				switch (display.getRotation())
				{
				case Surface.ROTATION_0:
					previewWidth = mPreviewSize.height;
					previewHeight = mPreviewSize.width;
					mCamera.setDisplayOrientation(90);
					break;
				case Surface.ROTATION_90:
					previewWidth = mPreviewSize.width;
					previewHeight = mPreviewSize.height;
					break;
				case Surface.ROTATION_180:
					previewWidth = mPreviewSize.height;
					previewHeight = mPreviewSize.width;
					break;
				case Surface.ROTATION_270:
					previewWidth = mPreviewSize.width;
					previewHeight = mPreviewSize.height;
					mCamera.setDisplayOrientation(180);
					break;
				}                                    
			}

			final int scaledChildHeight = previewHeight * width / previewWidth;

			cameraView.layout(0, height - scaledChildHeight, width, height);

		}
	}


	private Size getOptimalPreviewSize(List<Size> sizes, int width, int height)
	{           
		Size optimalSize = null;                                

		final double ASPECT_TOLERANCE = 0.1;
		double targetRatio = (double) height / width;

		// Try to find a size match which suits the whole screen minus the menu on the left.
		for (Size size : sizes)
		{
			if (size.height != width) continue;
			double ratio = (double) size.width / size.height;
			if (ratio <= targetRatio + ASPECT_TOLERANCE && ratio >= targetRatio - ASPECT_TOLERANCE)
			{
				optimalSize = size;
			}               
		}

		// If we cannot find the one that matches the aspect ratio, ignore the requirement.
		if (optimalSize == null)
		{
			// TODO : Backup in case we don't get a size.
		}

		return optimalSize;
	}

	public void previewCamera()
	{        
		try 
		{           
			mCamera.setPreviewDisplay(mHolder);         
			mCamera.startPreview();                 
		}
		catch(Exception e)
		{
			Log.d(TAG, "Cannot start preview.", e);    
		}
	}


	/*public void onPreviewFrame(byte[] data, Camera arg1) { 
    Log.d("CameraSurfaceView", "PREVIEW FRAME:"); 
    byte[] pixels = new byte[use_size.width * use_size.height * 3]; ; 
    decodeYUV420SP(pixels, data, use_size.width,  use_size.height);  
    renderer.bindCameraTexture(pixels, use_size.width,  use_size.height); 
}*/ 

	void decodeYUV420SP(byte[] rgb, byte[] yuv420sp, int width, int height) {   

		final int frameSize = width * height;   

		for (int j = 0, yp = 0; j < height; j++) {        
			int uvp = frameSize + (j >> 1) * width, u = 0, v = 0;   
			for (int i = 0; i < width; i++, yp++) {   
				int y = (0xff & ((int) yuv420sp[yp])) - 16;   
				if (y < 0){   
					y = 0;  
				} 
				if ((i & 1) == 0) {   
					v = (0xff & yuv420sp[uvp++]) - 128;   
					u = (0xff & yuv420sp[uvp++]) - 128;   
				}   

				int y1192 = 1192 * y;   
				int r = (y1192 + 1634 * v);   
				int g = (y1192 - 833 * v - 400 * u);   
				int b = (y1192 + 2066 * u);   

				if (r < 0){ 
					r = 0;                
				}else if (r > 262143){   
					r = 262143;  
				} 
				if (g < 0){                   
					g = 0;                
				}else if (g > 262143){ 
					g = 262143;  
				} 
				if (b < 0){                   
					b = 0;                
				}else if (b > 262143){ 
					b = 262143;  
				} 
				rgb[yp*3] = (byte) (b << 6); 
				rgb[yp*3 + 1] = (byte) (b >> 2); 
				rgb[yp*3 + 2] = (byte) (b >> 10); 
			}   
		}   
	}
	
	// ********** add oliver
	
	
	
	// **********
	
	
}// fin class