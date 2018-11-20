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
	 * 非报警状态
	 */
	boolean mAlarmTriage = false;
	String mNumber;

	/**
	 * 温度模块
	 * 
	 * @param n
	 */
	LightDevicePresent(Node n) {
		super(R.layout.light, n);
		// 通过父类的方法找到<LinearLayout
		// android:id="@+id/tempInfoView"，相当于xml文件中的通过标签获得元素getElementByTag()
		mInfoView = super.mView.findViewById(R.id.lightInfoView);
		// 通过父类的方法找到<com.embedkit.zigbee.TempCurveView
		mLightCurveView = (ThreeXLCurveView) super.mView
				.findViewById(R.id.lightCurveView);
		// 通过父类的方法找到<LinearLayout android:id="@+id/tempConfigView"
		mLightConfigView = super.mView.findViewById(R.id.lightConfigView);
		// <LinearLayout android:id="@+id/tempConfigView"视图里边包含的四个可编辑组件
		mEditTextHighter = (EditText) mLightConfigView
				.findViewById(R.id.light_et_heighter);
		mEditTextLower = (EditText) mLightConfigView
				.findViewById(R.id.light_et_lower);
		mEditTextNumber = (EditText) mLightConfigView
				.findViewById(R.id.light_et_number);
		mCheckBox = (CheckBox) mLightConfigView
				.findViewById(R.id.light_checkbox_enable_alarm);
		// 报警上下限
		mAlarmLower = Integer.parseInt(mEditTextLower.getText().toString());
		mAlarmHeighter = Integer
				.parseInt(mEditTextHighter.getText().toString());

		mEditTextHighter
				.setOnFocusChangeListener(new View.OnFocusChangeListener() {
					@Override
					public void onFocusChange(View v, boolean hasFocus) {
						// 该编辑控件没获得焦点
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

		// 定义切换卡，三个
		final TabHost tabHost = (TabHost) super.mView
				.findViewById(android.R.id.tabhost);
		tabHost.setup();

		// TabSpec定义一个具体的切换卡项
		TabHost.TabSpec tb = tabHost.newTabSpec("0");// 采集状态
		// tb.setIndicator("", new BitmapDrawable(Resource.imageNodeInfo));
		// tb.setIndicator("结点信息", null);
		tb.setIndicator("结点信息");
		tb.setContent(new TabHost.TabContentFactory() {
			@Override
			public View createTabContent(String tag) {
				// 该视图是对应的采集图像切换卡的实际内容，一个TextView 显示温度值：当前温度 27度
				return mInfoView;
			}
		});
		// 添加一个具体的切换卡项
		tabHost.addTab(tb);

		TabSpec tb2 = tabHost.newTabSpec("1"); // 采集波形
		// tb2.setIndicator("", new BitmapDrawable(Resource.imageNodeCurve));
		// tb2.setIndicator("温度曲线", null);
		tb2.setIndicator("光敏曲线");
		tb2.setContent(new TabHost.TabContentFactory() {
			@Override
			public View createTabContent(String tag) {
				// 自定义视图采集波形
				return mLightCurveView;
			}
		});
		tabHost.addTab(tb2);

		tabHost.addTab(tabHost
				.newTabSpec("2")
				// 传感器控制
				// .setIndicator("温度控制", null)
				.setIndicator("监测控制")
				.setContent(new TabHost.TabContentFactory() {
					@Override
					public View createTabContent(String tag) {
						// 自定义视图 传感器
						return mLightConfigView;
					}
				}));
		// 当前显示的切换卡项
		tabHost.setCurrentTab(1);
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
		// mLightCurveView.addData((byte)value);//去初始化 画图
		mLightCurveView.addData(value);// 去初始化 画图

		boolean alarm = false;
		// 复选框勾选 ，当前温度值大于上限
		if (mCheckBox.isChecked() && value > this.mAlarmHeighter) {
			// 非报警状态的话
			if (!mAlarmTriage) {
				Log.d(TAG, "alarm height temp...");
				String title = "高光强警告";
				String msg = "当前光敏" + value + "高于告警值" + mAlarmHeighter;
				Tool.notify(title, msg);
				Tool.playAlarm(3);

				mNumber = mEditTextNumber.getText().toString();
				if (mNumber != null && mNumber.length() > 0) {
					Tool.sendShortMessage(mNumber, title + ":" + msg);
				}
			}
			// 置于报警状态
			mAlarmTriage = true;
		} else if (mCheckBox.isChecked() && value < this.mAlarmLower) {
			if (!mAlarmTriage) {
				Log.d(TAG, "alarm lower temp...");
				String title = "低光强警告";
				String msg = "当前光敏" + value + "低于告警值" + mAlarmLower;
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
