package com.embestdkit.zigbee;

import com.embestdkit.R;

import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.TabHost.TabSpec;

public class LightDevicePresent extends NodePresent {
	static final String TAG = "TempSensorPresent";

	ZigBeeTool mZBTool;

	View mInfoView;
	ThreeXLCurveView mLightCurveView;
	View mLightConfigView;

	EditText mEditTextHighter;
	EditText mEditTextLower;
	EditText mEditTextNumber;

	CheckBox mCheckBox;

	int mAlarmHeighter;
	int mAlarmLower;
	/**
	 * �Ǳ���״̬
	 */
	boolean mAlarmTriage = false;
	String mNumber;

	/**
	 * �¶�ģ��
	 * 
	 * @param n
	 */
	LightDevicePresent(Node n) {
		super(R.layout.light, n);
		// ͨ������ķ����ҵ�<LinearLayout
		// android:id="@+id/tempInfoView"���൱��xml�ļ��е�ͨ����ǩ���Ԫ��getElementByTag()
		mInfoView = super.mView.findViewById(R.id.lightInfoView);
		// ͨ������ķ����ҵ�<com.embedkit.zigbee.TempCurveView
		mLightCurveView = (ThreeXLCurveView) super.mView
				.findViewById(R.id.lightCurveView);
		// ͨ������ķ����ҵ�<LinearLayout android:id="@+id/tempConfigView"
		mLightConfigView = super.mView.findViewById(R.id.lightConfigView);
		// <LinearLayout android:id="@+id/tempConfigView"��ͼ��߰������ĸ��ɱ༭���
		mEditTextHighter = (EditText) mLightConfigView
				.findViewById(R.id.light_et_heighter);
		mEditTextLower = (EditText) mLightConfigView
				.findViewById(R.id.light_et_lower);
		mEditTextNumber = (EditText) mLightConfigView
				.findViewById(R.id.light_et_number);
		mCheckBox = (CheckBox) mLightConfigView
				.findViewById(R.id.light_checkbox_enable_alarm);
		// ����������
		mAlarmLower = Integer.parseInt(mEditTextLower.getText().toString());
		mAlarmHeighter = Integer
				.parseInt(mEditTextHighter.getText().toString());

		mEditTextHighter
				.setOnFocusChangeListener(new View.OnFocusChangeListener() {
					@Override
					public void onFocusChange(View v, boolean hasFocus) {
						// �ñ༭�ؼ�û��ý���
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

		// �����л���������
		final TabHost tabHost = (TabHost) super.mView
				.findViewById(android.R.id.tabhost);
		tabHost.setup();

		// TabSpec����һ��������л�����
		TabHost.TabSpec tb = tabHost.newTabSpec("0");// �ɼ�״̬
		// tb.setIndicator("", new BitmapDrawable(Resource.imageNodeInfo));
		// tb.setIndicator("�����Ϣ", null);
		tb.setIndicator("�����Ϣ");
		tb.setContent(new TabHost.TabContentFactory() {
			@Override
			public View createTabContent(String tag) {
				// ����ͼ�Ƕ�Ӧ�Ĳɼ�ͼ���л�����ʵ�����ݣ�һ��TextView ��ʾ�¶�ֵ����ǰ�¶� 27��
				return mInfoView;
			}
		});
		// ���һ��������л�����
		tabHost.addTab(tb);

		TabSpec tb2 = tabHost.newTabSpec("1"); // �ɼ�����
		// tb2.setIndicator("", new BitmapDrawable(Resource.imageNodeCurve));
		// tb2.setIndicator("�¶�����", null);
		tb2.setIndicator("��������");
		tb2.setContent(new TabHost.TabContentFactory() {
			@Override
			public View createTabContent(String tag) {
				// �Զ�����ͼ�ɼ�����
				return mLightCurveView;
			}
		});
		tabHost.addTab(tb2);

		tabHost.addTab(tabHost
				.newTabSpec("2")
				// ����������
				// .setIndicator("�¶ȿ���", null)
				.setIndicator("������")
				.setContent(new TabHost.TabContentFactory() {
					@Override
					public View createTabContent(String tag) {
						// �Զ�����ͼ ������
						return mLightConfigView;
					}
				}));
		// ��ǰ��ʾ���л�����
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
		super.sendRequest(0x0002, new byte[] { 0x02, 0x02, 0x05 });
	}

	@Override
	void setdown() {
		super.sendRequest(0x0002, new byte[] { 0x02, 0x02, 0x00 });
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
		int value1;
		int value2;
		int value3;
		// short value;
		byte value;

		if (addr != super.mNode.mNetAddr)
			return;
		if (cmd != 0x0003)
			return;
		if (dat.length < 3)
			return;

		Log.d(TAG, "current temp : " + dat[2]);

		param = Tool.builduInt(dat[0], dat[1]); // dat[0]<<8 | dat[1];

		value1 = Tool.builduInt(dat[2]); // dat[2];b&0xff
		value2 = (Tool.builduInt(dat[3])); // dat[2];b&0xff
		// value3 = (Tool.builduInt(dat[4])); //dat[2];b&0xff
		value = (byte) (value1 * 10 + value2);
		// mLightCurveView.addData((byte)value);//ȥ��ʼ�� ��ͼ
		mLightCurveView.addData(value);// ȥ��ʼ�� ��ͼ

		boolean alarm = false;
		// ��ѡ��ѡ ����ǰ�¶�ֵ��������
		if (mCheckBox.isChecked() && value > this.mAlarmHeighter) {
			// �Ǳ���״̬�Ļ�
			if (!mAlarmTriage) {
				Log.d(TAG, "alarm height temp...");
				String title = "�߹�ǿ����";
				String msg = "��ǰ����" + value + "���ڸ澯ֵ" + mAlarmHeighter;
				Tool.notify(title, msg);
				Tool.playAlarm(3);

				mNumber = mEditTextNumber.getText().toString();
				if (mNumber != null && mNumber.length() > 0) {
					Tool.sendShortMessage(mNumber, title + ":" + msg);
				}
			}
			// ���ڱ���״̬
			mAlarmTriage = true;
		} else if (mCheckBox.isChecked() && value < this.mAlarmLower) {
			if (!mAlarmTriage) {
				Log.d(TAG, "alarm lower temp...");
				String title = "�͹�ǿ����";
				String msg = "��ǰ����" + value + "���ڸ澯ֵ" + mAlarmLower;
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
