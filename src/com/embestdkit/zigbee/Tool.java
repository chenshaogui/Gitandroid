package com.embestdkit.zigbee;

import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;

import com.embestdkit.R;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.ColorMatrixColorFilter;
import android.media.MediaPlayer;
import android.telephony.SmsManager;
import android.util.Log;

/**
 * ���ߺ���
 * 
 * @author Administrator
 * 
 */
public class Tool {

	static final float[] BT_SELECTED = new float[] { 1, 0, 0, 0, 50, 0, 1, 0,
			0, 50, 0, 0, 1, 0, 50, 0, 0, 0, 1, 0 };
	static final float[] BT_NOT_SELECTED = new float[] { 1, 0, 0, 0, 0, 0, 1,
			0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0 };
	static final ColorMatrixColorFilter selfilter = new ColorMatrixColorFilter(
			BT_SELECTED);
	static final ColorMatrixColorFilter unselfilter = new ColorMatrixColorFilter(
			BT_NOT_SELECTED);

	/**
	 * ��byteת����int
	 * 
	 * @param b
	 * @return
	 */
	static int builduInt(byte b) { // ��16λ��0����
		return (b & 0xff);
	}

	/**
	 * ��16����תΪ�ַ���
	 * 
	 * @param b
	 * @return
	 */
	public static String str16toStr(String hexstr) {

		String str="0123456789ABCDEF";
		char[] hexs=hexstr.toCharArray();
		byte[] bytes=new byte[hexstr.length()/2];
		int n;
		for (int i = 0; i < bytes.length; i++) {
			n=str.indexOf(hexs[2*i])*16;
			n+=str.indexOf(hexs[2*i+1]);
			bytes[i]=(byte) (n & 0xff);
		}
		return new String(bytes);
	}

	/**
	 * ���ַ���תΪ16����
	 * 
	 * @param b
	 * @return
	 */
	public static String strTo16Str(String str) {
		char[] chars = "0123456789ABCDEF".toCharArray();
		StringBuilder sb = new StringBuilder("");
		byte[] bs = str.getBytes();
		int bit;
		for (int i = 0; i < bs.length; i++) {
			bit = (bs[i] & 0x0f0) >> 4;
			sb.append(chars[bit]);
			bit = bs[i] & 0x0f;
			sb.append(chars[bit]);
		}
		return sb.toString().trim();
	}

	/**
	 * 
	 * @param b1
	 * @param b2
	 * @return
	 */
	static int builduInt(byte b1, byte b2) { // ��λ���룬b1&0xff��16λ��(b1&0xff)<<8����b1&0xff���16λ����ֵ���߰�λΪ0���Ͱ�λ�����ֵ���ĵͰ�λͨ�����ư�λת�Ƶ��߰�λ���Ͱ�λ��0����
												// |�����㣬�õ�һ���µ�16λ����8λ��b1���֣��Ͱ�λ��b2���֣������Ϊƴ��
		return (((b1 & 0xff) << 8) | (b2 & 0xff));
	}

	/**
	 * �ֽ�ת����String
	 * 
	 * @param b
	 * @return
	 */
	static String byteTostring(byte[] b) {
		String s = "";
		if (b == null) {
			return "<null>";
		}
		for (int i = 0; i < b.length; i++) {
			s += String.format("%02X ", b[i]);
		}
		return s;
	}

	/***************************************************************/
	/*
	 * class TimerHandler extends Handler { public void handleMessage(Message
	 * msg) {
	 * 
	 * } } interface TimerListener {
	 * 
	 * } static TimerHandler mTimerHandler = new TimerHandler(); static void
	 * startTimer(TimerListener li, int ms) { Message msg = Message.obtain();
	 * msg.obj = li; mTimerHandler.sendMessageDelayed(msg, 300); }
	 */

	/***************************************************************
	 ************************** ֪ͨ����
	 *****************************/
	static NotificationManager mNotificationManager;
	static SmsManager mSmsManager;
	static MediaPlayer mMdeiaPlayer = MediaPlayer.create(ZigBeeTool
			.getInstance().getBaseContext(), R.raw.alarm_1);
	static MediaPlayer.OnCompletionListener onCompletion = new MediaPlayer.OnCompletionListener() {
		@Override
		public void onCompletion(MediaPlayer arg0) {
			Log.d("player", "onCompletion...");
			mAlarmCnt--;
			try {
				mMdeiaPlayer.prepare();
			} catch (IllegalStateException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			if (mAlarmCnt > 0) {
				mMdeiaPlayer.start();
			}
		}
	};
	static int mAlarmCnt = 0;

	// ���¶ȣ�ʪ�ȵȲ�����ʱ����֪ͨ
	static void playAlarm(int cnt) {
		mMdeiaPlayer.setOnCompletionListener(onCompletion);
		mAlarmCnt = cnt;
		if (cnt == 0) {
			mMdeiaPlayer.setLooping(true);
		} else {
			mAlarmCnt = cnt;
		}
		mMdeiaPlayer.start();
	}

	static void stopAlarm() {
		mAlarmCnt = 0;
		mMdeiaPlayer.stop();
		try {
			mMdeiaPlayer.prepare();
			mMdeiaPlayer.seekTo(0);
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	static int sNotifyId = 1001;

	// ���¶ȣ�ʪ�ȵȲ�����ʱ����֪ͨ
	static void notify(String title, String msg) {
		if (mNotificationManager == null) {
			mNotificationManager = (NotificationManager) ZigBeeTool
					.getInstance().getSystemService(
							Context.NOTIFICATION_SERVICE);
		}

		Notification notification = new Notification(R.drawable.icon, title,
				System.currentTimeMillis());
		PendingIntent intent = PendingIntent.getActivity(
				ZigBeeTool.getInstance(), 0,
				new Intent(ZigBeeTool.getInstance(), ZigBeeTool.class), 0);
		notification.setLatestEventInfo(ZigBeeTool.getInstance(), title, msg,
				intent);
		notification.flags |= Notification.FLAG_AUTO_CANCEL;// ���߰�λ�����㣬��notification.flags��ȥNotification.FLAG_AUTO_CANCEL��������

		// notification.defaults |= Notification.DEFAULT_SOUND ; //֪ͨ������
		// ��������

		// noticed.sound = Uri.parse("file:///sdcard/notification/ringer.mp3");
		// noticed.sound =
		// Uri.withAppendedPath(Audio.Media.INTERNAL_CONTENT_URI, "6");
		// mNotificationManager.cancelAll();
		// ע������д����������ģʽ
		mNotificationManager.notify(sNotifyId++, notification);

		// m.notifyWithText(1001, "�錄����SHARETOP�Ǥ���", NotificationManager, null);
	}

	static void sendShortMessage(String mobile, String content) {
		if (mSmsManager == null) {
			mSmsManager = SmsManager.getDefault();
		}
		if (content.length() > 70) {
			// ʹ�ö��Ź��������ж������ݵķֶΣ����طֳɵĶ�
			ArrayList<String> contents = mSmsManager.divideMessage(content);
			for (String msg : contents) {
				// ʹ�ö��Ź��������Ͷ�������
				// ����һΪ���Ž�����
				// ������Ϊ��������
				// ����������Ϊnull
				mSmsManager.sendTextMessage(mobile, null, msg, null, null);
			}
			// ����һ�ι�����
		} else {
			mSmsManager.sendTextMessage(mobile, null, content, null, null);
		}
	}

	interface IterateIpAddressListener {
		void iterate(String name, String ip);
	}

	/**
	 * ?????????????????????????????????????????????????????????????????????????
	 * 
	 * @param a
	 */
	static void IterateLocalIpAddress(IterateIpAddressListener a) {
		try { // ö�ټ�en,װ����ӿ�
			for (Enumeration<NetworkInterface> en = NetworkInterface
					.getNetworkInterfaces(); en.hasMoreElements();) {
				NetworkInterface intf = en.nextElement();
				// ö�ټ�enumIpAddr��װip��ַ
				for (Enumeration<InetAddress> enumIpAddr = intf
						.getInetAddresses(); enumIpAddr.hasMoreElements();) {
					InetAddress inetAddress = enumIpAddr.nextElement();
					// if (!inetAddress.isLoopbackAddress())
					{
						Log.d(intf.getName(), inetAddress.getHostAddress()
								.toString());
						// �ӿڱ������ýӿڵķ���
						a.iterate(intf.getName(), inetAddress.getHostAddress()
								.toString());
					}
				}
			}
		} catch (SocketException ex) {
			Log.e("WifiPreference IpAddress", ex.toString());
		}

	}

}
