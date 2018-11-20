package com.embestdkit.zigbee;

import com.embestdkit.zigbee.ZbProx.ZbProxCallBack;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

public class ZbThread extends Thread implements ZbProxCallBack {
	private static final String TAG = "ZbThread";

	static final int MSG_CONNECT_STATUS = 1;
	static final int MSG_NEW_NETWORK = 2;
	static final int MSG_CONNECT_DATA = 3;
	static final int MSG_ENDPOINT_INFO = 4;
	private final Object mLockInit = new Object();

	static final int MSG_GET_APP_MSG = 5;

	static final int REQUEST_SEARCH_NETWORK = 0x0100;
	private ZbProx mProx = new ZbProx(this);

	static final int REQUEST_APP_MESSAGE = 0x0300;
	// mMyHandler��һ���������
	private MyWorkerHandler mMyHandler;
	private Handler mMainHandler;

	static final int REQUEST_NODE_ENDPOINT_INFO = 0x0200;

	Node mTree;

	void requestConnect(String host, int port) {
		mProx.connect(host, port);
	}

	void requestDisConnect() {
		mProx.disConnect();
	}

	/**
	 * ���͡������ѯ���硱��Ϣ
	 */
	void requestSerachNetWrok() {
		Message msg = Message.obtain();
		msg.what = REQUEST_SEARCH_NETWORK;
		mMyHandler.sendMessage(msg);
	}

	class MyWorkerHandler extends Handler {
		private static final String TAG = "MyWorkHandler";
		MyWorkerHandler(Looper looper) {
			super(looper);
		}

		@Override
		public void handleMessage(Message msg) { // ״̬����msg����
			switch (msg.what) {
			case REQUEST_SEARCH_NETWORK: // ��Ӧ�������ѯ���硱��Ϣ
				doSearchNetWork();
				break;
			case REQUEST_NODE_ENDPOINT_INFO:
				doGetNodeEndPointInfo(msg.arg1, msg.arg2);
				break;
			case REQUEST_APP_MESSAGE:
				doAppMessage(msg.arg1, (byte[]) msg.obj);
				break;
			}
		}
	}

	/*
	 * void requestNodeEndPointInfo(Node n, int ep) { Message msg =
	 * Message.obtain(); msg.what = REQUEST_NODE_ENDPOINT_INFO; msg.arg1 =
	 * n.mNetAddr; msg.arg2 = ep; mMyHandler.sendMessage(msg);
	 * 
	 * }
	 */

	void requestAppMessage(int ep, byte[] dat) { // msg����һ���н�
		Message msg = Message.obtain();
		msg.what = REQUEST_APP_MESSAGE;
		msg.arg1 = ep;
		msg.obj = dat;
		mMyHandler.sendMessage(msg);
	}

	public ZbThread(Handler hd) {
		super();
		mMainHandler = hd;
		start();
		synchronized (mLockInit) {
			while (mMyHandler == null) {
				try {
					mLockInit.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	public void run() // �̴߳��������
	{
		Looper.prepare();
		synchronized (mLockInit) {
			mMyHandler = new MyWorkerHandler(Looper.myLooper());
			mLockInit.notifyAll();
		}
		Looper.loop();
	}

	@Override
	public void OnconnectCallBack(boolean st) {
		Message msg = Message.obtain();
		msg.what = ZbThread.MSG_CONNECT_STATUS;
		msg.arg1 = st ? 1 : 0;
		mMainHandler.sendMessage(msg);
	}

	@Override
	public void OnDataCallBack(int req, byte[] dat) {
		Log.d(TAG, "�յ���������" + Tool.byteTostring(dat));
		Message msg = Message.obtain();
		msg.what = ZbThread.MSG_CONNECT_DATA;
		msg.arg1 = req;
		msg.obj = dat;
		mMainHandler.sendMessage(msg);
	}

	/**
	 * //����ZigBee
	 * @param pa
	 * @param cli
	 */
	private void buildNetWork(Node pa, int[] cli) {
		for (int i = 0; i < cli.length; i++) {
			/* get child i info */
			try {
				Thread.currentThread();
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			byte[] ninfo = mProx.syncRequestSYS_APP_MSG(2,
					new byte[] { (byte) (cli[i] >> 8),
							(byte) cli[i], // addr
							0x00,
							0x01, // cmd
							0x00, 0x01, 0x00, 0x02, 0x00, 0x05, 0x00, 0x14,
							0x00, 0x15 });
			if (ninfo == null || ninfo.length < 29) {
				Log.d(TAG, "**** get node " + cli[i] + " info fail.");
				continue;
			}
			int tmp, off = 0;
			tmp = Tool.builduInt(ninfo[off], ninfo[off + 1]); // addr
			if (tmp != cli[i]) {
				Log.d(TAG, "net add is not equl...");
				continue;
			}
			off += 2;
			tmp = Tool.builduInt(ninfo[off], ninfo[off + 1]); // cmd
			if (tmp != 0x8001) {
				Log.d(TAG, "response cmd not euql...");
				continue;
			}
			off += 2;
			if (ninfo[off] != 0) { // read status
				Log.d(TAG, "read status is not 0");
				continue;
			}
			off += 1;
			Node nd = new Node(cli[i], Node.ZB_NODE_TYPE_ENDDEVICE);
			int[] childs = {};

			while (off < ninfo.length) {
				tmp = Tool.builduInt(ninfo[off], ninfo[off + 1]);
				off += 2;
				switch (tmp) {
				case 0x0001: // hard ver
					nd.mHardVer = Tool.builduInt(ninfo[off], ninfo[off + 1]);
					off += 2;
					break;
				case 0x0002:
					nd.mSoftVer = Tool.builduInt(ninfo[off], ninfo[off + 1]);
					off += 2;
					break;
				case 0x0005:
					nd.mDevType = ninfo[off];
					off += 1;
					break;
				case 0x0014:
					for (int j = 0; j < 8; j++) {
						nd.mIEEEAddr[j] = ninfo[off + j];
					}
					off += 8;

					break;
				case 0x0015:
					int assocCnt = ninfo[off];
					off += 1;
					if (assocCnt != 0) {
						nd.mNodeType = Node.ZB_NODE_TYPE_ROUTER;
						int[] nli = new int[assocCnt];
						for (int j = 0; j < assocCnt; j++) {
							nli[j] = Tool.builduInt(ninfo[off], ninfo[off + 1]);
							off += 2;
						}
						childs = nli;
					}
					break;
				}
			}
			pa._childNode.add(nd);
			// //////mTree ��ô��ʼ����������������������������������
			Top.DrawTop(mTree);
			Message msg = Message.obtain();
			msg.what = MSG_NEW_NETWORK;
			msg.arg1 = 1;
			mMainHandler.sendMessage(msg);
			buildNetWork(nd, childs);
		}
	}

	/**
	 * ���û���һ�δ򿪳��򣬻��ǴӲ˵���ѡ��������ʱ��Zigbee ���� TOPͼ����ģ�����ȼ� ���������ӣ��������������ӵ� Zigbee
	 * �������غ����ȷ��ͻ�ȡЭ�����ڵ���Ϣָ���ȡ�� Э�����ڵ���Ϣ��֮��Э�����ڵ�ͨ����ͼ�ӳ�������Ļ�ϰ�Э������ʾ����
	 */
	private void doSearchNetWork() {
		Top.DistroyTree(mTree);
		// devinfo = mProx.syncRequestSYS_GET_DEVICE_INFO();
		// ninfo!=null���� ninfo �б����ȡ����Э������Ϣ����Ϣ��
		// �����ȡЭ������Ϣʧ�ܣ�zigbee ������������
		byte[] ninfo = mProx.syncRequestSYS_APP_MSG(2, new byte[] {
				(byte) (0 >> 8), (byte) 0, // addr
				0x00, 0x01, // cmd
				0x00, 0x01, 0x00, 0x02, 0x00, 0x05, 0x00, 0x14, 0x00, 0x15 });
		if (ninfo == null || ninfo.length < 29) {
			/* get devinfo fail */
			Log.e(TAG, "Can't get the root device info.");// Э����
			Message msg = Message.obtain();
			msg.what = MSG_NEW_NETWORK;
			msg.arg1 = -1;
			mMainHandler.sendMessage(msg);
			return;
		}

		int tmp, off = 0;
		tmp = Tool.builduInt(ninfo[off], ninfo[off + 1]); // addr
		if (tmp != 0) {
			Log.d(TAG, "net add is not equl...");
			return;
		}
		off += 2;
		tmp = Tool.builduInt(ninfo[off], ninfo[off + 1]); // cmd
		if (tmp != 0x8001) {
			Log.d(TAG, "response cmd not euql...");
			return;
		}
		off += 2;
		if (ninfo[off] != 0) { // read status
			Log.d(TAG, "read status is not 0");
			return;
		}
		off += 1;
		Node nd = new Node(0, Node.ZB_NODE_TYPE_ENDDEVICE);
		int[] childs = {};

		while (off < ninfo.length) {
			tmp = Tool.builduInt(ninfo[off], ninfo[off + 1]);
			off += 2;
			switch (tmp) {
			case 0x0001: // hard ver
				nd.mHardVer = Tool.builduInt(ninfo[off], ninfo[off + 1]);
				off += 2;
				break;
			case 0x0002:
				nd.mSoftVer = Tool.builduInt(ninfo[off], ninfo[off + 1]);
				off += 2;
				break;
			case 0x0005:
				nd.mDevType = ninfo[off];
				off += 1;
				break;
			case 0x0014:
				for (int j = 0; j < 8; j++) {
					nd.mIEEEAddr[j] = ninfo[off + j];
				}
				off += 8;

				break;
			case 0x0015:
				int assocCnt = ninfo[off];
				off += 1;
				if (assocCnt != 0) {
					nd.mNodeType = Node.ZB_NODE_TYPE_ROUTER;
					int[] nli = new int[assocCnt];
					for (int j = 0; j < assocCnt; j++) {
						nli[j] = Tool.builduInt(ninfo[off], ninfo[off + 1]);
						off += 2;
					}
					childs = nli;
				}
				break;
			}
		}
		// ����Э�����ڵ�
		nd.mNodeType = Node.ZB_NODE_TYPE_COORDINATOR;
		mTree = nd;

		Log.d(TAG, "Build tree node net addr:" + mTree.mNetAddr);

		/* notify ui */
		// ����Э�����ڵ㲢֪ͨ���߳�����Ļ����ʾ����
		Top.DrawTop(mTree);
		Message msg = Message.obtain();
		msg.what = MSG_NEW_NETWORK;
		msg.arg1 = 1;
		mMainHandler.sendMessage(msg);
		/**
		 * ���ҵ�Э�����󣬳���ͨ��������Э����Э����ֱ�����ӵ���ؽڵ㣬 Ȼ��ݹ��������������������� ���粢���Ƴ� Zigbee ����� TOP
		 * �ṹ�� mTree �������Э�������
		 */
		buildNetWork(mTree, childs);

		/* notify ui */
		Top.DrawTop(mTree);
		msg = Message.obtain();
		msg.what = MSG_NEW_NETWORK;
		msg.arg1 = 0;
		mMainHandler.sendMessage(msg);
	}

	private void doGetNodeEndPointInfo(int addr, int ep) {
		byte[] info;
		info = mProx.syncRequestZDO_SIMPLEDESCRIPTOR_REQUEST(addr, addr, ep);
		Message msg = Message.obtain();
		msg.what = MSG_ENDPOINT_INFO;
		if (info == null || info[0] != 0) {
			msg.arg1 = 1;
		} else {
			msg.arg1 = 0;
			msg.obj = info;
		}
		mMainHandler.sendMessage(msg);
	}

	/**
	 * ������Ϣ
	 * 
	 * @param ep
	 * @param dat
	 */
	private void doAppMessage(int ep, byte[] dat) {
		byte[] info;
		info = mProx.syncRequestSYS_APP_MSG(ep, dat);
		if (info != null) {
			Log.d(TAG, "doAppMessage:" + Tool.byteTostring(info));
		} else {
			Log.d(TAG, "appMessage request timeout...");
		}
		Message msg = Message.obtain();
		msg.what = MSG_GET_APP_MSG;
		msg.obj = info;

		mMainHandler.sendMessage(msg);
	}
}
