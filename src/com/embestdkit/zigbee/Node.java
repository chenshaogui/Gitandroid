package com.embestdkit.zigbee;

import java.util.LinkedList;
import java.util.List;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

/*
 * �����ڵ�����ݱ�ʾ 
 */
public class Node {
	private static final String TAG = "Zbnode";

	/* �������,Э������·�ɣ��豸 */
	final static int ZB_NODE_TYPE_COORDINATOR = 0;
	final static int ZB_NODE_TYPE_ROUTER = 1;
	final static int ZB_NODE_TYPE_ENDDEVICE = 2;

	/* �豸���� */
	static final int DEV_NONE = 0;// δ֪
	static final int DEV_TEMPSENSOR = 1;// ��ʪ��
	static final int DEV_LIGHT = 2;// �����豸
	static final int DEV_SWITCH = 3;// �̵����豸
	static final int DEV_INFRAREDSENSOR = 4;// �������
	static final int DEV_GASSENSOR = 5;// ��ȼ����
	static final int DEV_VIBRATE = 6;// ��
	static final int DEV_ALCOHOL = 7;// �ƾ�
	static final int DEV_RAIN = 8;// ���
	static final int DEV_HALL = 9;// ����
	static final int DEV_THREEACC = 10;// ������ٶ�
	static final int DEV_DYNAMO = 11;// ���
	static final int DEV_PRESSURE = 12;// ����ѹ��
	static final int DEV_DECIBLE = 13;// �ֱ�
	static final int DEV_FLAME = 14;// ����
	static final int DEV_ULTRASONIC = 15;// ������
	static final int DEV_IRREMOTE = 16;// ����ң��
	static final int DEV_RFID = 17;// RFID��Ƶʶ��

	List<Node> _childNode; // ���ӽ�������ýڵ�����������

	int mHardVer; // Ӳ����
	int mSoftVer; // �����

	byte[] mIEEEAddr; // M A C �� ַ ,�ֽ�����
	int mNetAddr; // �����ַ

	int mNodeType; // �ڵ�����
	int mDevType; // �豸����

	// EndPoint[] mEndPoints;

	/* ��ʾ������� */
	// Bitmap mBmp; //����Ӧ����ʾͼ��
	int mLeft, mTop, mWidth, mHeight; // ��ͼʱ������ڵ�λ��
	int mDeepth; // ��������������,���������ж��ٲ�

	Node(int netaddr) {
		mNetAddr = netaddr;
		mIEEEAddr = new byte[8]; // ����8���ֽڵ��ֽ�����
		_childNode = new LinkedList<Node>();
	}

	Node(int netaddr, int nodetype) {
		mNetAddr = netaddr;
		mNodeType = nodetype;
		mIEEEAddr = new byte[8]; // ����8���ֽڵ��ֽ�����
		_childNode = new LinkedList<Node>();
	}

	// ��ý���ַ�������
	static String getNodeTypeString(Node n) {
		switch (n.mNodeType) {
		case Node.ZB_NODE_TYPE_COORDINATOR:
			return "Э����";
		case Node.ZB_NODE_TYPE_ROUTER:
			return "·����";
		default:
			return "�ն˽��";
		}
	}

	// ����豸�ַ�������
	static String getDeviceTypeString(Node n) {
		switch (n.mDevType) {
		case Node.DEV_TEMPSENSOR:
			return "�¶ȴ�����";
		case Node.DEV_LIGHT:
			return "����������";
		case Node.DEV_SWITCH:
			return "�̵����豸";
		case Node.DEV_INFRAREDSENSOR:
			return "������⴫����";
		case Node.DEV_GASSENSOR:
			return "��ȼ���崫����";
		case Node.DEV_VIBRATE:
			return "�񶯴�����";
		case Node.DEV_THREEACC:
			return "������ٶȴ�����";
		case Node.DEV_PRESSURE:
			return "��ѹ������";
		case Node.DEV_RAIN:
			return "��δ�����";
		case Node.DEV_ALCOHOL:
			return "�ƾ�������";
		case Node.DEV_FLAME:
			return "���洫����";
		case Node.DEV_HALL:
			return "����������";
		case Node.DEV_DECIBLE:
			return "�ֱ�������";
		case Node.DEV_ULTRASONIC:
			return "������������";
		case Node.DEV_DYNAMO:
			return "���������";
		case Node.DEV_IRREMOTE:
			return "����ң�ش�����";
		case Node.DEV_RFID:
			return "RFID��Ƶʶ���豸";
		default:
			return "δ֪�豸";
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

	// ����ͼ��������ڵ�λ��
	void setIconRect(int left, int top, int width, int height) {
		mLeft = left;
		mTop = top;
		mWidth = width;
		mHeight = height;
		Log.d(TAG, "(" + left + "," + top + "," + width + "," + height + ")");
	}

	/**
	 * ������������ͼ�꼰��������,ĳһ������ͼ�꣺ͼ�񣬵�ַ����������
	 * @param cv
	 *            ����
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
		cv.drawText(String.format("��ַ:%d", mNetAddr), mLeft + mWidth / 2, mTop
				+ mb.getHeight() + 10, p);
		cv.drawText(desc, mLeft + mWidth / 2, mTop + mb.getHeight() + 25, p);
	}
}
