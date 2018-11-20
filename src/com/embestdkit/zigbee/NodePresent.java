package com.embestdkit.zigbee;

import com.embestdkit.R;
import com.embestkit.login.BaseActivity;
import com.embestkit.login.UserModuleActivity;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

public abstract class NodePresent extends UserModuleActivity{
	View mView;
	Node mNode;
	String userId=null;

	/**
	 * �������ģʽ ���ݽ�����ʹ���ʵ��,Э���������
	 * @param n
	 * @return
	 */
	static NodePresent FactoryCreateInstance(Node n) {
		if (n.mNodeType == Node.ZB_NODE_TYPE_COORDINATOR) {
			return new CoordinatorPresent(n);
		} else if (n.mDevType == Node.DEV_TEMPSENSOR) {
			return new TempSensorPresent(n);
		}else if (n.mDevType == Node.DEV_LIGHT) {
		    return new LightDevicePresent(n);
		} else if (n.mDevType == Node.DEV_SWITCH) {
			return new SwitchDevicePresent(n);
		} else if (n.mDevType == Node.DEV_INFRAREDSENSOR) {
			return new InfraredSensorPresent(n);
		} else if (n.mDevType == Node.DEV_GASSENSOR) {
			return new GasSensorPresent(n);
		} else if (n.mDevType == Node.DEV_ULTRASONIC) {
			return new UltrasonicSensorPresent(n);
		} else if (n.mDevType == Node.DEV_RFID) {
			return new RfidPresent(n);
		} else if (n.mDevType == Node.DEV_THREEACC) {
			return new ThreeAccPresent(n);
		} else if (n.mDevType == Node.DEV_PRESSURE) {
			return new PressurePresent(n);
		}else if (n.mDevType == Node.DEV_RAIN) {
			return new RainPresent(n);
		} else if (n.mDevType == Node.DEV_VIBRATE) {
			return new VibrateSensorPresent(n);
		} else if (n.mDevType == Node.DEV_ALCOHOL) {
			return new AlcoholSensorPresent(n);
		} else if (n.mDevType == Node.DEV_HALL) {
			return new HallSensorPresent(n);
		} else if (n.mDevType == Node.DEV_DYNAMO) {
			return new DynamoSensorPresent(n);
		} else if (n.mDevType == Node.DEV_DECIBLE) {
			return new DecibelSensorPresent(n);
		} else if (n.mDevType == Node.DEV_FLAME) {
			return new FlameSensorPresent(n);
		} else if (n.mDevType == Node.DEV_ULTRASONIC) {
			return new UltrasonicSensorPresent(n);
		} else if (n.mDevType == Node.DEV_IRREMOTE) {
			return new IrRemoteSensorPresent(n);
		} else {
			return new CoordinatorPresent(n);
		}
	}

	NodePresent(int id, Node n) { // ���һ��context
		LayoutInflater inflater = LayoutInflater.from(ZigBeeTool.getInstance().getBaseContext());
		mView = inflater.inflate(id, null);
		mNode = n;
		_commNodeInfoSet(mView, mNode);
	}

	/**
	 * ��Щ��Ϣ���������ͽ�㶼�еġ���д������Ϣ�� �����ַ �����ַ ������� �豸���� Ӳ���汾 ����汾
	 * 
	 * @param v
	 *            ��д��Ϣ������
	 * @param n
	 */
	private void _commNodeInfoSet(View v, Node n) { // ע������д��
		((EditText) (v.findViewById(R.id.EditText_IEEEAddr))).// ��дM A C �� ַ
				setText(String.format(
						"%02X:%02X:%02X:%02X:%02X:%02X:%02X:%02X",
						n.mIEEEAddr[0], n.mIEEEAddr[1], n.mIEEEAddr[2],
						n.mIEEEAddr[3], n.mIEEEAddr[4], n.mIEEEAddr[5],
						n.mIEEEAddr[6], n.mIEEEAddr[7]));

		((EditText) (v.findViewById(R.id.EditText_NetAddr))).// ��д���� �� ַ
				setText(String.format("%d", n.mNetAddr));

		((EditText) (v.findViewById(R.id.EditText_NodeType))).// ��д�������
				setText(Node.getNodeTypeString(n));

		((EditText) (v.findViewById(R.id.EditText_DeviceType))).// ��д�豸����
				setText(Node.getDeviceTypeString(n));

		((EditText) (v.findViewById(R.id.EditText_HardVer))). // ��дӲ���汾
				setText(Node.getHardVer(n));

		((EditText) (v.findViewById(R.id.EditText_SoftVer))).// ��д����汾
				setText(Node.getSoftVer(n));
	}

	/**
	 * ������������
	 * 
	 * @param cmd
	 * @param dat
	 */
	void sendRequest(int cmd, byte[] dat) {
		byte[] data = new byte[dat.length + 4]; // ��datǰ���ټ���4���ֽ�
		data[0] = (byte) (mNode.mNetAddr >> 8); // data[0]����ýڵ������ַ��ǰ24λ
		data[1] = (byte) mNode.mNetAddr; // data[1]����ý��������ַ
		data[2] = (byte) (cmd >> 8); // data[2]����ýڵ������ǰ24λ
		data[3] = (byte) cmd; // data[3]����ýڵ�����

		for (int i = 0; i < dat.length; i++)
			data[4 + i] = dat[i]; // ����dat����Ϣ
		ZigBeeTool.getInstance().mZbThread.requestAppMessage(2, data);
	}

	// ���󷽷�����������ʵ�ַ��ʣ�����
	abstract void setup();

	abstract void setdown();

	abstract void procData(int req, byte[] dat);

	abstract void procAppMsgData(int addr, int cmd, byte[] dat);
}
