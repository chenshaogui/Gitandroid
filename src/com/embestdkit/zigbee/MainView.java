package com.embestdkit.zigbee;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class MainView extends View/* implements OnTouchListener, OnGestureListener */{
	static final String TAG = "MainView";

	OnNodeClickListener mOnNodeClick;

	// GestureDetector mGestureDetector;

	public MainView(Context c) {
		super(c);
		// mGestureDetector = new GestureDetector(this);
		// setLongClickable(true);
	}

	public MainView(Context c, AttributeSet attr) {
		super(c, attr);
		// mGestureDetector = new GestureDetector(this);
		// setLongClickable(true);
	}

	public MainView(Context c, AttributeSet attr, int style) {
		super(c, attr, style);
		// mGestureDetector = new GestureDetector(this);
		// setLongClickable(true);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		int x = 0, y = 0;
		if (ZigBeeTool.sScreenHeight > Top.bm.getHeight()) {
			y = (ZigBeeTool.sScreenHeight - Top.bm.getHeight()) / 2;
		}
		if (ZigBeeTool.sScreenWidth > Top.bm.getWidth()) {
			x = (ZigBeeTool.sScreenWidth - Top.bm.getWidth()) / 2;
		}
		canvas.drawBitmap(Top.bm, x, y, null);
		// 在这里实现我们自己的图像，由xml转换过来

		// 加载xml布局文件
		// LayoutInflater factory =
		// LayoutInflater.from(ZigBeeTool.getInstance());
		// View view = factory.inflate(R.layout.main, null);
		//
		// //调用下面这个方法非常重要，如果没有调用这个方法，得到的bitmap为null
		// view.measure(MeasureSpec.makeMeasureSpec(256, MeasureSpec.EXACTLY),
		// MeasureSpec.makeMeasureSpec(256, MeasureSpec.EXACTLY));
		// //这个方法也非常重要，设置布局的尺寸和位置
		// view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
		// //生成bitmap
		// Bitmap bitmap = Bitmap.createBitmap(view.getWidth(),
		// view.getHeight(),
		// Bitmap.Config.RGB_565);
		// //利用bitmap生成画布
		// // Canvas canvas = new Canvas(bitmap);
		// //把view中的内容绘制在画布上
		// view.draw(canvas);
		// canvas.drawBitmap(bitmap, x, y, null);

	}

	/*
	 * public boolean onDown(MotionEvent arg0) { // TODO Auto-generated method
	 * stub Log.d(TAG, "on down"); return false; } public boolean
	 * onFling(MotionEvent arg0, MotionEvent arg1, float arg2, float arg3) { //
	 * TODO Auto-generated method stub Log.d(TAG, "fling ...."); return false; }
	 * public void onLongPress(MotionEvent arg0) { // TODO Auto-generated method
	 * stub Log.d(TAG, "long press."); } public boolean onScroll(MotionEvent
	 * arg0, MotionEvent arg1, float arg2, float arg3) { // TODO Auto-generated
	 * method stub Log.d(TAG, "scroll"); return false; } public void
	 * onShowPress(MotionEvent arg0) { // TODO Auto-generated method stub
	 * Log.d(TAG, "show press"); } public boolean onSingleTapUp(MotionEvent
	 * arg0) { // TODO Auto-generated method stub Log.d(TAG, "click"); return
	 * false; }
	 * 
	 * public boolean onTouch(View v, MotionEvent event) { // TODO
	 * Auto-generated method stub return mGestureDetector.onTouchEvent(event); }
	 */
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		int x = 0, y = 0;
		if (ZigBeeTool.sScreenHeight > Top.bm.getHeight()) {
			y = (ZigBeeTool.sScreenHeight - Top.bm.getHeight()) / 2;
		}
		if (ZigBeeTool.sScreenWidth > Top.bm.getWidth()) {
			x = (ZigBeeTool.sScreenWidth - Top.bm.getWidth()) / 2;
		}

		Log.d(TAG, "onTouchEvent...." + ev.getAction());
		// if (ev.getAction() == ev.ACTION_UP) {
		Log.d(TAG, "click x:" + ev.getX() + " y:" + ev.getY());
		// }
		Node n = Top.findClick(Top.tree, ev.getX() - x, ev.getY() - y);
		if (n != null) {
			Log.d(TAG, "click : " + n);
			if (mOnNodeClick != null) {
				mOnNodeClick.onNodeClick(n);
			}
		} else
			Log.d(TAG, "not node click.");

		return false;
	}

	void setOnNodeClickListener(OnNodeClickListener li) {
		mOnNodeClick = li;
	}

	interface OnNodeClickListener {
		/**
		 * 触摸操作，显示上一页，下一页
		 * 
		 * @param n
		 */
		void onNodeClick(Node n);
	}
}
