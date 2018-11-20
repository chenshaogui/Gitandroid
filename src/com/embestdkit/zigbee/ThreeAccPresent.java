package com.embestdkit.zigbee;

import com.embestdkit.R;

import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.TabHost.TabSpec;

public class ThreeAccPresent extends NodePresent {

	static final String TAG = "HumiditySensorPresent";

	ZigBeeTool mZBTool;

	View mInfoView;
	ThreeXCurveView threexCurveView;
	ThreeYCurveView threeyCurveView;
	ThreeZCurveView threezCurveView;

	View mThreeConfigView;

	EditText mEditTextHighter;
	EditText mEditTextLower;
	EditText mEditTextNumber;

	CheckBox mCheckBox;

	int mAlarmHeighter;
	int mAlarmLower;
	// boolean mEnableAlarm = false;
	boolean mAlarmTriage = false;
	String mNumber;

	/**
	 * ʪ�ȣ�ʪ��
	 * 
	 * @param n
	 */
	ThreeAccPresent(Node n) {
		super(R.layout.threeacc, n);
		mInfoView = super.mView.findViewById(R.id.threeaccInfoView);
		threexCurveView = (ThreeXCurveView) super.mView
				.findViewById(R.id.threeaccxCurveView);
		// threeyCurveView = (ThreeYCurveView)
		// super.mView.findViewById(R.id.threeaccyCurveView);
		// threezCurveView = (ThreeZCurveView)
		// super.mView.findViewById(R.id.threeacczCurveView);

		mThreeConfigView = super.mView.findViewById(R.id.threeaccConfigView);

		mEditTextHighter = (EditText) mThreeConfigView
				.findViewById(R.id.threeacc_et_heighter);
		mEditTextLower = (EditText) mThreeConfigView
				.findViewById(R.id.threeacc_et_lower);
		mEditTextNumber = (EditText) mThreeConfigView
				.findViewById(R.id.threeacc_et_number);
		mCheckBox = (CheckBox) mThreeConfigView
				.findViewById(R.id.threeacc_checkbox_enable_alarm);

		mAlarmLower = Integer.parseInt(mEditTextLower.getText().toString());
		mAlarmHeighter = Integer
				.parseInt(mEditTextHighter.getText().toString());

		mEditTextHighter
				.setOnFocusChangeListener(new View.OnFocusChangeListener() {
					@Override
					public void onFocusChange(View v, boolean hasFocus) {
						if (!mEditTextHighter.isFocused()) {
							mAlarmHeighter = Integer.parseInt(mEditTextHighter
									.getText().toString());
						}
					}
				});
		mEditTextLower
				.setOnFocusChangeListener(new View.OnFocusChangeListener() {
					@Override
					public void onFocusChange(View v, boolean hasFocus) {
						if (!mEditTextLower.isFocused()) {
							mAlarmLower = Integer.parseInt(mEditTextLower
									.getText().toString());
						}
					}
				});

		final TabHost tabHost = (TabHost) super.mView
				.findViewById(android.R.id.tabhost);
		tabHost.setup();

		TabHost.TabSpec tb = tabHost.newTabSpec("0");
		// tb.setIndicator("", new BitmapDrawable(Resource.imageNodeInfo));
		// tb.setIndicator("�����Ϣ", null);
		tb.setIndicator("�����Ϣ");
		tb.setContent(new TabHost.TabContentFactory() {
			@Override
			public View createTabContent(String tag) {

				return mInfoView;
			}
		});
		tabHost.addTab(tb);

		TabSpec tb2 = tabHost.newTabSpec("1");
		// tb2.setIndicator("", new BitmapDrawable(Resource.imageNodeCurve));
		// tb2.setIndicator("ʪ������", null);
		tb2.setIndicator("������ֵ");
		tb2.setContent(new TabHost.TabContentFactory() {
			@Override
			public View createTabContent(String tag) {
				return threexCurveView;
			}
		});
		tabHost.addTab(tb2);

		// TabSpec tb3 = tabHost.newTabSpec("2");
		// tb3.setIndicator("Yֵ����");
		// tb3.setContent(new TabHost.TabContentFactory()
		// {
		// public View createTabContent(String tag) {
		// return threeyCurveView;
		// }
		// });
		// tabHost.addTab( tb3 );
		//
		// TabSpec tb4 = tabHost.newTabSpec("3");
		// tb4.setIndicator("Zֵ����");
		// tb4.setContent(new TabHost.TabContentFactory()
		// {
		// public View createTabContent(String tag) {
		// return threezCurveView;
		// }
		// });
		// tabHost.addTab( tb4 );

		tabHost.addTab(tabHost.newTabSpec("2").setIndicator("������")
				.setContent(new TabHost.TabContentFactory() {
					@Override
					public View createTabContent(String tag) {
						return mThreeConfigView;
					}
				}));
		tabHost.setCurrentTab(1);
		changeLayout(tabHost);
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

	@Override
	void setup() {
		// enable report temp value
		super.sendRequest(0x0002, new byte[] { 0x0A, 0x02, 0x05 });
	}

	@Override
	void setdown() {
		super.sendRequest(0x0002, new byte[] { 0x0A, 0x02, 0x00 });
	}

	@Override
	void procData(int req, byte[] dat) {
		float v;
		String s = new String(dat);
		if (s.contains("Temp")) {
			int i = s.lastIndexOf(':') + 1;
			s = s.substring(i, i + 3);
			// mTempCurveView.addData(new Float(s));
		}
		// mDataView.setText();
	}

	@Override
	void procAppMsgData(int addr, int cmd, byte[] dat) {
		int param;
		int valuex;
		int valuey;
		int valuez;

		if (addr != super.mNode.mNetAddr)
			return;
		if (cmd != 0x0003)
			return;
		if (dat.length < 3)
			return;

		Log.d(TAG, "current temp : " + dat[2]);

		param = Tool.builduInt(dat[0], dat[1]); // dat[0]<<8 | dat[1];
		valuex = Tool.builduInt(dat[2]); // dat[2];
		valuey = Tool.builduInt(dat[3]); // dat[2];
		valuez = Tool.builduInt(dat[4]); // dat[2];

		threexCurveView.addData((byte) valuex, (byte) valuey, (byte) valuez);
		// threeyCurveView.addData((byte)valuey);
		// threezCurveView.addData((byte)valuez);

		boolean alarm = false;
		if (mCheckBox.isChecked() && valuex > this.mAlarmHeighter) {
			if (!mAlarmTriage) {
				Log.d(TAG, "alarm height temp...");
				String title = "ʪ�Ⱦ���";
				String msg = "��ǰʪ��" + valuex + "���ڸ澯ֵ" + mAlarmHeighter;
				Tool.notify(title, msg);
				Tool.playAlarm(3);

				mNumber = mEditTextNumber.getText().toString();
				if (mNumber != null && mNumber.length() > 0) {
					Tool.sendShortMessage(mNumber, title + ":" + msg);
				}
			}
			mAlarmTriage = true;
		} else if (mCheckBox.isChecked() && valuex < this.mAlarmLower) {
			if (!mAlarmTriage) {
				Log.d(TAG, "alarm lower temp...");
				String title = "ʪ�Ⱦ���";
				String msg = "��ǰʪ��" + valuex + "���ڸ澯ֵ" + mAlarmLower;
				Tool.notify(title, msg);
				Tool.playAlarm(3);
				mNumber = mEditTextNumber.getText().toString();
				if (mNumber != null && mNumber.length() > 0) {
					Tool.sendShortMessage(mNumber, title + ":" + msg);
				}
			}
			mAlarmTriage = true;
		} else {
			mAlarmTriage = false;
		}
	}

}
