package com.embestkit.login;

import android.text.TextUtils;

import com.embestkit.util.HttpUtil;
import com.embestkit.util.HttpUtil.HttpCallbackListener;

public class UserModuleActivity extends BaseActivity {

	/*
	 * public static HttpResultListener listener;
	 * 
	 * public interface HttpResultListener{ void didUserLogin(String response);
	 * }
	 */

	//private static String URL = "http://192.168.253.1:8082/sensor/user/userLogin?userName=chen&passWord=chen";
	private  String URL1 = "http://192.168.253.1:8082/sensor/user/";
	public String URL=null;

	void userData(String name, String password, final String token) {
		// TODO �Զ����ɵķ������
		
		if (token == "Login")
			URL =URL1+ "userLogin";
		else if (token == "Register")
			URL =URL1+ "userReg";
		else
			return;
		String data = "userName=" + name + "&passWord=" + password;
		// String http="http://192.168.253.1:8082/sensor/user/userReg";
		HttpUtil.postHttpResquest(URL, data, new HttpCallbackListener() {

			@Override
			public void onError(Exception e) {
				// TODO �Զ����ɵķ������
				System.out.println("�������ʧ��");
			}

			@Override
			public void finish(String response) {
				// TODO �Զ����ɵķ������
				System.out.println("�������" + response);
				if (token == "Login")
					UserModuleActivity.this.didUserLogin(response);
				else if (token == "Register")
					UserModuleActivity.this.didUserRegister(response);
				else
					return;

			}
		});
	}

	/**
	 * �豸���ݴ���
	 */
	public void deviceData(String msg,String token){
		if(token=="relay_")
			URL=URL1+token;
		else if(token=="photosensitive")
			URL=URL1+token;
		else if(token=="tempseneor")
			URL=URL1+token;
		else
			return;
		HttpUtil.postHttpResquest(URL, msg, new HttpCallbackListener() {
			
			@Override
			public void onError(Exception e) {
				// TODO �Զ����ɵķ������
				
			}
			
			@Override
			public void finish(String response) {
				// TODO �Զ����ɵķ������
				
			}
		});
		
		
	}
	
	
	void didUserLogin(String response) {
	}

	void didUserRegister(String resonse) {
	}
}
