package com.embestdkit.zigbee;

import com.embestdkit.R;

import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;

public class RainPresent extends NodePresent {

	View mInfoView;
	View mConfigView;

	ImageView mRainSensorImageView;

	Button mBtnDisable;
	Button mBtnEnable;

	CheckBox mAlarmCheckBox;
	EditText mAlarmNumberEditText;

	boolean mSensorEnable = true;
	boolean mSensorAlarm = false;
	String mAlarmNumber;

	/**
	 * 雨滴传感器
	 * 
	 * @param n
	 */
	RainPresent(Node n) {
		super(R.layout.rain_sensor, n);
		mInfoView = super.mView.findViewById(R.id.rainsensorInfoView);
		mConfigView = super.mView.findViewById(R.id.rainsensorConfigView);
		mRainSensorImageView = (ImageView) mConfigView
				.findViewById(R.id.rainsensorImageView);

		mBtnEnable = (Button) mConfigView
				.findViewById(R.id.rainsensorBtnEnable);
		mBtnEnable.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if (!mSensorEnable) {
					RainPresent.super.sendRequest(0x0002, new byte[] { 0x08,
							0x01, 0x01 });
				}
			}
		});

		mBtnDisable = (Button) mConfigView
				.findViewById(R.id.rainsensorBtnDisable);
		mBtnDisable.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if (mSensorEnable) {
					RainPresent.super.sendRequest(0x0002, new byte[] { 0x08,
							0x01, 0x00 });
				}
			}
		});

		// mAlarmNumberEditText =
		// (EditText)mConfigView.findViewById(R.id.rainsensorAlarmNumberEditText);
		mAlarmCheckBox = (CheckBox) mConfigView
				.findViewById(R.id.rainsensorAlarmCheckBox);

		final TabHost tabHost = ((TabHost) super.mView
				.findViewById(android.R.id.tabhost));
		tabHost.setup();

		tabHost.addTab(tabHost.newTabSpec("0")
				// .setIndicator("", new BitmapDrawable(Resource.imageNodeInfo))
				.setIndicator("结点信息")
				.setContent(new TabHost.TabContentFactory() {
					@Override
					public View createTabContent(String tag) {
						return mInfoView;
					}
				}));

		tabHost.addTab(tabHost.newTabSpec("1")
				// .setIndicator("", new BitmapDrawable(Resource.imageNodeInfo))
				.setIndicator("监测控制")
				.setContent(new TabHost.TabContentFactory() {
					@Override
					public View createTabContent(String tag) {
						return mConfigView;
					}
				}));

		tabHost.setCurrentTab(1);
		mMyHandler = new MyHandler();
		changeLayout(tabHost);
	}

	// 可以改变TabHost的高度和字体大小
	private void changeLayout(TabHost tabHost) {

		int count = tabHost.getTabWidget().getChildCount();// TabHost中有一个getTabWidget()的方法
		for (int i = 0; i < count; i++) {
			View view = tabHost.getTabWidget().getChildTabViewAt(i);
			view.getLayoutParams().height = 60;// 改变TabHost的高度
			view.setBackgroundColor(android.graphics.Color.BLACK);// 将当前选中的标签页设置为红色
			final TextView tv = (TextView) view
					.findViewById(android.R.id.title);
			tv.setTextSize(16);// 改变文字大小
		}
	}

	boolean mShowBlue = true;
	boolean mExit = false;

	class MyHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			if (mSensorEnable && mShowBlue) {
				if (mSensorAlarm) {
					mRainSensorImageView
							.setImageBitmap(Resource.imageGasSensorAlarm);
					// mRainSensorImageView.setImageBitmap(Resource.imageGasSensorBlue);
				} else {
					mRainSensorImageView
							.setImageBitmap(Resource.imageGasSensorBlue);
					// mRainSensorImageView.setImageBitmap(Resource.imageGasSensorAlarm);
				}
			} else {
				mRainSensorImageView
						.setImageBitmap(Resource.imageGasSensorEnable);
			}
			if (!mExit) {
				Message msg2 = Message.obtain();
				mMyHandler.sendMessageDelayed(msg2, 300);
			}
			mShowBlue = !mShowBlue;
		}
	}

	MyHandler mMyHandler;

	@Override
	void procAppMsgData(int addr, int cmd, byte[] dat) {
		int i = -1;
		if (addr != super.mNode.mNetAddr)
			return;

		if (cmd == 0x0003)
			i = 0;// 主 动 上 报 传 感 器 值
		if (cmd == 0x8001 && dat[0] == 0)
			i = 1;// 读 参 数 响 应
		if (cmd == 0x8002 && dat[0] == 0) {// 写 参 数 响 应
			if (mSensorEnable) {
				// mCo2SensorImageView.setImageBitmap(Resource.imageCo2SensorBlue);
				mSensorEnable = false;
			} else {
				// mCo2SensorImageView.setImageBitmap(Resource.imageCo2SensorEnable);
				mSensorEnable = true;
			}
			return;
		}

		while (i >= 0 && i < dat.length) {
			int pid = Tool.builduInt(dat[i], dat[i + 1]);
			if (pid == 0x0801) {
				if (dat[i + 2] == 0) {
					/* 传感结点被禁止 */
					mSensorEnable = false;
				} else {
					mSensorEnable = true;
				}
				i += 3;

			} else if (pid == 0x0802) {
				if (dat[i + 2] != 0) {
					/* 报警 */
					mSensorAlarm = true;
					if (mAlarmCheckBox.isChecked()) {
						/* 发送告警通知和短信 */
						String msg = this.mNode.mNetAddr + ":检测到雨滴";
						Tool.notify("雨滴告警", msg);
						Tool.playAlarm(3);
						// mAlarmNumber =
						// (mAlarmNumberEditText.getText().toString());
						// if (mAlarmNumber != null && mAlarmNumber.length()>0)
						// {
						// Tool.sendShortMessage(mAlarmNumber, msg);
						// }
					}
				} else {
					mSensorAlarm = false;
				}
				i += 3;
			} else {
				return;
			}
		}
	}

	@Override
	void procData(int req, byte[] dat) {

	}

	@Override
	void setdown() {
		mExit = true;
	}

	@Override
	void setup() {
		super.sendRequest(0x0001, new byte[] { 0x08, 0x01, 0x08, 0x02 });
		Message msg = Message.obtain();
		mMyHandler.sendMessageDelayed(msg, 300);
	}
}
