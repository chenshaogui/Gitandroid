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
		// ������ʵ�������Լ���ͼ����xmlת������

		// ����xml�����ļ�
		// LayoutInflater factory =
		// LayoutInflater.from(ZigBeeTool.getInstance());
		// View view = factory.inflate(R.layout.main, null);
		//
		// //����������������ǳ���Ҫ�����û�е�������������õ���bitmapΪnull
		// view.measure(MeasureSpec.makeMeasureSpec(256, MeasureSpec.EXACTLY),
		// MeasureSpec.makeMeasureSpec(256, MeasureSpec.EXACTLY));
		// //�������Ҳ�ǳ���Ҫ�����ò��ֵĳߴ��λ��
		// view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
		// //����bitmap
		// Bitmap bitmap = Bitmap.createBitmap(view.getWidth(),
		// view.getHeight(),
		// Bitmap.Config.RGB_565);
		// //����bitmap���ɻ���
		// // Canvas canvas = new Canvas(bitmap);
		// //��view�е����ݻ����ڻ�����
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
		 * ������������ʾ��һҳ����һҳ
		 * 
		 * @param n
		 */
		void onNodeClick(Node n);
	}
}
