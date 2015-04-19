package com.datumdroid.android.ocr.simple;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.graphics.drawable.shapes.RectShape;
import android.util.AttributeSet;
import android.view.View;

public class CustomDrawableView extends View{

	protected ShapeDrawable mDrawable;

	public CustomDrawableView(Context context, AttributeSet as) {
		super(context);
		int x,y,height,width;
		if (CarteVitaleOCRActivity.sceneCv) {
			x = 840;
			y = 500;
			width = 350;
			height = 40;
			
			mDrawable = new ShapeDrawable(new RectShape());
			mDrawable.getPaint().setColor(0x33FF7F2A);
			mDrawable.setBounds(x, y,  x+width, y+ height);
		} else {
			x = 750;
			y = 400;
			width = 400;
			height = 150;
			
			mDrawable = new ShapeDrawable(new RectShape());
			mDrawable.getPaint().setColor(0x3355FFFF);
			mDrawable.setBounds(x, y,  x+width, y+ height);
		}


		
	}

	public ShapeDrawable getmDrawable() {
		return mDrawable;
	}

	public void setmDrawable(ShapeDrawable mDrawable) {
		this.mDrawable = mDrawable;
	}

	public void onDraw(Canvas c) {
		mDrawable.draw(c);
	}

	public static int getX(CustomDrawableView mCustomDrawableView) {
		// TODO Auto-generated method stub
		int x =22;
		x = mCustomDrawableView.mDrawable.getBounds().left;
		return x;
	}

	public static int getY(CustomDrawableView mCustDrawView) {
		// TODO Auto-generated method stub
		int y =33;
		y = mCustDrawView.mDrawable.getBounds().top;
		return y;
	}

	public static int getH(CustomDrawableView mCustomDrawableView) {
		// TODO Auto-generated method stub
		int h =44;
		h = mCustomDrawableView.mDrawable.getBounds().height();
		return h;
	}

	public static int getW(CustomDrawableView mCustomDrawableView) {
		// TODO Auto-generated method stub
		int w =55;
		w = mCustomDrawableView.mDrawable.getBounds().width();
		return w;
	}


}
