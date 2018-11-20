package com.embestdkit.zigbee;

import com.embestdkit.R;
import com.embestkit.login.BaseActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.text.style.SuperscriptSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;


public class SwitchDevicePresent extends NodePresent implements OnClickListener {

	/**
	 * ���ݿ�洢
	 *
	 */
	int lightNum;
	int status;
	
	View mInfoView;
	View mConfigView;

	Button mBtnEnable0;
	Button mBtnEnable1;
	Button mBtnEnable2;
	Button mBtnEnable3;

	Button mBtnSetDevAddr;

	EditText mDistAddrEditText;

	ImageView mSwitchImage0;
	ImageView mSwitchImage1;
	ImageView mSwitchImage2;
	ImageView mSwitchImage3;

	int mSwitchEnable0 = 0;// ��ʼ״̬��0�Ͽ�
	int mSwitchEnable1 = 0;
	int mSwitchEnable2 = 0;
	int mSwitchEnable3 = 0;

	int mDistAddr = 0;
	int mNewDistAddr;

	/**
	 * �����豸ģ��
	 * 
	 * @param n
	 */
	SwitchDevicePresent(Node n) {
		super(R.layout.switch_device, n);
		// ��RelativeLayout��ͼ����ĵ��÷���
		mInfoView = super.mView.findViewById(R.id.switchInfoView);
		mConfigView = super.mView.findViewById(R.id.switchConfigView);

		mSwitchImage0 = (ImageView) mConfigView
				.findViewById(R.id.switchImageView0);
		mBtnEnable0 = (Button) mConfigView.findViewById(R.id.switchBtnEnable0);
		mBtnEnable0.setOnClickListener(this);

		mSwitchImage1 = (ImageView) mConfigView
				.findViewById(R.id.switchImageView1);
		mBtnEnable1 = (Button) mConfigView.findViewById(R.id.switchBtnEnable1);
		mBtnEnable1.setOnClickListener(this);

		mSwitchImage2 = (ImageView) mConfigView
				.findViewById(R.id.switchImageView2);
		mBtnEnable2 = (Button) mConfigView.findViewById(R.id.switchBtnEnable2);
		mBtnEnable2.setOnClickListener(this);

		mSwitchImage3 = (ImageView) mConfigView
				.findViewById(R.id.switchImageView3);
		mBtnEnable3 = (Button) mConfigView.findViewById(R.id.switchBtnEnable3);
		mBtnEnable3.setOnClickListener(this);

		// mBtnSetDevAddr =
		// (Button)mConfigView.findViewById(R.id.switchBtnSetCtrlDevAddr);
		// mBtnSetDevAddr.setOnClickListener(this);
		// mDistAddrEditText =
		// (EditText)mConfigView.findViewById(R.id.switchCtrlDevAddrEditText);

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
	
	public  void uploadData(String data) {
		// TODO �Զ����ɵķ������
		System.out.println(userId);
		if(!TextUtils.isEmpty(userId)){
			data=data+"&userId="+userId;
			deviceData(data,"relay_");
		}
	}

	@Override
	public void onClick(View v) {
		if (v == this.mBtnEnable0) {// ��һ��ƣ����ã��رգ���ת
			lightNum=1;
			if (mSwitchEnable0 == 0) {
				super.sendRequest(0x0002, new byte[] { 0x03, 0x02, 0x01 });// ����
				mSwitchEnable0 = 0x01;
				
				status=1;
				String data="deviceNum="+lightNum+"&status="+status;
				uploadData(data);
				
			} else {
				super.sendRequest(0x0002, new byte[] { 0x03, 0x02, 0x00 });
				mSwitchEnable0 = 0x00;
				
				status=0;
				String data="deviceNum="+lightNum+"&status="+status;
				uploadData(data);
			}
		} else if (v == this.mBtnEnable1) {// �ڶ���ƣ����ã��رգ���ת
			lightNum=2;
			if (mSwitchEnable1 == 0) {
				super.sendRequest(0x0002, new byte[] { 0x03, 0x03, 0x01 });
				mSwitchEnable1 = 0x01;
				
				status=1;
				String data="deviceNum="+lightNum+"&status="+status;
				uploadData(data);
			} else {
				super.sendRequest(0x0002, new byte[] { 0x03, 0x03, 0x00 });
				mSwitchEnable1 = 0x00;
				
				status=0;
				String data="deviceNum="+lightNum+"&status="+status;
				uploadData(data);
			}
		} else if (v == this.mBtnEnable2) {// ������ƣ����ã��رգ���ת
			lightNum=3;
			if (mSwitchEnable2 == 0) {
				super.sendRequest(0x0002, new byte[] { 0x03, 0x04, 0x01 });
				mSwitchEnable2 = 0x01;
				
				status=1;
				String data="deviceNum="+lightNum+"&status="+status;
				uploadData(data);
			} else {
				super.sendRequest(0x0002, new byte[] { 0x03, 0x04, 0x00 });
				mSwitchEnable2 = 0x00;
				
				status=0;
				String data="deviceNum="+lightNum+"&status="+status;
				uploadData(data);
			}
		} else if (v == this.mBtnEnable3) {// ������ƣ����ã��رգ���ת
			lightNum=4;
			if (mSwitchEnable3 == 0) {
				super.sendRequest(0x0002, new byte[] { 0x03, 0x05, 0x01 });
				mSwitchEnable3 = 0x01;
				
				status=1;
				String data="deviceNum="+lightNum+"&status="+status;
				uploadData(data);
			} else {
				super.sendRequest(0x0002, new byte[] { 0x03, 0x05, 0x00 });
				mSwitchEnable3 = 0x00;
				
				status=0;
				String data="deviceNum="+lightNum+"&status="+status;
				uploadData(data);
			}
		} else if (v == mBtnSetDevAddr) {
			mNewDistAddr = Integer.parseInt(mDistAddrEditText.getText()
					.toString());
			// if (mNewDistAddr != mDistAddr) {
			super.sendRequest(0x0002, new byte[] { 0x03, 0x11,
					(byte) (mNewDistAddr >> 8), (byte) mNewDistAddr });
			mDistAddr = mNewDistAddr;
			// }
		}
	}

	@Override
	void procAppMsgData(int addr, int cmd, byte[] dat) {
		int pid;
		if (cmd == 0x8001 && dat[0] == 0) {// ��������Ӧ
			for (int i = 1; i < dat.length; /* i+=2 */) {
				pid = Tool.builduInt(dat[i], dat[i + 1]);
				i += 2;
				if (pid == 0x0302) {
					mSwitchEnable0 = dat[i];
					if (mSwitchEnable0 == 0) {
						mSwitchImage0.setImageBitmap(Resource.imageLightOff);
					} else {
						mSwitchImage0.setImageBitmap(Resource.imageLightOn);
					}
					i += 1;
				} else if (pid == 0x0303) {
					mSwitchEnable1 = dat[i];
					if (mSwitchEnable1 == 0) {
						mSwitchImage1.setImageBitmap(Resource.imageLightOff);
					} else {
						mSwitchImage1.setImageBitmap(Resource.imageLightOn);
					}
					i += 1;
				} else if (pid == 0x0304) {
					mSwitchEnable2 = dat[i];
					if (mSwitchEnable2 == 0) {
						mSwitchImage2.setImageBitmap(Resource.imageLightOff);
					} else {
						mSwitchImage2.setImageBitmap(Resource.imageLightOn);
					}
					i += 1;
				} else if (pid == 0x0305) {
					mSwitchEnable3 = dat[i];
					if (mSwitchEnable3 == 0) {
						mSwitchImage3.setImageBitmap(Resource.imageLightOff);
					} else {
						mSwitchImage3.setImageBitmap(Resource.imageLightOn);
					}
					i += 1;
				}
				// else if (pid == 0x0311) {
				// mDistAddr = Tool.builduInt(dat[i], dat[i+1]);
				// mDistAddrEditText.setText(String.format("%d", mDistAddr));
				//
				// i += 2;
				// }
				else {
					return;
				}
			}
		}
		if (cmd == 0x8002 && dat[0] == 0) {
			if (mSwitchEnable0 == 0) {
				mSwitchImage0.setImageBitmap(Resource.imageLightOff);
			} else {
				mSwitchImage0.setImageBitmap(Resource.imageLightOn);
			}
			if (mSwitchEnable1 == 0) {
				mSwitchImage1.setImageBitmap(Resource.imageLightOff);
			} else {
				mSwitchImage1.setImageBitmap(Resource.imageLightOn);
			}
			if (mSwitchEnable2 == 0) {
				mSwitchImage2.setImageBitmap(Resource.imageLightOff);
			} else {
				mSwitchImage2.setImageBitmap(Resource.imageLightOn);
			}
			if (mSwitchEnable3 == 0) {
				mSwitchImage3.setImageBitmap(Resource.imageLightOff);
			} else {
				mSwitchImage3.setImageBitmap(Resource.imageLightOn);
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

	}

	@Override
	void setup() {
		// TODO Auto-generated method stub
		// super.sendRequest(0x0001, new byte[]{0x03,0x11, 0x03,0x21,});
		super.sendRequest(0x0001, new byte[] { 0x03, 0x02, 0x03, 0x03, 0x03,
				0x04, 0x03, 0x05 });
	}
}
