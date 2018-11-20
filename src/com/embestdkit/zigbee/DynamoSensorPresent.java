package com.embestdkit.zigbee;

import com.embestdkit.R;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;

public class DynamoSensorPresent extends NodePresent implements OnClickListener {

	static final String TAG = "LightDevicePresent";

	View mInfoView;
	View mCtrolView;

	TextView mLogView;

	Button mbtnMove;
	Button mbtnOrientation;
	Button mbtnSpeeed;

	ImageView mMoveImageView;
	ImageView mOrientationImageView;
	ImageView mSpeeedImageView;

	Button mBtnDisable;
	Button mBtnEnable;

	CheckBox mAlarmCheckBox;
	EditText mAlarmNumberEditText;

	int MEnable0 = 0;// 开始状态，0断开，1转动
	int OEnable0 = 0;
	int SEnable0 = 0;

	boolean mMove = false; // 默认是关闭
	// boolean mOrientation = false;
	// boolean mSpeeed = false;

	// ImageView mLightImageView;
	// Button mBtnOff;
	// Button mBtnOn;
	// Button mBtnReverse;

	/**
	 * 电机模块
	 * 
	 * @param n
	 */
	DynamoSensorPresent(Node n) {
		super(R.layout.dynamo_sensor, n);

		mInfoView = super.mView.findViewById(R.id.dynamoInfoView);

		mCtrolView = super.mView.findViewById(R.id.dynamoControlView);
		mLogView = (TextView) super.mView.findViewById(R.id.dynamoLogView);

		mMoveImageView = (ImageView) mCtrolView
				.findViewById(R.id.dynamoMImageView);
		mOrientationImageView = (ImageView) mCtrolView
				.findViewById(R.id.dynamoOImageView);
		// mSpeeedImageView = (ImageView)
		// mCtrolView.findViewById(R.id.dynamoSImageView);

		mbtnMove = ((Button) mCtrolView.findViewById(R.id.dynamoBtnMove));
		mbtnMove.setOnClickListener(this);
		mbtnOrientation = ((Button) mCtrolView
				.findViewById(R.id.dynamoBtnOrientation));
		mbtnOrientation.setOnClickListener(this);
		// mbtnSpeeed = ((Button) mCtrolView.findViewById(R.id.dynamoBtnSpeed));
		// mbtnSpeeed.setOnClickListener(this);

		// mBtnOff = ((Button) mCtrolView.findViewById(R.id.lightBtnOff));
		// mBtnOff.setOnClickListener(this);
		// mBtnOn = ((Button) mCtrolView.findViewById(R.id.lightBtnOn));
		// mBtnOn.setOnClickListener(this);
		// mBtnReverse = ((Button)
		// mCtrolView.findViewById(R.id.lightBtnReverse));
		// mBtnReverse.setOnClickListener(this);

		final TabHost tabHost = ((TabHost) super.mView
				.findViewById(android.R.id.tabhost));
		tabHost.setup();

		tabHost.addTab(tabHost
				.newTabSpec("0")
				// 基本信息业
				// .setIndicator("", new BitmapDrawable(Resource.imageNodeInfo))
				.setIndicator("结点信息")
				.setContent(new TabHost.TabContentFactory() {
					@Override
					public View createTabContent(String tag) {
						return mInfoView;
					}
				}));

		tabHost.addTab(tabHost
				.newTabSpec("1")
				// 设置信息页
				// .setIndicator("", new BitmapDrawable(Resource.imageNodeInfo))
				.setIndicator("监测控制")
				.setContent(new TabHost.TabContentFactory() {
					@Override
					public View createTabContent(String tag) {
						return mCtrolView;
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
		changeLayout(tabHost);
	}

	// 可以改变TabHost的高度和字体大小
	private void changeLayout(TabHost tabHost) {
		int count = tabHost.getTabWidget().getChildCount();// TabHost中有一个getTabWidget()的方法
		for (int i = 0; i < count; i++) {
			View view = tabHost.getTabWidget().getChildTabViewAt(i);
			view.getLayoutParams().height = 60;// 改变TabHost的高度
			view.setBackgroundColor(android.graphics.Color.BLACK);// 将当前选中的标签页设置为红色
			final TextView tv = (TextView) view.findViewById(android.R.id.title);
			tv.setTextSize(16);// 改变文字大小
		}
	}

	// public void onClick(View v) {
	// byte[] dat = new byte[3];
	// dat[0] = 0x02;
	// dat[1] = 0x02;
	// if (v == mbtnMove) {
	// dat[2] = 0;
	// if (mMove) {
	// mMove = false;
	// } else {
	// mMove = true;
	// }
	// } else if (v == mbtnOrientation) {
	// dat[2] = 0;
	// if (mOrientation) {
	// mOrientation = false;
	// } else {
	// mOrientation = true;
	// }
	// } else if (v == mbtnSpeeed) {
	// dat[2] = 0;
	// if (mSpeeed) {
	// mSpeeed = false;
	// } else {
	// mSpeeed = true;
	// }
	// }
	// super.sendRequest(0x0002, dat);
	// }
	@Override
	public void onClick(View v) {
		// byte[] dat = new byte[3];
		// if (v == mbtnMove) {
		// dat[0] = 0x0B;
		// dat[1] = 0x01;
		// dat[2] = 0;
		// // if (mMove) {
		// // mMove = false;
		// // } else {
		// // mMove = true;
		// // }
		// if (data == 0) {
		// mMove = false;
		// } else {
		// mMove = true;
		// }
		// } else if (v == mbtnOrientation) {
		// dat[0] = 0x0B;
		// dat[1] = 0x02;
		// dat[2] = 0;
		// if (mOrientation) {
		// mOrientation = false;
		// } else {
		// mOrientation = true;
		// }
		// }
		// super.sendRequest(0x0002, dat);
		byte[] dat = new byte[3];
		if (v == mbtnMove) {// 点击正转
		// if (MEnable0 == 0) {
		// super.sendRequest(0x0002, new byte[]{0x0B,0x01, 0x01});//启用
		// MEnable0 = 0x01;
		// mMove = true;
		// } else {
		// super.sendRequest(0x0002, new byte[]{0x0B,0x01, 0x00});
		// MEnable0 = 0x00;
		// mMove = false;
		// }
			// super.sendRequest(0x0002, new byte[]{0x0B,0x01, 0x00});//停止命令
			super.sendRequest(0x0002, new byte[] { 0x0B, 0x02, 0x01 });// 正转命令
			// super.sendRequest(0x0002, new byte[]{0x0B,0x01, 0x01});//转动
			MEnable0 = 0x00;
			OEnable0 = 0x00;

		} else if ((v == mbtnOrientation)) {
			// if (OEnable0 == 0) {
			// super.sendRequest(0x0002, new byte[]{0x0B,0x02, 0x01});//启用
			// OEnable0 = 0x01;
			// } else {
			// super.sendRequest(0x0002, new byte[]{0x0B,0x02, 0x00});
			// OEnable0 = 0x00;
			// }
			// super.sendRequest(0x0002, new byte[]{0x0B,0x01, 0x00});//停止命令
			super.sendRequest(0x0002, new byte[] { 0x0B, 0x02, 0x00 });// 反转命令
			// super.sendRequest(0x0002, new byte[]{0x0B,0x01, 0x01});//转动
			MEnable0 = 0x00;
			OEnable0 = 0x01;
		} // else if (v == mbtnSpeeed) {
		// // if (SEnable0 == 0) {
		// // super.sendRequest(0x0002, new byte[]{0x0B,0x03, 0x01});//启用
		// // SEnable0 = 0x01;
		// // } else {
		// // super.sendRequest(0x0002, new byte[]{0x0B,0x03, 0x00});
		// // SEnable0 = 0x00;
		// // }
		// super.sendRequest(0x0002, new byte[]{0x0B,0x01, 0x00});//停止命令
		// MEnable0 = 0x01;
		// }
	}

	@Override
	void procAppMsgData(int addr, int cmd, byte[] dat) {
		// int pid;
		// int i = -1;
		// Log.d(TAG, Tool.byteTostring(dat));
		// if (cmd == 0x8001 && dat[0] == 0) {
		// i = 1;
		// }
		// if (cmd == 0x0003) {
		// i = 0;
		// }
		// if (i < 0)
		// return;
		// while (i < dat.length) {
		// pid = Tool.builduInt(dat[i], dat[i + 1]);
		// if (pid == 0x0B01) {
		// if ((dat[i + 2] == 0) && (!mMove)) {// 停止转动
		// mMoveImageView.setImageBitmap(Resource.imageDynamoMoff);
		// } else {// 开始转动
		// mMoveImageView.setImageBitmap(Resource.imageDynamoMon);
		// if (pid == 0x0B02) {
		// if ((dat[i + 2] == 0) && (!mOrientation)) {// 正(右)向转动
		// mOrientationImageView
		// .setImageBitmap(Resource.imageDynamoOright);
		// } else {// 反（左）向转动
		// mOrientationImageView
		// .setImageBitmap(Resource.imageDynamoOleft);
		// }
		//
		// } else if (pid == 0x0B03) {
		// if ((dat[i + 2] == 0) && (!mSpeeed)) {// 加速转动
		// mSpeeedImageView
		// .setImageBitmap(Resource.imageDynamoSadd);
		// } else {
		// mSpeeedImageView
		// .setImageBitmap(Resource.imageDynamoSsub);
		// }
		// }
		// // else if (pid == 0x0B02) {
		// // if ((dat[i + 2] == 0) && (!mOrientation)) {// 正(右)向转动
		// // mOrientationImageView
		// // .setImageBitmap(Resource.imageDynamoOright);
		// // } else {// 反（左）向转动
		// // mOrientationImageView
		// // .setImageBitmap(Resource.imageDynamoOleft);
		// // }
		// }
		// // else if (pid == 0x0B03) {
		// // if ((dat[i + 2] == 0) && (!mSpeeed)) {// 加速转动
		// // mSpeeedImageView.setImageBitmap(Resource.imageDynamoSadd);
		// // } else {
		// // mSpeeedImageView.setImageBitmap(Resource.imageDynamoSsub);
		// // }
		// }
		// }
		int pid;
		if (cmd == 0x8001 && dat[0] == 0) {// 读参数响应
			for (int i = 1; i < dat.length; /* i+=2 */) {
				pid = Tool.builduInt(dat[i], dat[i + 1]);
				i += 2;
				// if (pid == 0x0B01) {
				// MEnable0 = dat[i];
				// if (MEnable0 == 0) {
				// mMoveImageView.setImageBitmap(Resource.imageSwitchDisable);
				// } else {
				// mMoveImageView.setImageBitmap(Resource.imageSwitchEnable);
				// }
				// i += 1;
				// } else if(pid == 0x0B02){
				// if(pid == 0x0B02){
				// OEnable0 = dat[i];
				// if (OEnable0 == 0) {
				// mOrientationImageView.setImageBitmap(Resource.imageSwitchDisable);
				// } else {
				// mOrientationImageView.setImageBitmap(Resource.imageSwitchEnable);
				// }
				// i += 1;
				// }else if(pid == 0x0B03){
				// SEnable0 = dat[i];
				// if (SEnable0 == 0) {
				// mSpeeedImageView.setImageBitmap(Resource.imageSwitchDisable);
				// } else {
				// mSpeeedImageView.setImageBitmap(Resource.imageSwitchEnable);
				// }
				// i += 1;
				// }
				// else {
				// return;
				// }
			}
		}
		// if (cmd == 0x8002 && dat[0] == 0) {
		// if (MEnable0 == 0) {
		// mMoveImageView.setImageBitmap(Resource.imageSwitchDisable);
		// } else {
		// mMoveImageView.setImageBitmap(Resource.imageSwitchEnable);
		// }
		// if (OEnable0 == 0) {
		// mOrientationImageView.setImageBitmap(Resource.imageSwitchDisable);
		// } else {
		// mOrientationImageView.setImageBitmap(Resource.imageSwitchEnable);
		// }
		// // if (SEnable0 == 0) {
		// // mSpeeedImageView.setImageBitmap(Resource.imageSwitchDisable);
		// // } else {
		// // mSpeeedImageView.setImageBitmap(Resource.imageSwitchEnable);
		// // }
		// }
	}

	@Override
	void procData(int req, byte[] dat) {
		// TODO Auto-generated method stub

	}

	@Override
	void setdown() {
		// TODO Auto-generated method stub

	}

	@Override
	void setup() {
		/* 读取灯光状态 */
		super.sendRequest(0x0001, new byte[] { 0x0B, 0x01, 0x0B, 0x02, 0x0B,
				0x03 });
	}
}
