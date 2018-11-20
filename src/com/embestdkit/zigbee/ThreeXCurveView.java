package com.embestdkit.zigbee;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * 自定义湿度波形图
 * 
 * @author Administrator
 * 
 */
public class ThreeXCurveView extends View {
	static String TAG = "HumidityCurveView";

	public ThreeXCurveView(Context context) {
		super(context);

	}

	public ThreeXCurveView(Context c, AttributeSet attr) {
		super(c, attr);

	}

	/**
	 * 湿度波形图
	 * 
	 * @param c
	 * @param attr
	 * @param style
	 */
	public ThreeXCurveView(Context c, AttributeSet attr, int style) {
		super(c, attr, style);
	}

	static final int XP = 30;
	static final int YP = 10;

	static final int MAX_DATA_CNT = 20;
	static final int MAX_DATA_CNTy = 20;
	static final int MAX_DATA_CNTz = 20;

	int mXLength, mXStart;
	int mYLength, mYStart;

	byte mBaseValue = 0;
	byte mBaseValuey = 0;
	byte mBaseValuez = 0;

	byte[] mData = new byte[MAX_DATA_CNT];
	byte[] mDatay = new byte[MAX_DATA_CNTy];
	byte[] mDataz = new byte[MAX_DATA_CNTz];
	int mDataCnt = 0;
	int mDataCnty = 0;
	int mDataCntz = 0;

	// void addData(byte v,byte vy) {
	public void addData(byte v, byte vy, byte vz) {
		// v = 40;
		// vy= 70;
		if (v <= 0)
			v = 0;
		if (v > 100)
			v = 100;
		if (vy <= 0)
			vy = 0;
		if (vy > 100)
			vy = 100;
		if (vz <= 0)
			vz = 0;
		if (vz > 100)
			vz = 100;

		Log.d(TAG, "temp data:" + v);

		if (mDataCnt == 0)
			mBaseValue = v;
		if (mDataCnty == 0)
			mBaseValuey = vy;
		if (mDataCntz == 0)
			mBaseValuez = vz;

		for (int i = 1; i < MAX_DATA_CNT; i++) {
			mData[i - 1] = mData[i];
		}
		for (int j = 1; j < MAX_DATA_CNTy; j++) {
			mDatay[j - 1] = mDatay[j];
		}
		for (int j = 1; j < MAX_DATA_CNTz; j++) {
			mDataz[j - 1] = mDataz[j];
		}
		mData[MAX_DATA_CNT - 1] = v;
		if (mDataCnt < MAX_DATA_CNT)
			mDataCnt++;

		mDatay[MAX_DATA_CNTy - 1] = vy;
		if (mDataCnty < MAX_DATA_CNTy)
			mDataCnty++;

		mDataz[MAX_DATA_CNTz - 1] = vz;
		if (mDataCntz < MAX_DATA_CNTz)
			mDataCntz++;

		this.invalidate();
	}

	void drawCurve(Canvas c) {
		int p = mYLength / 5;
		int up = p / 10;
		int sx, sy, ex, ey;

		if (mDataCnt <= 1)
			return;
		Paint paint = new Paint();
		paint.setColor(Color.RED);
		sx = mXStart;
		sy = mYStart + mYLength / YP * 3;
		float pa[] = new float[mDataCnt * 2];
		for (int i = 0; i < mDataCnt; i++) {
			pa[2 * i] = sx + i * mXLength / 10;
			pa[2 * i + 1] = sy - mData[i];
		}
		for (int i = mDataCnt - 1; i > 0; i--) {
			c.drawLine(pa[2 * i], pa[2 * i + 1], pa[2 * (i - 1)],
					pa[2 * (i - 1) + 1], paint);
		}

	}

	void drawCross(Canvas canvas) {
		int w = getWidth();
		int h = getHeight();

		Paint paint = new Paint();

		int spaceWidth = w * 5 / 100;
		int textWidth = w * 23 / 100;
		int curveWidth = w - spaceWidth * 2 - textWidth;
		int sh = h * 8 / 100;
		int ph = h - sh * 2;

		int cw = curveWidth * 3 / 100;
		int cs = cw + cw / 2;
		int cv = curveWidth - cw - cs;

		mXStart = spaceWidth + textWidth + cv;
		mYStart = sh + ph / 2;

		int dx = cv / (MAX_DATA_CNT - 1);
		int dy = ph / 20;

		mBaseValue = 50;
		mBaseValuey = 50;
		mBaseValuez = 50;

		/* 文子显示矩形框 */
		paint.setStrokeWidth(2);
		paint.setTextAlign(Align.CENTER);
		paint.setStyle(Style.STROKE);
		paint.setColor(Color.RED);
		int textw = textWidth - textWidth * 3 / 20;
		// canvas.drawRoundRect(new RectF(spaceWidth, sh,
		// spaceWidth+textw, sh+ph), 3, 3, paint);

		paint.setColor(Color.YELLOW);
		paint.setTextSize(50);
		paint.setStrokeWidth(1);
		paint.setStyle(Style.FILL_AND_STROKE);
		paint.setColor(Color.GREEN);
		// canvas.drawText("当前X值", spaceWidth+textw/2, sh+ph/2-20, paint);
		canvas.drawText("当前X值:", spaceWidth + textw / 2 + 300, sh + ph / 2 - 70
				- 20 - 10, paint);
		if (mDataCnt > 0) {
			// String str = String.format("%d%", mData[MAX_DATA_CNT-1]);
			String str = String.format("%d", mData[MAX_DATA_CNT - 1]);
			String string = str + "fg";
			// canvas.drawText(String.format("%d%%", mData[MAX_DATA_CNT-1]),
			// spaceWidth+textw/2, sh+ph/2+20, paint);
			// canvas.drawText(string, spaceWidth+textw/2, sh+ph/2+20, paint);
			canvas.drawText(string, spaceWidth + textw / 2 + 300, sh + ph / 2
					- 30 + 10 - 20 - 10, paint);
		}
		paint.setColor(Color.YELLOW);
		// canvas.drawText("当前Y值", spaceWidth+textw/2, sh+ph/2+50, paint);
		canvas.drawText("当前Y值:", spaceWidth + textw / 2 + 300,
				sh + ph / 2 + 10, paint);
		if (mDataCnty > 0) {
			String str = String.format("%d", mDatay[MAX_DATA_CNTy - 1]);
			String string = str + "fg";
			// canvas.drawText(String.format("%d%%", mData[MAX_DATA_CNT-1]),
			// spaceWidth+textw/2, sh+ph/2+20, paint);
			// canvas.drawText(string, spaceWidth+textw/2, sh+ph/2+90, paint);
			canvas.drawText(string, spaceWidth + textw / 2 + 300, sh + ph / 2
					+ 50 + 10, paint);

		}
		paint.setColor(Color.CYAN);
		// canvas.drawText("当前Z值", spaceWidth+textw/2, sh+ph/2+120, paint);
		canvas.drawText("当前Z值:", spaceWidth + textw / 2 + 300, sh + ph / 2 + 90
				+ 20 + 10, paint);
		if (mDataCntz > 0) {
			String str = String.format("%d", mDataz[MAX_DATA_CNTz - 1]);
			String string = str + "fg";
			// canvas.drawText(String.format("%d%%", mData[MAX_DATA_CNT-1]),
			// spaceWidth+textw/2, sh+ph/2+20, paint);
			// canvas.drawText(string, spaceWidth+textw/2, sh+ph/2+160, paint);
			canvas.drawText(string, spaceWidth + textw / 2 + 300, sh + ph / 2
					+ 130 + 10 + 20 + 10, paint);

		}
		/* 曲线矩形框 */
		paint.setStrokeWidth(2);
		paint.setStyle(Style.STROKE);
		paint.setColor(Color.RED);

		// canvas.drawRoundRect(new RectF(spaceWidth+textWidth, sh,
		// spaceWidth+textWidth+cv+cw/2, sh+ph), 3, 3, paint);

		paint.setStrokeWidth(1);
		for (int i = 1; i <= 10; i++) { // 上半部刻度
			int lw = (cw * 8 / 10);
			if (i % 2 != 0)
				lw /= 2;
			// canvas.drawLine(mXStart+cw, mYStart-i*dy,
			// mXStart+cw+lw, mYStart-i*dy, paint);
		}
		for (int i = 1; i <= 10; i++) {
			int lw = (cw * 8 / 10);
			if (i % 2 != 0)
				lw /= 2;
			// canvas.drawLine(mXStart+cw, mYStart+i*dy,
			// mXStart+cw+lw, mYStart+i*dy, paint);
		}

		paint.setStrokeWidth(2);
		// canvas.drawLine(mXStart+cw, mYStart, mXStart+cw+cw, mYStart, paint);
		// //中心刻度线
		// if (mDataCnt > 0) { //刻度数字
		paint.setStrokeWidth(1);
		paint.setStyle(Style.FILL_AND_STROKE);
		paint.setTextSize(10);
		// canvas.drawText(String.format("%d", 50), mXStart+cw*2+2, mYStart+8,
		// paint);
		//
		// }

		paint.setStrokeWidth(2);
		paint.setColor(Color.BLUE);
		// canvas.drawLine(spaceWidth+textWidth, mYStart,
		// spaceWidth+textWidth+cv, mYStart, paint);

		int x1 = mXStart, y1 = mYStart - (mData[MAX_DATA_CNT - 1] - mBaseValue)
				* dy / 5;
		int x2, y2;
		int x1y = mXStart, y1y = mYStart
				- (mDatay[MAX_DATA_CNTy - 1] - mBaseValuey) * dy / 5;
		int x2y, y2y;
		int x1z = mXStart, y1z = mYStart
				- (mDataz[MAX_DATA_CNTz - 1] - mBaseValuez) * dy / 5;
		int x2z, y2z;
		paint.setStrokeWidth(3);
		paint.setColor(Color.GREEN);
		for (int i = 1; i < mDataCnt; i++) {
			// for (int i=1; i<10; i++) {
			x2 = mXStart - i * dx;
			y2 = mYStart - (mData[MAX_DATA_CNT - i - 1] - mBaseValue) * dy / 5;
			paint.setColor(Color.GREEN);
			// canvas.drawLine(x1,y1, x2, y2, paint);
			x1 = x2;
			y1 = y2;

			x2y = mXStart - i * dx;
			y2y = mYStart - (mDatay[MAX_DATA_CNTy - i - 1] - mBaseValuey) * dy
					/ 5;
			paint.setColor(Color.YELLOW);
			// canvas.drawLine(x1y,y1y, x2y, y2y, paint);
			x1y = x2y;
			y1y = y2y;

			x2z = mXStart - i * dx;
			y2z = mYStart - (mDataz[MAX_DATA_CNTz - i - 1] - mBaseValuez) * dy
					/ 5;
			paint.setColor(Color.CYAN);
			// canvas.drawLine(x1z,y1z, x2z, y2z, paint);
			x1z = x2z;
			y1z = y2z;
		}
		// paint.setStrokeWidth(3);
		// paint.setColor(Color.YELLOW);
		// for (int j=1; j<mDataCnty; j++) {
		// x2y = mXStart - j * dx;
		// y2y = mYStart - (mDatay[MAX_DATA_CNTy-j-1] - mBaseValuey)*dy / 5;
		// canvas.drawLine(x1y,y1y, x2y, y2y, paint);
		// x1y = x2y; y1y = y2y;
		// }
		LinearGradient mLinearGradient = new LinearGradient(0, 0, 20, 500,
				new int[] { Color.RED, Color.BLUE, }, null,
				Shader.TileMode.REPEAT);
		paint.setShader(mLinearGradient);
		paint.setStyle(Style.FILL);
		RectF post = new RectF(spaceWidth + textWidth + cv, sh, spaceWidth
				+ textWidth + cv + cw, sh + ph);
		// canvas.drawRoundRect(post, 5, 5, paint);
		paint.clearShadowLayer();

	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		// 首先定义一个paint
		Paint paint = new Paint();
		// 绘制矩形区域-实心矩形
		// 设置颜色
		paint.setColor(Color.TRANSPARENT);
		canvas.drawRect(new Rect(0, 0, getWidth(), getHeight()), paint);
		drawCross(canvas);
		// drawCurve(canvas);
	}

}
