package com.embestdkit.zigbee;

import com.embestdkit.R;
import com.embestkit.login.BaseActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

public class ZigBeeTool extends BaseActivity implements
		MainView.OnNodeClickListener
/* implements OnTouchListener */{
	static final String TAG = "ZigBee";
	static final String PREFS_NAME = "ZigBeeConfig";
	static final String SAVE_IPADDR = "zbGetWayIP";

	int mConnectStatus = 0;
	int mSearchingZbNet = 0;

	ViewFlipper mViewFilpper;

	View mViewTop;

	/**
	 * �������
	 */
	NodePresent mNodePresent;
	Object mLock = new Object();
	MainView mMainView;
	ProgressDialog mDlg;
	UiHandler handler;
	// Node mFocusNode;

	private static ZigBeeTool mInstance;
	// ��SharedPreferences�洢����
	SharedPreferences mSaveVar;
	// ��¼ name + ":" + ip
	String mIpAddr = "";

	String mZigBeeGetWay;

	ZbThread mZbThread;

	boolean mNeedAutoConnect = false;

	static ZigBeeTool getInstance() {
		return mInstance;
	}

	static int sScreenHeight;
	static int sScreenWidth;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// ��ô��ڽ������
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		Log.i(TAG, "screen size : " + dm.widthPixels + " X " + dm.heightPixels);
		sScreenHeight = dm.heightPixels;
		sScreenWidth = dm.widthPixels;

		mInstance = this;
		// �ȸ�Activityע��������������
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);

		setContentView(R.layout.main);
		mViewFilpper = (ViewFlipper) findViewById(R.id.viewFlipper1);
		// ��layout.main.xml������mViewTop
		mViewTop = findViewById(R.id.top);

		// ��layout.top.xml������MainView
		mMainView = (MainView) mViewTop.findViewById(R.id.MainView);
		mMainView.setOnNodeClickListener(this);

		mDlg = new ProgressDialog(this);
		mDlg.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		mDlg.setMessage("���ڻ�ȡ�����Ϣ");
		mDlg.show();
		// mDlg.setCancelable(true);

		// �������붯��
		mViewFilpper.setInAnimation(AnimationUtils.loadAnimation(
				getApplicationContext(), android.R.anim.slide_in_left));
		// �����г�����
		mViewFilpper.setOutAnimation(AnimationUtils.loadAnimation(
				getApplicationContext(), android.R.anim.slide_out_right));

		UiHandler hd = new UiHandler();

		mZbThread = new ZbThread(hd);
		// ���һ�� getSharedPreferences ����
		mSaveVar = getSharedPreferences(PREFS_NAME, 0);
		// ���û�о���Ĭ��ֵ127.0.0.1
		mZigBeeGetWay = mSaveVar.getString(SAVE_IPADDR, "127.0.0.1");

		connectToserver();
		// �����ڲ��ڣ�ʵ�����ķ���
		Tool.IterateLocalIpAddress(new Tool.IterateIpAddressListener() {
			@Override
			public void iterate(String name, String ip) {
				if (mIpAddr.length() != 0) {
					mIpAddr += "\n";// ����
				}
				mIpAddr += name + ":" + ip;// ƴ��
			}
		});
	}

	private void connectToserver() {
		mConnectStatus = 1;
		mZbThread.requestConnect(mZigBeeGetWay, 8320); // 10.0.2.2
														// liren408990992.gicp.net
		setTitle("�������ӵ�ZigBee���� -- " + this.mZigBeeGetWay);
		setProgressBarIndeterminateVisibility(true);
	}

	/**
	 * ������Ϣ
	 * 
	 * @author Administrator
	 * 
	 */
	class UiHandler extends Handler {
		@SuppressLint("HandlerLeak")
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case ZbThread.MSG_CONNECT_STATUS:
				mDlg.cancel();
				onConnectChange(msg.arg1);
				break;
			case ZbThread.MSG_NEW_NETWORK:
				mDlg.cancel();
				onMsgNetwork(msg.arg1);
				break;
			case ZbThread.MSG_CONNECT_DATA:
				mDlg.cancel();
				byte[] dat = (byte[]) msg.obj;
				if (msg.arg1 == 0x6980) { /* app msg */
					onResponseMSG_GET_APP_MSG(dat);
					System.out.println("��ȷ���ӵ�����"+Tool.str16toStr(dat.toString()));
				}
				break;
			case ZbThread.MSG_GET_APP_MSG:
				mDlg.cancel();
				byte[] dat2 = (byte[]) msg.obj;
				onResponseMSG_GET_APP_MSG(dat2);
				System.out.println("��ȷ�յ�������"+dat2);
				break;
			}
		}
	}

	private void onConnectChange(int st) {
		Log.d(TAG, "onConnectChange status : " + st);
		if (st == 0) {
			/* connect off */
			mConnectStatus = 0;
			setTitle("����ZigBee����ʧ�� -- " + this.mZigBeeGetWay);
			setProgressBarIndeterminateVisibility(false);
			if (mNeedAutoConnect) {
				connectToserver();
			}
		} else {
			/* connect on */
			mConnectStatus = 2;
			setTitle("��������ZigBee����...");
			setProgressBarIndeterminateVisibility(true);
			mSearchingZbNet = 1;
			mZbThread.requestSerachNetWrok();
		}
	}

	private void onMsgNetwork(int st) {
		mSearchingZbNet = 0;
		if (st < 0) {
			setTitle("û���ҵ�Э����");
			setProgressBarIndeterminateVisibility(false);
			Toast.makeText(ZigBeeTool.this, "��������ʧ��", Toast.LENGTH_LONG).show();
			return;
		}
		if (st == 0) {
			/* finish */
			setTitle("�������ۺ���ʾʵ��");
			setProgressBarIndeterminateVisibility(false);
			return;
		}
		// st > 0
		LayoutParams p = mMainView.getLayoutParams();
		int w = sScreenWidth, h = sScreenHeight;
		if (Top.bm.getHeight() > h)
			h = Top.bm.getHeight();
		if (Top.bm.getWidth() > w)
			w = Top.bm.getWidth();

		p.height = h;
		p.width = w;
		mMainView.setLayoutParams(p);
		mMainView.invalidate();
	}

	/**
	 * ��Ӧ ����������
	 * 
	 * @param dat
	 */
	private void onResponseMSG_GET_APP_MSG(byte[] dat) {
		if (dat == null)
			return;
		Log.d(TAG, "APP MSG :" + Tool.byteTostring(dat));
		if (dat == null || dat.length <= 4) {
			Log.d(TAG, "APP MSG timeout or package error.");
			return;
		}
		// int addr = 0xffff & ((dat[0]<<8) | (0xff&dat[1]));
		int addr = Tool.builduInt(dat[0], dat[1]); // ��dat����ȡaddr��Ϣ
		// int cmd = 0xffff & ((dat[2]<<8) | (0xff&dat[3]));
		int cmd = Tool.builduInt(dat[2], dat[3]); // ��dat����ȡcmd��Ϣ

		byte[] data = new byte[dat.length - 4];
		for (int i = 0; i < data.length; i++)
			data[i] = dat[4 + i]; // ���ֽ����飬��ԭ�������ݻ�ԭ
		synchronized (mLock) {
			if (mNodePresent != null) {
				mNodePresent.procAppMsgData(addr, cmd, data);
				mNodePresent.userId=spf.getString("userId", "");
			}
		}
	}

	@Override
	public void onStop() {
		Log.d(TAG, "onStop...");
		mZbThread.requestDisConnect();
		super.onStop();
	}

	@Override
	public void onRestart() {
		Log.d(TAG, "onRestart...");
		connectToserver();
		super.onRestart();
	}

	/************************************************************************************
	 ****************** Activity ��������*************************************************
	 ***********************************************************************************/
	@Override
	public void onResume() {
		Log.d(TAG, "onResume....");
		super.onResume();
	}

	@Override
	public void onPause() {
		Log.d(TAG, "onPause....");
		super.onPause();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			synchronized (mLock) {
				if (mNodePresent != null) {
					mViewFilpper.showPrevious();
					mViewFilpper.removeView(mNodePresent.mView);
					this.setTitle("�ҵ��豸");
					mNodePresent.setdown();
					mNodePresent = null;
					return true;
				}
			}
			mZbThread.requestDisConnect();
			this.finish();
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onNodeClick(Node n) {
		Log.d(TAG, "do node click... " + n);
		synchronized (mLock) {
			if (mNodePresent != null) {
				return;
			}
			// ������ı��� ����һ�����������ʵ��
			mNodePresent = NodePresent.FactoryCreateInstance(n);
			mNodePresent.setup();
			mViewFilpper.addView(mNodePresent.mView);
			// Manually shows the next child.
			mViewFilpper.showNext();
			this.setTitle(Node.getDeviceTypeString(mNodePresent.mNode));
		}
	}

	/*****************************************************************
	 * 
	 * �˵�����
	 */
	static final int _COMMAND_SET_ID = Menu.FIRST + 1;
	static final int _COMMAND_SEARCH_ID = Menu.FIRST + 2;
	static final int _COMMAND_ABORT_ID = Menu.FIRST + 3;

	// �̳з�������Ӳ˵����Ӳ˵���
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(Menu.NONE, _COMMAND_SET_ID, 1, "����");
		menu.add(Menu.NONE, _COMMAND_SEARCH_ID, 2, "��������");
		menu.add(Menu.NONE, _COMMAND_ABORT_ID, 3, "����");
		return true;
	}

	/**
	 * �˵����� �Ӳ˵���ѡ��������ʱ��Zigbee ���� TOPͼ����ģ�����ȼ� ����������
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case _COMMAND_SET_ID: {
			LayoutInflater factory = LayoutInflater.from(this);
			// �õ��Զ���Ի��� ��settingdlg.����Ի�����ϲ��֣�
			final View settingView = factory.inflate(R.layout.settingdlg, null);
			// �����ZigBee���ص�ַ��192.192.192.111
			final EditText etIp = (EditText) settingView
					.findViewById(R.id.etipaddr);
			etIp.setText(this.mZigBeeGetWay);
			Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("ZigBee����");// ���ñ���
			builder.setView(settingView);
			builder.setPositiveButton("ȷ��",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface arg0, int arg1) {
							mZigBeeGetWay = etIp.getText().toString();
							if (mConnectStatus != 0) {
								mNeedAutoConnect = true;
								mZbThread.requestDisConnect();
							} else {
								connectToserver();
							}
							SharedPreferences.Editor editor = mSaveVar.edit();
							// �������������ip��ַ
							editor.putString(SAVE_IPADDR, mZigBeeGetWay);
							editor.commit();
						}// ���ü����¼�
					});
			builder.setNegativeButton("ȡ��",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
						}
					});
			builder.create().show();
			break;
		}
		case _COMMAND_SEARCH_ID:
			if (mConnectStatus == 0) {
				connectToserver();
			} else if (mConnectStatus == 2) {
				if (mSearchingZbNet == 0) {
					setTitle("��������ZigBee����...");
					setProgressBarIndeterminateVisibility(true);
					mSearchingZbNet = 1;
					// �����ѯ����
					mZbThread.requestSerachNetWrok();
				} else {
					Toast.makeText(ZigBeeTool.this, "�����������磬���Ե�...",
							Toast.LENGTH_LONG).show();
				}
			}
			break;
		// ��ʾ��ǰset���õ�ip��ַ
		case _COMMAND_ABORT_ID: {
			LayoutInflater factory = LayoutInflater.from(this);
			// �õ��Զ���Ի���
			final View DialogView = factory.inflate(R.layout.abort, null);
			TextView txIpAddr = (TextView) DialogView
					.findViewById(R.id.abortIpAddr);
			txIpAddr.setText(mIpAddr);
			Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("ZigBee����");// ���ñ���
			builder.setView(DialogView);
			builder.setPositiveButton("ȷ��",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface arg0, int arg1) {
						}
					});
			builder.create().show();
			break;
		}
		}
		return false;
	}
	
	public Handler getMyhandler(){
		return handler;
	}
}
