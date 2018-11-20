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

public class UltraCurveView extends View {
	static String TAG = "UltraCurveView";

	// Bitmap mBmp = Bitmap.createBitmap(500, 100, Bitmap.Config.ARGB_4444);
	public UltraCurveView(Context context) {
		super(context);

	}

	public UltraCurveView(Context c, AttributeSet attr) {
		super(c, attr);

	}

	/**
	 * �Զ������ͼ�� �ɼ�����
	 * @param c
	 * @param attr
	 * @param style
	 */
	public UltraCurveView(Context c, AttributeSet attr, int style) {
		super(c, attr, style);
	}

	static final int XP = 30;
	static final int YP = 10;

	static final int MAX_DATA_CNT = 20;

	int mXLength, mXStart;
	int mYLength, mYStart;

	byte mBaseValue = 0;

	byte[] mData = new byte[MAX_DATA_CNT];// 20������
	int mDataCnt = 0;

	/**
	 * @param v��ǰ�¶�ֵ
	 *            ��38
	 */
	public void addData(byte v) {
		if (v < 0)
			v = 0;
		if (v > 127)
			v = 127;
		
		 Log.e(TAG, "current data:" + v);

		if (mDataCnt == 0)
			mBaseValue = v; // mBaseValue 38

		for (int i = 1; i < MAX_DATA_CNT; i++) {
			mData[i - 1] = mData[i];// 0~19Ϊ��
		}
		mData[MAX_DATA_CNT - 1] = v;// ���һ�� ����38

		if (mDataCnt < MAX_DATA_CNT)
			mDataCnt++;

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
			// c.drawLine(pa[2*i], pa[2*i+1], pa[2*(i-1)], pa[2*(i-1)+1],
			// paint);
		}
	}

	void drawCross(Canvas canvas) { // Return the width of the your view,�൱�ڴ���
		int w = this.getWidth(); // 500
		int h = getHeight(); // 212

		Paint paint = new Paint();

		/**
		 * ��ʼ��ͼ��������View = ��spaceWidth + ����textWidth + ��������curveWidth +
		 * ��spaceWidth
		 */

		int spaceWidth = w * 5 / 100; // 25�����ұ�Ե�հ�������
		int textWidth = w * 23 / 100; // 115�����������ȣ��ڲ�������

		int curveWidth = w - spaceWidth * 2 - textWidth; // 335�����߲��ֿ��

		int sh = h * 8 / 100; // 17�������հ�����߶�
		int ph = h - sh * 2; // 178�����������������ĸ߶�

		int cw = curveWidth * 3 / 100; // 10
		int cs = cw + cw / 2; // 15
		int cv = curveWidth - cw - cs; // 310

		mXStart = spaceWidth + textWidth + cv; // 450
		mYStart = sh + ph / 2; // 106

		int dx = cv / (MAX_DATA_CNT - 1); // 16
		int dy = ph / 20; // 9

		/* ������ʾ���ο򣬴�Բ�ǻ��� */
		paint.setStrokeWidth(2);
		paint.setTextAlign(Align.CENTER);// ���־��ж���
		paint.setStyle(Style.STROKE);
		paint.setColor(Color.RED);
		int textw = textWidth - textWidth * 3 / 20; // ����97.7������������115��������������������߻���
		// Բ�Ǿ��Σ�����ʵ�λ�ͼ
		// canvas.drawRoundRect(new RectF(spaceWidth, sh,
		// spaceWidth+textw, sh+ph), 3, 3, paint);

		paint.setColor(Color.YELLOW);
		paint.setTextSize(50);
		paint.setStrokeWidth(1);
		paint.setStyle(Style.FILL_AND_STROKE);
		// ����ǰ�¶�ֵ ����
		canvas.drawText("��ǰ����", spaceWidth + textw / 2 + 300, sh + ph / 2 - 20,paint);
        Log.e(TAG, "ִ�е����canvas.drawText��ǰ����,");
        Log.e(TAG, String.valueOf(mDataCnt));
        
		if (mDataCnt > 0) { // 38
			 Log.e(TAG, "ִ�е������ֵ��λ��,");
			canvas.drawText(String.format("%d cm", mData[MAX_DATA_CNT - 1]),
//				canvas.drawText(String.format("%d cm", 15),

					spaceWidth + textw / 2 + 300, sh + ph / 2 + 40 , paint);

		}
		Log.e(TAG, "ִ�е������д���,");
		/* ���߾��ο򣬴�Բ�ǻ��� */
		paint.setStrokeWidth(2);
		paint.setStyle(Style.STROKE);
		paint.setColor(Color.RED);

		// canvas.drawRoundRect(new RectF(spaceWidth+textWidth, sh,
		// spaceWidth+textWidth+cv+cw/2, sh+ph), 3, 3, paint);

		paint.setStrokeWidth(1);
		for (int i = 1; i <= 10; i++) { // �ϰ벿�̶�
			int lw = (cw * 8 / 10);
			if (i % 2 != 0)
				lw /= 2;
			// canvas.drawLine(mXStart+cw, mYStart-i*dy,
			// mXStart+cw+lw, mYStart-i*dy, paint);
		}
		for (int i = 1; i <= 10; i++) { // �°벿�̶�
			int lw = (cw * 8 / 10);
			if (i % 2 != 0)
				lw /= 2;
			// canvas.drawLine(mXStart+cw, mYStart+i*dy,
			// mXStart+cw+lw, mYStart+i*dy, paint);
		}

		paint.setStrokeWidth(2);
		// һ����ʾ�¶ȵ��߶Σ�û�и߶ȣ�ֻ���ߵĴֶ�Width
		// canvas.drawLine(mXStart+cw, mYStart, mXStart+cw+cw, mYStart, paint);
		// //���Ŀ̶���
		if (mDataCnt > 0) { // �̶�����
			paint.setStrokeWidth(1);
			paint.setStyle(Style.FILL_AND_STROKE);
			paint.setTextSize(10);
			// canvas.drawText(String.format("%d", this.mBaseValue),
			// mXStart+cw*2+2, mYStart+8, paint);
		}

		paint.setStrokeWidth(2);
		paint.setColor(Color.BLUE);
		// canvas.drawLine(spaceWidth+textWidth, mYStart,
		// spaceWidth+textWidth+cv, mYStart, paint);

		int x1 = mXStart, y1 = mYStart - (mData[MAX_DATA_CNT - 1] - mBaseValue)* dy;
		int x2, y2;

		paint.setStrokeWidth(3);
		paint.setColor(Color.GREEN);
		for (int i = 1; i < mDataCnt; i++) {
			x2 = mXStart - i * dx;
			y2 = mYStart - (mData[MAX_DATA_CNT - i - 1] - mBaseValue) * dy;
			// canvas.drawLine(x1,y1, x2, y2, paint);
			x1 = x2;
			y1 = y2;
		}
		// �仯�ݶ�
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
		Paint paint = new Paint();
		paint.setColor(Color.TRANSPARENT);
		// ��һ������,ǰ�����Ǿ������Ͻ����꣬�������������½�����
		canvas.drawRect(new Rect(0, 0, getWidth(), getHeight()), paint);
		drawCross(canvas);
		drawCurve(canvas);
	}

}
