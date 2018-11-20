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

public class DecibelSensorPresent extends NodePresent {

	View mInfoView;
	View mConfigView;

	ImageView mDecibelImageView;

	Button mBtnEnable;
	Button mBtnDisable;

	EditText mAlarmNumberEditText;
	CheckBox mAlarmCheckBox;

	boolean mSensorEnable = true;
	boolean mSensorAlarm = false;

	// boolean mNeedStopAlarm = false;

	String mAlarmNumber;

	/**
	 * 人体红外传感器
	 * 
	 * @param n
	 */
	DecibelSensorPresent(Node n) {
		super(R.layout.decible_sensor, n);
		mInfoView = super.mView.findViewById(R.id.decibelsensorInfoView);
		mConfigView = super.mView.findViewById(R.id.decibelsensorConfigView);

		mDecibelImageView = (ImageView) mConfigView
				.findViewById(R.id.decibelsensorImageView);
		mBtnEnable = (Button) mConfigView
				.findViewById(R.id.decibelsensorBtnEnable);
		mBtnDisable = (Button) mConfigView
				.findViewById(R.id.decibelsensorBtnDisable);
		mAlarmNumberEditText = (EditText) mConfigView
				.findViewById(R.id.decibelsensorAlarmNumberEditText);
		mAlarmCheckBox = (CheckBox) mConfigView
				.findViewById(R.id.decibelsensorAlarmCheckBox);

		mBtnEnable.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (!mSensorEnable) {
					DecibelSensorPresent.super.sendRequest(0x0002, new byte[] {
							0x0D, 0x01, 0x01 });
				}
			}
		});
		mBtnDisable.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (mSensorEnable) {
					DecibelSensorPresent.super.sendRequest(0x0002, new byte[] {
							0x0D, 0x01, 0x00 });
				}
			}
		});
		/*
		 * mAlarmNumberEditText.setOnFocusChangeListener(new
		 * View.OnFocusChangeListener() { public void onFocusChange(View v,
		 * boolean hasFocus) { // TODO Auto-generated method stub if
		 * (!mAlarmNumberEditText.isFocused()) { mAlarmNumber =
		 * (mAlarmNumberEditText.getText().toString()); } } });
		 */

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

		/*
		 * final TabWidget tabWidget = (TabWidget)
		 * tabHost.findViewById(android.R.id.tabs); View v =
		 * tabWidget.getChildAt(0); v.setBackgroundDrawable(new
		 * BitmapDrawable(Resource.imageNodeInfo)); v = tabWidget.getChildAt(1);
		 * v.setBackgroundDrawable(new BitmapDrawable(Resource.imageNodeCurve));
		 * 
		 * tabHost.setOnTabChangedListener(new OnTabChangeListener(){ public
		 * void onTabChanged(String tabId) { // TODO Auto-generated method stub
		 * for (int i = 0; i < tabWidget.getChildCount(); i++) { View v =
		 * tabWidget.getChildAt(i); if (tabHost.getCurrentTab() == i) {
		 * v.getBackground().setColorFilter(Tool.selfilter); } else {
		 * v.getBackground().setColorFilter(Tool.unselfilter); }
		 * v.setBackgroundDrawable(v.getBackground()); } } });
		 */
		tabHost.setCurrentTab(1);

		mMyHandler = new MyHandler();
	}

	boolean mShowBlue = true;
	boolean mExit = false;

	class MyHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			if (mSensorEnable && mShowBlue) {
				if (mSensorAlarm) {
					mDecibelImageView
							.setImageBitmap(Resource.imageInfraredAlarm);
				} else {
					mDecibelImageView
							.setImageBitmap(Resource.imageInfraredBlue);
				}
			} else {
				mDecibelImageView.setImageBitmap(Resource.imageInfraredEnable);
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
			i = 0;
		if (cmd == 0x8001 && dat[0] == 0)
			i = 1;
		if (cmd == 0x8002 && dat[0] == 0) {

			if (mSensorEnable) {
				// mInfraredImageView.setImageBitmap(Resource.imageInfraredDisable);
				mSensorEnable = false;
			} else {
				// mInfraredImageView.setImageBitmap(Resource.imageInfraredEnable);
				mSensorEnable = true;
			}
			return;
		}
		while (i >= 0 && i < dat.length) {
			int pid = Tool.builduInt(dat[i], dat[i + 1]);
			switch (pid) {
			case 0x0D01:
				if (dat[i + 2] == 0) { // 结点被禁止
					// mInfraredImageView.setImageBitmap(Resource.imageInfraredDisable);
					mSensorEnable = false;
				} else {
					// mInfraredImageView.setImageBitmap(Resource.imageInfraredEnable);
					mSensorEnable = true;
				}
				i += 3;
				break;
			case 0x0D02:
				if (dat[i + 2] != 0) {
					/* 报警 */
					mSensorAlarm = true;
					// String msg = this.mNode.mNetAddr + ":检测到入侵";
					if (mAlarmCheckBox.isChecked()) {
						/* 发送告警通知和短信 */
						String msg = this.mNode.mNetAddr + ":检测声音";
						Tool.notify("分贝告警", msg);
						Tool.playAlarm(3);
						mAlarmNumber = (mAlarmNumberEditText.getText()
								.toString());
						if (mAlarmNumber != null && mAlarmNumber.length() > 0) {
							Tool.sendShortMessage(mAlarmNumber, msg);
						}
					}
					// mAlarmNumber =
					// (mAlarmNumberEditText.getText().toString());
					// if (mAlarmNumber != null && mAlarmNumber.length()>0) {
					// Tool.sendShortMessage(mAlarmNumber, msg);
					// }
				} else {
					mSensorAlarm = false;
				}
				i += 3;
				break;
			default:
				return;
			}
		}

	}

	@Override
	void procData(int req, byte[] dat) {
		// TODO Auto-generated method stub
	}

	@Override
	void setdown() {
		// TODO Auto-generated method stub
		mExit = true;
	}

	@Override
	void setup() {
		super.sendRequest(0x0001, new byte[] { 0x0D, 0x01, 0x0D, 0x02 });
		Message msg = Message.obtain();
		mMyHandler.sendMessageDelayed(msg, 300);
	}

}
