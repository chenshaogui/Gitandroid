package com.embestdkit.zigbee;

import java.util.LinkedList;
import java.util.List;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

/*
 * 单个节点的数据表示 
 */
public class Node {
	private static final String TAG = "Zbnode";

	/* 结点类型,协调器，路由，设备 */
	final static int ZB_NODE_TYPE_COORDINATOR = 0;
	final static int ZB_NODE_TYPE_ROUTER = 1;
	final static int ZB_NODE_TYPE_ENDDEVICE = 2;

	/* 设备类型 */
	static final int DEV_NONE = 0;// 未知
	static final int DEV_TEMPSENSOR = 1;// 温湿度
	static final int DEV_LIGHT = 2;// 光敏设备
	static final int DEV_SWITCH = 3;// 继电器设备
	static final int DEV_INFRAREDSENSOR = 4;// 人体红外
	static final int DEV_GASSENSOR = 5;// 可燃气体
	static final int DEV_VIBRATE = 6;// 振动
	static final int DEV_ALCOHOL = 7;// 酒精
	static final int DEV_RAIN = 8;// 雨滴
	static final int DEV_HALL = 9;// 霍尔
	static final int DEV_THREEACC = 10;// 三轴加速度
	static final int DEV_DYNAMO = 11;// 电机
	static final int DEV_PRESSURE = 12;// 气体压力
	static final int DEV_DECIBLE = 13;// 分贝
	static final int DEV_FLAME = 14;// 火焰
	static final int DEV_ULTRASONIC = 15;// 超声波
	static final int DEV_IRREMOTE = 16;// 红外遥控
	static final int DEV_RFID = 17;// RFID射频识别

	List<Node> _childNode; // 孩子结点链表，该节点下面包含结点

	int mHardVer; // 硬件商
	int mSoftVer; // 软件商

	byte[] mIEEEAddr; // M A C 地 址 ,字节数组
	int mNetAddr; // 网络地址

	int mNodeType; // 节点类型
	int mDevType; // 设备类型

	// EndPoint[] mEndPoints;

	/* 显示相关数据 */
	// Bitmap mBmp; //结点对应的显示图标
	int mLeft, mTop, mWidth, mHeight; // 画图时结点坐在的位置
	int mDeepth; // 结点与子树的深度,包括顶点有多少层

	Node(int netaddr) {
		mNetAddr = netaddr;
		mIEEEAddr = new byte[8]; // 包含8个字节的字节数组
		_childNode = new LinkedList<Node>();
	}

	Node(int netaddr, int nodetype) {
		mNetAddr = netaddr;
		mNodeType = nodetype;
		mIEEEAddr = new byte[8]; // 包含8个字节的字节数组
		_childNode = new LinkedList<Node>();
	}

	// 获得结点字符串描述
	static String getNodeTypeString(Node n) {
		switch (n.mNodeType) {
		case Node.ZB_NODE_TYPE_COORDINATOR:
			return "协调器";
		case Node.ZB_NODE_TYPE_ROUTER:
			return "路由器";
		default:
			return "终端结点";
		}
	}

	// 获得设备字符串描述
	static String getDeviceTypeString(Node n) {
		switch (n.mDevType) {
		case Node.DEV_TEMPSENSOR:
			return "温度传感器";
		case Node.DEV_LIGHT:
			return "光敏传感器";
		case Node.DEV_SWITCH:
			return "继电器设备";
		case Node.DEV_INFRAREDSENSOR:
			return "人体红外传感器";
		case Node.DEV_GASSENSOR:
			return "可燃气体传感器";
		case Node.DEV_VIBRATE:
			return "振动传感器";
		case Node.DEV_THREEACC:
			return "三轴加速度传感器";
		case Node.DEV_PRESSURE:
			return "气压传感器";
		case Node.DEV_RAIN:
			return "雨滴传感器";
		case Node.DEV_ALCOHOL:
			return "酒精传感器";
		case Node.DEV_FLAME:
			return "火焰传感器";
		case Node.DEV_HALL:
			return "霍尔传感器";
		case Node.DEV_DECIBLE:
			return "分贝传感器";
		case Node.DEV_ULTRASONIC:
			return "超声波传感器";
		case Node.DEV_DYNAMO:
			return "电机传感器";
		case Node.DEV_IRREMOTE:
			return "红外遥控传感器";
		case Node.DEV_RFID:
			return "RFID射频识别设备";
		default:
			return "未知设备";
		}
	}

	static String getHardVer(Node n) {
		return String.format("%02X.%02X", (n.mHardVer >> 8) & 0xff,
				n.mHardVer & 0xff);
	}

	static String getSoftVer(Node n) {
		return String.format("%02X.%02X", (n.mSoftVer >> 8) & 0xff,
				n.mSoftVer & 0xff);
	}

	// 设置图标矩形所在的位置
	void setIconRect(int left, int top, int width, int height) {
		mLeft = left;
		mTop = top;
		mWidth = width;
		mHeight = height;
		Log.d(TAG, "(" + left + "," + top + "," + width + "," + height + ")");
	}

	/**
	 * 根据类型设置图标及文字描述,某一个完整图标：图像，地址，文字描述
	 * @param cv
	 *            画布
	 */
	void DrawIcon(Canvas cv) {
		Bitmap mb;
		String desc;
		if (mNodeType == Node.ZB_NODE_TYPE_COORDINATOR) {
			mb = Resource.imageCoordinator;
			desc = Node.getNodeTypeString(this);
		} else {
			switch (mDevType) {
			case Node.DEV_TEMPSENSOR:
				mb = Resource.imageDeviceTempSensor;
				break;
			case Node.DEV_LIGHT:
				mb = Resource.imageDeviceLight;
				break;
			case Node.DEV_SWITCH:
				mb = Resource.imageDeviceSwitch;
				break;
			case Node.DEV_GASSENSOR:
				mb = Resource.imageDevGasSensor;
				break;
			case Node.DEV_INFRAREDSENSOR:
				mb = Resource.imageDevInfrared;
				break;
			case Node.DEV_RFID:
				mb = Resource.imageDevRfid;
				break;
			case Node.DEV_THREEACC:
				mb = Resource.imageDevThreeAcc;
				break;
			case Node.DEV_PRESSURE:
				mb = Resource.imageDevPressure;
				break;
			case Node.DEV_RAIN:
				mb = Resource.imageDevRain;
				break;
			case Node.DEV_VIBRATE:
				mb = Resource.imageDevVIBRATE;
				break;
			case Node.DEV_HALL:
				mb = Resource.imageDevHALL;
				break;
			case Node.DEV_DYNAMO:
				mb = Resource.imageDevDYNAMO;
				break;
			case Node.DEV_DECIBLE:
				mb = Resource.imageDevDECIBLE;
				break;
			case Node.DEV_FLAME:
				mb = Resource.imageDevFLAME;
				break;
			case Node.DEV_ULTRASONIC:
				mb = Resource.imageDevULTRASONIC;
				break;
			case Node.DEV_IRREMOTE:
				mb = Resource.imageDevIRREMOTE;
				break;
			default:
				mb = Resource.imageDevUnknow;
				break;
			}
			desc = Node.getDeviceTypeString(this);
		}
		cv.drawBitmap(mb, mLeft + mWidth / 2 - mb.getWidth() / 2, mTop, null);
		Paint p = new Paint();
		p.setColor(Color.RED);
		p.setTextAlign(Paint.Align.CENTER);
		cv.drawText(String.format("地址:%d", mNetAddr), mLeft + mWidth / 2, mTop
				+ mb.getHeight() + 10, p);
		cv.drawText(desc, mLeft + mWidth / 2, mTop + mb.getHeight() + 25, p);
	}
}
