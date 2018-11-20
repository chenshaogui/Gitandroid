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

public class FlameSensorPresent extends NodePresent {

	View mInfoView;
	View mConfigView;

	ImageView mFlameSensorImageView;

	Button mBtnDisable;
	Button mBtnEnable;

	CheckBox mAlarmCheckBox;
	EditText mAlarmNumberEditText;

	boolean mSensorEnable = true;
	boolean mSensorAlarm = false;
	String mAlarmNumber;

	/**
	 * ����
	 * @param n
	 */
	FlameSensorPresent(Node n) {
		super(R.layout.flame_sensor, n);
		mInfoView = super.mView.findViewById(R.id.flamesensorInfoView);
		mConfigView = super.mView.findViewById(R.id.flamesensorConfigView);

		mFlameSensorImageView = (ImageView) mConfigView.findViewById(R.id.flamesensorImageView);

		mBtnEnable = (Button) mConfigView.findViewById(R.id.flamesensorBtnEnable);
		mBtnEnable.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if (!mSensorEnable) {
					FlameSensorPresent.super.sendRequest(0x0002, new byte[] {
							0x0E, 0x01, 0x01 });
				}
			}
		});

		mBtnDisable = (Button) mConfigView
				.findViewById(R.id.flamesensorBtnDisable);
		mBtnDisable.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if (mSensorEnable) {
					FlameSensorPresent.super.sendRequest(0x0002, new byte[] {
							0x0E, 0x01, 0x00 });
				}
			}
		});

		mAlarmNumberEditText = (EditText) mConfigView
				.findViewById(R.id.flamesensorAlarmNumberEditText);
		mAlarmCheckBox = (CheckBox) mConfigView
				.findViewById(R.id.flamesensorAlarmCheckBox);

		final TabHost tabHost = ((TabHost) super.mView
				.findViewById(android.R.id.tabhost));
		tabHost.setup();

		tabHost.addTab(tabHost.newTabSpec("0")
				// .setIndicator("", new BitmapDrawable(Resource.imageNodeInfo))
				.setIndicator("�����Ϣ")
				.setContent(new TabHost.TabContentFactory() {
					@Override
					public View createTabContent(String tag) {
						return mInfoView;
					}
				}));

		tabHost.addTab(tabHost.newTabSpec("1")
				// .setIndicator("", new BitmapDrawable(Resource.imageNodeInfo))
				.setIndicator("������")
				.setContent(new TabHost.TabContentFactory() {
					@Override
					public View createTabContent(String tag) {
						return mConfigView;
					}
				}));

		tabHost.setCurrentTab(1);
		changeLayout(tabHost);
		mMyHandler = new MyHandler();
	}
	// ���Ըı�TabHost�ĸ߶Ⱥ������С
			private void changeLayout(TabHost tabHost) {
				int count = tabHost.getTabWidget().getChildCount();// TabHost����һ��getTabWidget()�ķ���
				for (int i = 0; i < count; i++) {
					View view = tabHost.getTabWidget().getChildTabViewAt(i);
					view.getLayoutParams().height = 60;// �ı�TabHost�ĸ߶�
					view.setBackgroundColor(android.graphics.Color.BLACK);// ����ǰѡ�еı�ǩҳ����Ϊ��ɫ
					final TextView tv = (TextView) view
							.findViewById(android.R.id.title);
					tv.setTextSize(16);// �ı����ִ�С
				}
			}
	
	boolean mShowBlue = true;
	boolean mExit = false;

	class MyHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			if (mSensorEnable && mShowBlue) {
				if (mSensorAlarm) {
					mFlameSensorImageView.setImageBitmap(Resource.imageGasSensorAlarm);
				} else {
					mFlameSensorImageView.setImageBitmap(Resource.imageGasSensorBlue);
				}
			} else {
				mFlameSensorImageView.setImageBitmap(Resource.imageGasSensorEnable);
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
			i = 0;// �� �� �� �� �� �� �� ֵ
		if (cmd == 0x8001 && dat[0] == 0)
			i = 1;// �� �� �� �� Ӧ
		if (cmd == 0x8002 && dat[0] == 0) {// д �� �� �� Ӧ
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
			if (pid == 0x0E01) {
				if (dat[i + 2] == 0) {
					/* ���н�㱻��ֹ */
					mSensorEnable = false;
				} else {
					mSensorEnable = true;
				}
				i += 3;

			} else if (pid == 0x0E02) {
				if (dat[i + 2] != 0) {
					/* ���� */
					mSensorAlarm = true;
					if (mAlarmCheckBox.isChecked()) {
						/* ���͸澯֪ͨ�Ͷ��� */
						String msg = this.mNode.mNetAddr + ":��⵽����";
						Tool.notify("����澯", msg);
						Tool.playAlarm(3);
						mAlarmNumber = (mAlarmNumberEditText.getText()
								.toString());
						if (mAlarmNumber != null && mAlarmNumber.length() > 0) {
							Tool.sendShortMessage(mAlarmNumber, msg);
						}
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
		super.sendRequest(0x0001, new byte[] { 0x0E, 0x01, 0x0E, 0x02 });
		Message msg = Message.obtain();
		mMyHandler.sendMessageDelayed(msg, 300);
	}
}
