package com.embestdkit.zigbee;

import android.util.Log;

class ZbProx implements ZbLink.ConnectListener {
	private static final String TAG = "ZbProx";

	private static final int SOP = 0x02;

	private static final int SUER_CONNECT_REQUEST = 0x010001;

	private static final int SYS_GET_DEVICE_INFO = 0x0014;
	private static final int SYS_APP_MSG = 0x2900; // CMD���� Ϊ��ֵ0x2900

	private static final int ZDO_IEEEADDRESS = 0x0A03;
	private static final int ZDO_NODEDESCRIPTOR = 0x0A04;

	private static final int SYS_RESET_RESPONSE = 0x1005;
	private static final int SYS_PING_RESPONSE = 0x1007;
	private static final int SYS_VERSION_RESPONSE = 0x1008;
	private static final int SYS_GET_DEVICE_INFO_RESPONSE = 0x1014;
	private static final int SYS_APP_MSG_RESPONSE = 0x6900;

	private static final int ZDO_IEEEADDRESS_RESPONSE = 0x1A03;
	private static final int ZDO_NODEDESCRIPTOR_RESPONSE = 0x1A04;

	private static final int ZDO_IEEEADDRESS_RESPONSECB = 0x0A81;
	private static final int ZDO_NODEDESCRIPTOR_RESPONSECB = 0x0A82;

	private static final int CONNECT_OFF = 0;
	private static final int CONNECT_ON = 1;

	private ZbLink mLink; // ZbProx��Ҫ�õ�ZbLink�ķ���������Ҫ��ZbProx������ZbLink����ʵ��
	private int mConnect = CONNECT_OFF;
	/**
	 * �������
	 */
	Request mRequestLock = new Request();

	private ZbProxCallBack mZbProxCB;

	interface ZbProxCallBack {
		void OnconnectCallBack(boolean st);

		void OnDataCallBack(int req, byte[] dat);
	}

	class Request {
		int reqToken;
		int reqResp;
		int reqRespcb;
		Object dat;

		int request(int req, int resp, int respcb, byte[] b) {
			reqToken = req;
			reqResp = resp;
			reqRespcb = respcb;
			if (b != null) {
				return mLink.requestSendData(makeRequest(req, b));
			} else {
				return mLink.requestSendData(makeRequest(req));
			}
		}
	}

	public ZbProx() {
		mLink = new ZbLink(this);
		mZbProxCB = null;
	}

	public ZbProx(ZbProxCallBack cb) {
		mLink = new ZbLink(this);
		mZbProxCB = cb;
	}

	/* ͬ������ */
	public int syncConnect(String host, int port) {
		int r;
		synchronized (mRequestLock) {
			if (mConnect != CONNECT_OFF)
				return -1;
			if (mRequestLock.reqToken != 0)
				return -2;
			r = mLink.requestConnect(host, port);
			if (r != 0)
				return -3;
			mRequestLock.reqToken = SUER_CONNECT_REQUEST;
			try {
				mRequestLock.wait(40 * 1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			mRequestLock.reqToken = 0;
			if (mConnect != CONNECT_ON) {
				return -1;
			}
		}
		return 0;
	}

	public int syncDisConnect() {
		int r;
		synchronized (mRequestLock) {
			if (mConnect != CONNECT_ON)
				return 0;
			r = mLink.requestDisConnect();
			if (r != 0)
				return -1;
			mRequestLock.reqToken = SUER_CONNECT_REQUEST;
			try {
				mRequestLock.wait(40 * 1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			mRequestLock.reqToken = 0;
			if (mConnect != CONNECT_OFF)
				return -1;
		}
		return 0;
	}

	private byte[] wait_data() {
		byte[] b;
		synchronized (mRequestLock) {
			Log.d(TAG, "**********begin wait ****************");
			try {
				mRequestLock.wait(40 * 1000);
			} catch (InterruptedException e) {
				Log.d(TAG,
						"**************wait response data timeout................");
				mRequestLock.reqToken = 0;
				return null;
			}
			Log.d(TAG, "**************** wait return ****************");
			b = (byte[]) mRequestLock.dat;
			mRequestLock.dat = null;
			mRequestLock.reqToken = 0;
		}
		return b;
	}

	byte[] syncRequestSYS_GET_DEVICE_INFO() {
		int ret;
		synchronized (mRequestLock) {
			if (mRequestLock.reqToken != 0)
				return null;
			Log.d(TAG, "syncRequestSYS_GET_DEVICE_INFO");

			ret = mRequestLock.request(SYS_GET_DEVICE_INFO,
					SYS_GET_DEVICE_INFO_RESPONSE, 0, null);
			if (ret != 0) {
				Log.d(TAG, "syncRequestSYS_GET_DEVICE_INFO:fail...");
				return null;
			}
			return wait_data();
		}
	}

	byte[] syncRequestSYS_APP_MSG(int ep, byte[] dat) {
		int ret;
		synchronized (mRequestLock) {
			if (mRequestLock.reqToken != 0)
				return null;
			Log.d(TAG, "syncRequestSYS_APP_MSG");
			// ����һ��byteֵ
			byte[] data = new byte[dat.length + 1];
			data[0] = (byte) (ep & 0xff); // epֵ��С
			for (int i = 0; i < dat.length; i++)
				data[i + 1] = dat[i];
			ret = mRequestLock.request(SYS_APP_MSG, SYS_APP_MSG_RESPONSE,
					0x6980, data);
			if (ret != 0) {
				Log.d(TAG, "syncRequestSYS_GET_DEVICE_INFO:fail...");
				return null;
			}
			return wait_data();
		}
	}

	byte[] syncRequestZDO_IEEEADDRESS(int naddr, int rty, int idx) {
		int ret;
		byte[] req = new byte[4];

		req[0] = (byte) ((naddr >> 8));
		req[1] = (byte) (naddr);
		req[2] = (byte) rty;
		req[3] = (byte) idx;
		synchronized (mRequestLock) {
			if (mRequestLock.reqToken != 0)
				return null;
			Log.d(TAG, "syncRequestZDO_IEEEADDRESS");
			ret = mRequestLock.request(ZDO_IEEEADDRESS,
					ZDO_IEEEADDRESS_RESPONSE, ZDO_IEEEADDRESS_RESPONSECB, req);
			if (ret != 0) {
				Log.d(TAG, "syncRequestZDO_IEEEADDRESS:fail...");
				return null;
			}

			return wait_data();
		}
	}

	byte[] syncRequestZDO_NODEDESCRIPTOR(int dstaddr, int intaddr, int sec) {
		int ret;
		byte[] req = new byte[5];
		req[0] = (byte) ((dstaddr >> 8) & 0xff);
		req[1] = (byte) ((dstaddr >> 0) & 0xff);
		req[2] = (byte) ((intaddr >> 8) & 0xff);
		req[3] = (byte) ((intaddr >> 0) & 0xff);
		req[4] = (byte) (sec & 0xff);

		synchronized (mRequestLock) {
			if (mRequestLock.reqToken != 0)
				return null;
			Log.d(TAG, "syncRequestZDO_NODEDESCRIPTOR");
			ret = mRequestLock.request(ZDO_NODEDESCRIPTOR,
					ZDO_NODEDESCRIPTOR_RESPONSE, ZDO_NODEDESCRIPTOR_RESPONSECB,
					req);
			if (ret != 0) {
				Log.d(TAG, "syncRequestZDO_NODEDESCRIPTOR:fail...");
				return null;
			}
			return wait_data();
		}
	}

	byte[] syncRequestZDO_SIMPLEDESCRIPTOR_REQUEST(int dstAddr,
			int addrOfInterest, int ep) {
		int ret;
		byte[] dat = new byte[6];

		dat[0] = (byte) ((dstAddr & 0xff00) >> 8);
		dat[1] = (byte) (dstAddr & 0xff);
		dat[2] = (byte) ((addrOfInterest & 0xff00) >> 8);
		dat[3] = (byte) (addrOfInterest & 0xff);
		dat[4] = (byte) (ep & 0xff);
		dat[5] = 0;
		Log.d(TAG, "syncRequestZDO_SIMPLEDESCRIPTOR_REQUEST");
		synchronized (mRequestLock) {
			if (mRequestLock.reqToken != 0)
				return null;
			ret = mRequestLock.request(0x0A06, 0x1A06, 0x0A84, dat);
			if (ret != 0) {
				Log.d(TAG, "request error.......");
				return null;
			}
			Log.d(TAG, "bbbbbbbbbbbb. wait.........");
			return wait_data();
		}
	}

	/************************************************/
	public void connect(String host, int port) {
		mLink.requestConnect(host, port);
	}

	public void disConnect() {
		mLink.requestDisConnect();
	}

	/***************************************************************************
	 * SYS_REQUEST AND RESPONSE
	 **************************************************************************/
	/* REQUEST AND RESPONSE */
	/*
	 * public int requestSYS_RESET() { byte[] b = new byte[1]; b[0] = 0; return
	 * mLink.requestSendData(makeRequest(0x0005, b)); } private void
	 * doresponseSYS_RESET(byte[] b) {
	 * 
	 * }
	 * 
	 * public int requestSYS_PING() { return
	 * mLink.requestSendData(makeRequest(0x0007)); }
	 * 
	 * private void doresponseSYS_PING(byte[] b) {
	 * 
	 * }
	 * 
	 * 
	 * public int requestSYS_VERSION() { return
	 * mLink.requestSendData(makeRequest(0x0008)); } private void
	 * doresponseSYS_VERSION(byte[] b) { Log.d(TAG, "sys version:"+
	 * b.toString()); }
	 * 
	 * 
	 * public int requestSYS_GET_DEVICE_INFO() { return
	 * mLink.requestSendData(makeRequest(0x0014)); }
	 */
	public int requestZDO_SIMPLEDESCRIPTOR_REQUEST(int dstAddr,
			int addrOfInterest, int ep) {
		byte[] dat = new byte[6];
		dat[0] = (byte) ((dstAddr & 0xff00) >> 8);
		dat[1] = (byte) (dstAddr & 0xff);
		dat[2] = (byte) ((addrOfInterest & 0xff00) >> 8);
		dat[3] = (byte) (addrOfInterest & 0xff);
		dat[4] = (byte) (ep & 0xff);

		return mLink.requestSendData(makeRequest(0x0A06, dat));
	}

	/*
	 * private void doresponseSYS_GET_DEVICE_INFO(byte b[]) { Log.d(TAG,
	 * "do get sys device info."); synchronized(mRequestLock) { if
	 * (mRequestLock.reqToken == SYS_GET_DEVICE_INFO_RESPONSE) {
	 * mRequestLock.dat = b; mRequestLock.reqToken = 0; mRequestLock.notify(); }
	 * } }
	 */

	/***************************************************************************
	 * ZDO_REQUEST AND RESPONSE
	 **************************************************************************/
	public int requestZDO_IEEEADDRESS(int sa, int ty, int st, int se) {
		byte b[];
		if (ty != 0)
			b = new byte[5];
		else
			b = new byte[4];
		b[0] = (byte) ((sa >> 8) & 0xff);
		b[1] = (byte) (sa & 0xff);
		b[2] = (byte) (ty != 0 ? 1 : 0);
		if (ty != 0) {
			b[3] = (byte) (st & 0xff);
			b[4] = (byte) (se & 0xff);
		} else {
			b[3] = (byte) (se & 0xff);
		}
		return mLink.requestSendData(makeRequest(0x0A03, b));
	}

	/*
	 * private void doresponseZDO_IEEEADDRESS(byte[] b) {
	 * synchronized(mRequestLock) { if (mRequestLock.reqToken ==
	 * ZDO_IEEEADDRESS_RESPONSE) { if (b[0] == 0) { mRequestLock.reqToken =
	 * ZDO_IEEEADDRESS_RESPONSECB; } mRequestLock.notify(); if (b[0] == 0) { try
	 * { mRequestLock.wait(); } catch (InterruptedException e) { // TODO
	 * Auto-generated catch block e.printStackTrace(); } } } } Thread.yield(); }
	 * private void doZDO_IEEEADDRESS_RESPPONSECB(byte[] b) { Log.d(TAG,
	 * "doZDO_IEEEADDRESS_RESPPONSECB...."); synchronized(mRequestLock) { if
	 * (mRequestLock.reqToken == ZDO_IEEEADDRESS_RESPONSECB) {
	 * mRequestLock.reqToken = 0; mRequestLock.dat = b; mRequestLock.notify();
	 * 
	 * } }
	 * 
	 * }
	 * 
	 * private void doZDO_NODEDESCRIPTOR_RESPONSE(byte[] b) { Log.d(TAG,
	 * "doZDO_NODEDESCRIPTOR_RESPONSE...."); synchronized(mRequestLock) { if
	 * (mRequestLock.reqToken == ZDO_NODEDESCRIPTOR_RESPONSE) { if (b[0] == 0) {
	 * mRequestLock.reqToken = ZDO_NODEDESCRIPTOR_RESPONSECB; }
	 * mRequestLock.notify(); if (b[0] == 0 ) { try { mRequestLock.wait(); }
	 * catch (InterruptedException e) { // TODO Auto-generated catch block
	 * e.printStackTrace(); } } } } Thread.yield(); } private void
	 * doZDO_NODEDESCRIPTOR_RESPONSECB(byte[] b) { Log.d(TAG,
	 * "doZDO_NODEDESCRIPTOR_RESPONSECB...."); synchronized(mRequestLock) { if
	 * (mRequestLock.reqToken == ZDO_NODEDESCRIPTOR_RESPONSECB) {
	 * mRequestLock.reqToken = 0; mRequestLock.dat = b; mRequestLock.notify(); }
	 * } }
	 */
	@Override
	public void onConnectData(byte[] b) {
		// int res = (0xff00&(b[1]<<8)) | (0xff&b[2]);
		int res = Tool.builduInt(b[1], b[2]);
		byte[] dat = new byte[Tool.builduInt(b[3])];
		try {
			for (int i = 0; i < dat.length; i++)
				dat[i] = b[i + 4];
		} catch (Exception e) {
			// TODO: handle exception
		}
       	Log.d(TAG, "RECV:" + Tool.byteTostring(b));
		if (calcFCS(b, 1, b.length - 1) != 0) {
			Log.d(TAG, "package error FCS !");
			return;
		}
		synchronized (mRequestLock) {
			if (mRequestLock.reqToken != 0
					&& (mRequestLock.reqResp == res || mRequestLock.reqRespcb == res)) {
				if (mRequestLock.reqRespcb != 0 && mRequestLock.reqResp == res) {// &&
																					// res
																					// ==
																					// mRequestLock.reqRespcb)
																					// {
					Log.d(TAG,
							"**********request response but net response cb......");
				} else {
					mRequestLock.dat = dat;
					mRequestLock.notify();
				}
			} else {
				Log.d(TAG, "unsolite data....");
				if (mZbProxCB != null)
					mZbProxCB.OnDataCallBack(res, dat);
			}
		}
		/*
		 * 
		 * switch (res) { case SYS_RESET_RESPONSE: doresponseSYS_RESET(dat);
		 * break; case SYS_PING_RESPONSE: doresponseSYS_PING(dat); break; case
		 * SYS_VERSION_RESPONSE: doresponseSYS_VERSION(dat); break; case
		 * SYS_GET_DEVICE_INFO_RESPONSE: doresponseSYS_GET_DEVICE_INFO(dat);
		 * break;
		 * 
		 * case ZDO_IEEEADDRESS_RESPONSE: doresponseZDO_IEEEADDRESS(dat); break;
		 * case ZDO_IEEEADDRESS_RESPONSECB: doZDO_IEEEADDRESS_RESPPONSECB(dat);
		 * break; case ZDO_NODEDESCRIPTOR_RESPONSE:
		 * doZDO_NODEDESCRIPTOR_RESPONSE(dat); break; case
		 * ZDO_NODEDESCRIPTOR_RESPONSECB: doZDO_NODEDESCRIPTOR_RESPONSECB(dat);
		 * break;
		 * 
		 * default: Log.d(TAG, String.format("unknow response:%04X", res));
		 * break; }
		 */
	}

	@Override
	public void onConnectStatu(boolean st) {
		synchronized (mRequestLock) {
			if (st) {
				mConnect = CONNECT_ON;
			} else {
				mConnect = CONNECT_OFF;
			}
			if (mRequestLock.reqToken == SUER_CONNECT_REQUEST) {
				mRequestLock.notify();
				mRequestLock.reqToken = 0;
			}
		}
		if (mZbProxCB != null)
			mZbProxCB.OnconnectCallBack(st);
	}

	private byte[] makeRequest(int i) {
		byte[] b = new byte[5];
		b[0] = SOP;
		b[1] = (byte) ((i >> 8) & 0xff);
		b[2] = (byte) (i & 0xff);
		b[3] = 0;
		b[4] = (byte) (b[1] ^ b[2] ^ b[3]);
		return b;
	}

	private byte[] makeRequest(int req, byte[] dat) {
		byte[] b = new byte[dat.length + 5];
		b[0] = SOP;
		b[1] = (byte) ((req >> 8) & 0xff);
		b[2] = (byte) (req & 0xff);
		b[3] = (byte) (dat.length);
		for (int i = 0; i < dat.length; i++) {
			b[4 + i] = dat[i];
		}
		b[b.length - 1] = calcFCS(b, 1, b.length - 2);

		return b;
	}

	/**
	 * ����
	 * 
	 * @param b
	 * @param off
	 * @param len
	 * @return
	 */
	private byte calcFCS(byte[] b, int off, int len) {
		byte fcs = b[off];
		for (int i = off + 1; i < off + len && i < b.length; i++) {
			fcs ^= b[i];
		}
		return fcs;
	}

}
