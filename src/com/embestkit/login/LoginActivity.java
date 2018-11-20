package com.embestkit.login;

import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.embestdkit.R;
import com.embestkit.util.TitleLayout;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.IntRange;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends UserModuleActivity implements OnClickListener{
	public static boolean isclean = false;
	
	public TitleLayout titlelayout;
	
	private static EditText etName;
	private static EditText etPsw;
	private static Button btLogin;
	
	private TextView tvRegister;
	private TextView tvForget;
	private TextView tvPass;
	private CheckBox cbLaws;
	
	private LinearLayout llQQ;
	private LinearLayout llWechat;
	
	private Intent intent;
	
	
	public static enum handler_key{
		FAIl_LOGIN,
		LOGIN,
		AUTO_LOGIN,
		THRED_LOGIN,
	}
	
	Handler baseHandler=new Handler(){
		@Override
		public void handleMessage(android.os.Message msg) {
			super.handleMessage(msg);
			handler_key key=handler_key.values()[msg.what];
			switch (key) {
			case LOGIN:
				setProgressDialog("���ڵ�¼", true, false);
				progressDialog.show();
				//������Ϣ��ת����
				userData(etName.getText().toString(), etPsw.getText().toString(),"Login");
				break;
				
			case AUTO_LOGIN:
				setProgressDialog("�������µ�¼", true, false);
				progressDialog.show();
				break;
				
			case THRED_LOGIN:
				setProgressDialog("��������¼", true, false);
				progressDialog.show();
				break;
			case FAIl_LOGIN:
				Toast.makeText(LoginActivity.this, "��¼ʧ��", Toast.LENGTH_LONG).show();
				break;

			}
		};
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		//����Ϊ����
		/*if(getRequestedOrientation()!=ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		spf=getSharedPreferences("set", Context.MODE_PRIVATE);*/
		
		/*titlelayout=(TitleLayout) findViewById(R.id.layout_title);
		titlelayout.setTitle("��¼");*/
		this.setTitle("�û���¼");
		//�ж��ǲ��ǵ�һ���
		if(!this.isTaskRoot()){
			Intent mainIntent=getIntent();
			String action=mainIntent.getAction();
			if(mainIntent.hasCategory(Intent.CATEGORY_LAUNCHER) && action.equals(Intent.ACTION_MAIN));
			{
				finish();
				return;
			}	
		}
		
		setContentView(R.layout.login);
		initView();
		initEvent();
		
	}
	
	private void initView() {
		// TODO Auto-generated method stub
		etName=(EditText) findViewById(R.id.etName);
		etPsw=(EditText) findViewById(R.id.etPsw);
		btLogin=(Button) findViewById(R.id.btnLogin);
		
		tvForget=(TextView) findViewById(R.id.tvForget);
		tvRegister=(TextView) findViewById(R.id.tvRegister);
		cbLaws=(CheckBox) findViewById(R.id.cbLaws);
		tvPass=(TextView) findViewById(R.id.tvPass);
		
		llQQ=(LinearLayout) findViewById(R.id.llQQ);
		llWechat=(LinearLayout) findViewById(R.id.llWechat);
	}
	
	private void initEvent() {
		// TODO Auto-generated method stub
		btLogin.setOnClickListener(this);
		tvRegister.setOnClickListener(this);
		tvForget.setOnClickListener(this);
		tvPass.setOnClickListener(this);
		
		llQQ.setOnClickListener(this);
		llWechat.setOnClickListener(this);
		
		cbLaws.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				String psw=etPsw.getText().toString();
				if(isChecked)
					etPsw.setInputType(0x90);
				else
					etPsw.setInputType(0x81);
				etPsw.setSelection(psw.length());
			}
		});
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btnLogin:
			if(TextUtils.isEmpty(etName.getText().toString())){
				Toast.makeText(LoginActivity.this, "�������û���", Toast.LENGTH_SHORT).show();
				return;
			}
			if(TextUtils.isEmpty(etPsw.getText().toString())){
				Toast.makeText(LoginActivity.this, "����������", Toast.LENGTH_SHORT).show();
				return;
			}
			//handle������Ϣ��ת����
			baseHandler.sendEmptyMessage(handler_key.LOGIN.ordinal());
			break;
			
		case R.id.tvRegister:
			intent = new Intent(LoginActivity.this, RegisterUserActivity.class);
			startActivity(intent);
			break;
		case R.id.tvForget:
			intent = new Intent(LoginActivity.this, ForgetPasswordActivity.class);
			startActivity(intent);
			break;
		case R.id.tvPass:

			intent = new Intent(LoginActivity.this, MainFrameActivity.class);
			startActivity(intent);

			logoutToClean();//ע������
			break;

		case R.id.llQQ:
			Toast.makeText(LoginActivity.this, "�˹�����δ����", Toast.LENGTH_SHORT).show();
			return;
		case R.id.llWechat:
			Toast.makeText(LoginActivity.this, "�˹�����δ����", Toast.LENGTH_SHORT).show();
			return;
		}
	}
	
	private static Boolean isExit = false;
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {// ����˫���˳�����
			Timer tExit = null;
			if (isExit == false) {
				isExit = true; // ׼���˳�
				Toast.makeText(this, "˫���˳�����", 2000).show();
				tExit = new Timer();
				tExit.schedule(new TimerTask() {
					@Override
					public void run() {
						isExit = false; // ȡ���˳�
					}
				}, 2000); // ���2������û�а��·��ؼ�����������ʱ��ȡ�����ղ�ִ�е�����

			} else {
				this.finish();
				System.exit(0);
			}
		}
		return false;
	}
	
	//����
	private void logoutToClean() {
		// TODO Auto-generated method stub
		spf.edit().putString("UserName", "").commit();
		spf.edit().putString("PassWord", "").commit();
	}
	private void aotuLogin() {
		// TODO Auto-generated method stub
		if (TextUtils.isEmpty(spf.getString("UserName", "")) || TextUtils.isEmpty(spf.getString("PassWord", ""))) {
			return;
		}
		//��¼��תhandler������Ϣ��ת����
		baseHandler.sendEmptyMessage(handler_key.AUTO_LOGIN.ordinal());
	}
	private void cleanuserthing() {

		if (isclean) {
			etName.setText("");
			;
			etPsw.setText("");
			;
		}
	}
	//�û���¼�ص�
	@Override
	public void didUserLogin(String response) {
		// TODO �Զ����ɵķ������
		progressDialog.cancel();
		String id = null;
		try {
			JSONArray jsonArray=new JSONArray(response);
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject jsonObject=jsonArray.getJSONObject(i);
				id=jsonObject.getString("id");
				
			}
		} catch (JSONException e) {
			// TODO �Զ����ɵ� catch ��
			e.printStackTrace();
		}
		
		if(!TextUtils.isEmpty(response) && Integer.parseInt(id)>0){
			spf.edit().putString("UserName", etName.getText().toString()).commit();
			spf.edit().putString("PassDord", etPsw.getText().toString()).commit();
			spf.edit().putString("userId", id).commit();
			System.out.println("�õ����û�ID�ǣ�"+spf.getString("userId", ""));
			intent = new Intent(LoginActivity.this, MainFrameActivity.class);
			startActivity(intent);
		}else{
			baseHandler.sendEmptyMessage(handler_key.FAIl_LOGIN.ordinal());
		}
	}
	
	//�������ڣ�����
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		aotuLogin();
		//cleanuserthing();
	}
		
}
