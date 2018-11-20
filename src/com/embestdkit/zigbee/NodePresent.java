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
	 * 工厂设计模式 根据结点类型创建实例,协调器，结点
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

	NodePresent(int id, Node n) { // 获得一个context
		LayoutInflater inflater = LayoutInflater.from(ZigBeeTool.getInstance().getBaseContext());
		mView = inflater.inflate(id, null);
		mNode = n;
		_commNodeInfoSet(mView, mNode);
	}

	/**
	 * 这些信息是所有类型结点都有的。填写描述信息： 物理地址 网络地址 结点类型 设备类型 硬件版本 软件版本
	 * 
	 * @param v
	 *            填写信息的依据
	 * @param n
	 */
	private void _commNodeInfoSet(View v, Node n) { // 注意这种写法
		((EditText) (v.findViewById(R.id.EditText_IEEEAddr))).// 填写M A C 地 址
				setText(String.format(
						"%02X:%02X:%02X:%02X:%02X:%02X:%02X:%02X",
						n.mIEEEAddr[0], n.mIEEEAddr[1], n.mIEEEAddr[2],
						n.mIEEEAddr[3], n.mIEEEAddr[4], n.mIEEEAddr[5],
						n.mIEEEAddr[6], n.mIEEEAddr[7]));

		((EditText) (v.findViewById(R.id.EditText_NetAddr))).// 填写网络 地 址
				setText(String.format("%d", n.mNetAddr));

		((EditText) (v.findViewById(R.id.EditText_NodeType))).// 填写结点类型
				setText(Node.getNodeTypeString(n));

		((EditText) (v.findViewById(R.id.EditText_DeviceType))).// 填写设备类型
				setText(Node.getDeviceTypeString(n));

		((EditText) (v.findViewById(R.id.EditText_HardVer))). // 填写硬件版本
				setText(Node.getHardVer(n));

		((EditText) (v.findViewById(R.id.EditText_SoftVer))).// 填写软件版本
				setText(Node.getSoftVer(n));
	}

	/**
	 * 发送命令请求
	 * 
	 * @param cmd
	 * @param dat
	 */
	void sendRequest(int cmd, byte[] dat) {
		byte[] data = new byte[dat.length + 4]; // 在dat前面再加上4个字节
		data[0] = (byte) (mNode.mNetAddr >> 8); // data[0]保存该节点网络地址的前24位
		data[1] = (byte) mNode.mNetAddr; // data[1]保存该结点的网络地址
		data[2] = (byte) (cmd >> 8); // data[2]保存该节点命令的前24位
		data[3] = (byte) cmd; // data[3]保存该节点命令

		for (int i = 0; i < dat.length; i++)
			data[4 + i] = dat[i]; // 保存dat的信息
		ZigBeeTool.getInstance().mZbThread.requestAppMessage(2, data);
	}

	// 抽象方法，在子类中实现访问，交互
	abstract void setup();

	abstract void setdown();

	abstract void procData(int req, byte[] dat);

	abstract void procAppMsgData(int addr, int cmd, byte[] dat);
}
