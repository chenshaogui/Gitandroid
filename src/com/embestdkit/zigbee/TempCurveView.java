package com.embestdkit.zigbee;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Rect;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

public class TempCurveView extends View {
	static String TAG = "TempCurveView";

	// Bitmap mBmp = Bitmap.createBitmap(500, 100, Bitmap.Config.ARGB_4444);
	public TempCurveView(Context context) {
		super(context);

	}

	public TempCurveView(Context c, AttributeSet attr) {
		super(c, attr);

	}

	/**
	 * 自定义的视图来 采集波形
	 * 
	 * @param c
	 * @param attr
	 * @param style
	 */
	public TempCurveView(Context c, AttributeSet attr, int style) {
		super(c, attr, style);
	}

	static final int XP = 30;
	static final int YP = 10;

	static final int MAX_DATA_CNT = 20;
	static final int MAX_DATA_CNTh = 20;

	int mXLength, mXStart;
	int mYLength, mYStart;

	byte mBaseValue = 0;

	byte[] mData = new byte[MAX_DATA_CNT];// 20个数组
	byte[] mDatah = new byte[MAX_DATA_CNTh];// 20个数组
	int mDataCnt = 0;
	int mDataCnth = 0;

	/**
	 * @param v当前温度值
	 *           eg，38
	 */
	public void addData(byte v, byte vh) {
		if (v <= 0)
			v = 0;
		if (v > 100)
			v = 100;
		if (vh <= 0)
			vh = 0;
		if (vh > 100)
			vh = 100;
		Log.d(TAG, "temp data:" + v);
		if (mDataCnt == 0)
			mBaseValue = v; // mBaseValue 38
			// if (mDataCnth == 0) mBaseValueh = vh; //mBaseValue 38

		
		for (int i = 1; i < MAX_DATA_CNT; i++) {
			mData[i - 1] = mData[i];// 0~19为空
		}
		for (int i = 1; i < MAX_DATA_CNTh; i++) {
			mDatah[i - 1] = mDatah[i];// 0~19为空
		}
		mData[MAX_DATA_CNT - 1] = v;// 最后一个 ，放38
		if (mDataCnt < MAX_DATA_CNT)
			mDataCnt++;

		mDatah[MAX_DATA_CNTh - 1] = vh;// 最后一个 ，放38
		if (mDataCnth < MAX_DATA_CNTh)
			mDataCnth++;

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

	void drawCross(Canvas canvas) { // Return the width of the your view,相当于窗体
		int w = this.getWidth(); // 500
		int h = getHeight(); // 212

		Paint paint = new Paint();
		/**
		 * 开始画图窗体区域View = 左spaceWidth + 文字textWidth + 曲线区域curveWidth +
		 * 右spaceWidth
		 */

		int spaceWidth = w * 5 / 100; // 25，左右边缘空白区域宽度
		int textWidth = w * 23 / 100; // 115，文字区域宽度，内部画框宽度

		int curveWidth = w - spaceWidth * 2 - textWidth; // 335，曲线部分宽度

		int sh = h * 8 / 100; // 17，顶部空白区域高度
		int ph = h - sh * 2; // 178，文字区，曲线区的高度

		int cw = curveWidth * 3 / 100; // 10
		int cs = cw + cw / 2; // 15
		int cv = curveWidth - cw - cs; // 310

		mXStart = spaceWidth + textWidth + cv; // 450
		mYStart = sh + ph / 2; // 106

		int dx = cv / (MAX_DATA_CNT - 1); // 16
		int dy = ph / 20; // 9
		mBaseValue = 50;
		/* 文子显示矩形框，带圆角弧度 */
		paint.setStrokeWidth(2);
		paint.setTextAlign(Align.CENTER);// 文字居中对齐
		paint.setStyle(Style.STROKE);
		paint.setColor(Color.RED);
		int textw = textWidth - textWidth * 3 / 20; // 框宽度97.7，文字区域宽度115，所以是在文字区域里边画框
		// 圆角矩形，具体实参画图
		canvas.drawRoundRect(new RectF(spaceWidth, sh, spaceWidth + textw, sh
				+ ph), 3, 3, paint);

		paint.setColor(Color.YELLOW);
		paint.setTextSize(25);
		paint.setStrokeWidth(1);
		paint.setStyle(Style.FILL_AND_STROKE);
		// 画当前温度值 居中
		canvas.drawText("当前温度:", spaceWidth + textw / 2, sh + ph / 2 - 40,
				paint);
		if (mDataCnt > 0) { // 38
			paint.setColor(Color.YELLOW);
			canvas.drawText(String.format("%d ℃", mData[MAX_DATA_CNT - 1]),
					spaceWidth + textw / 2, sh + ph / 2 + 0, paint);

		}
		// 画当前温度值 居中
		paint.setColor(Color.GREEN);
		canvas.drawText("当前湿度:", spaceWidth + textw / 2, sh + ph / 2 + 30,
				paint);
		if (mDataCnth > 0) { // 38
			paint.setColor(Color.GREEN);
			canvas.drawText(String.format("%d%%", mDatah[MAX_DATA_CNTh - 1]),
					spaceWidth + textw / 2, sh + ph / 2 + 70, paint);

		}
		/* 曲线矩形框，带圆角弧度 */
		paint.setStrokeWidth(2);
		paint.setStyle(Style.STROKE);
		paint.setColor(Color.RED);

		canvas.drawRoundRect(new RectF(spaceWidth + textWidth, sh, spaceWidth
				+ textWidth + cv + cw / 2, sh + ph), 3, 3, paint);

		paint.setStrokeWidth(1);
		for (int i = 1; i <= 10; i++) { // 上半部刻度
			int lw = (cw * 8 / 10);
			if (i % 2 != 0)
				lw /= 2;
			canvas.drawLine(mXStart + cw, mYStart - i * dy, mXStart + cw + lw,
					mYStart - i * dy, paint);
		}
		for (int i = 1; i <= 10; i++) { // 下半部刻度
			int lw = (cw * 8 / 10);
			if (i % 2 != 0)
				lw /= 2;
			canvas.drawLine(mXStart + cw, mYStart + i * dy, mXStart + cw + lw,
					mYStart + i * dy, paint);
		}

		paint.setStrokeWidth(2);
		// 一条表示温度的线段，没有高度，只有线的粗度Width
		canvas.drawLine(mXStart + cw, mYStart, mXStart + cw + cw, mYStart,
				paint); // 中心刻度线
		if (mDataCnt > 0) { // 刻度数字
			paint.setStrokeWidth(1);
			paint.setStyle(Style.FILL_AND_STROKE);
			paint.setTextSize(10);
			canvas.drawText(String.format("%d", this.mBaseValue), mXStart + cw
					* 2 + 2, mYStart + 8, paint);
		}

		paint.setStrokeWidth(2);
		paint.setColor(Color.BLUE);
		canvas.drawLine(spaceWidth + textWidth, mYStart, spaceWidth + textWidth
				+ cv, mYStart, paint);

		int x1 = mXStart, y1 = mYStart - (mData[MAX_DATA_CNT - 1] - mBaseValue)* dy;
		int x2, y2;
		int x1h = mXStart, y1h = mYStart
				- (mDatah[MAX_DATA_CNTh - 1] - mBaseValue) * dy;
		int x2h, y2h;
		paint.setStrokeWidth(3);
		paint.setColor(Color.GREEN);
		for (int i = 1; i < mDataCnt; i++) {
			// for (int i=1; i<10; i++) {
			paint.setColor(Color.YELLOW);
			x2 = mXStart - i * dx;
			y2 = mYStart - (mData[MAX_DATA_CNT - i - 1] - mBaseValue) * dy;
			canvas.drawLine(x1, y1, x2, y2, paint);
			x1 = x2;
			y1 = y2;

			paint.setColor(Color.GREEN);
			x2h = mXStart - i * dx;
			y2h = mYStart - (mDatah[MAX_DATA_CNTh - i - 1] - mBaseValue) * dy;
			canvas.drawLine(x1h, y1h, x2h, y2h, paint);
			x1h = x2h;
			y1h = y2h;
		}
		// 变化梯度
		LinearGradient mLinearGradient = new LinearGradient(0, 0, 20, 500,
				new int[] { Color.RED, Color.BLUE, }, null,
				Shader.TileMode.REPEAT);
		paint.setShader(mLinearGradient);
		paint.setStyle(Style.FILL);
		RectF post = new RectF(spaceWidth + textWidth + cv, sh, spaceWidth
				+ textWidth + cv + cw, sh + ph);
		canvas.drawRoundRect(post, 5, 5, paint);
		paint.clearShadowLayer();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		Paint paint = new Paint();
		paint.setColor(Color.TRANSPARENT);
		// 画一个矩形,前俩个是矩形左上角坐标，后面俩个是右下角坐标
		canvas.drawRect(new Rect(0, 0, getWidth(), getHeight()), paint);
		drawCross(canvas);
		// drawCurve(canvas);
	}

}
