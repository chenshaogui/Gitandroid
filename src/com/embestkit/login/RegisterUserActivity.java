package com.embestkit.login;

import java.util.Timer;
import java.util.TimerTask;

import com.embestdkit.R;
import com.embestkit.login.LoginActivity.handler_key;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.Toast;

public class RegisterUserActivity extends UserModuleActivity implements OnClickListener{
	
	private EditText etName;
	private EditText etPsw;
	private EditText etPsw1;
	
	private Button btnRegister;
	private CheckBox cbLaws;
	
	ProgressDialog progressDialog;
	Timer timer;
	
	String name,psw,psw1;
	
	private Intent intent;
	
	
	private enum handler_key{
		TOAST,
		/**
		 * 倒计时
		 *
		 */
		TICK_TIME,
		REGISTER,
		FAIl_REG,
	}
	Handler handler=new Handler(){
		@Override
		public void handleMessage(android.os.Message msg) {
			super.handleMessage(msg);
			handler_key key=handler_key.values()[msg.what];
			switch (key){
			case REGISTER:
				progressDialog=new ProgressDialog(RegisterUserActivity.this);
				progressDialog.setMessage("正在加载");
				progressDialog.setCanceledOnTouchOutside(false);
				progressDialog.show();
				//回调函数处理信息
				userData(name, psw, "Register");
				break;
			case FAIl_REG:
				Toast.makeText(RegisterUserActivity.this, "注册失败", Toast.LENGTH_LONG).show();
				//回调函数处理信息
				break;
			default:
				break;
			}
			
		};
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register_user);
		this.setTitle("用户注册");
		initView();
		initEvent();
	}
	
	private void initView() {
		// TODO 自动生成的方法存根
		etName=(EditText) findViewById(R.id.etName);
		etPsw=(EditText) findViewById(R.id.etPsw);
		etPsw1=(EditText) findViewById(R.id.etPsw1);
		btnRegister=(Button) findViewById(R.id.btnRrgister);
		cbLaws=(CheckBox) findViewById(R.id.cbLaws);
	}
	
	private void initEvent() {
		// TODO 自动生成的方法存根
		//进入打开虚拟键盘
		final Timer etTimer=new Timer();
		etTimer.schedule(new TimerTask() {
			
			@Override
			public void run() {
				// TODO 自动生成的方法存根
				etName.requestFocus();
				InputMethodManager imm=(InputMethodManager) etName.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.toggleSoftInput(0, InputMethodManager.SHOW_FORCED);
				etTimer.cancel();
				
			}
		}, 500);
		
		btnRegister.setOnClickListener(this);
		cbLaws.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO 自动生成的方法存根
				String psw=etPsw.getText().toString();
				String psw1=etPsw.getText().toString();
				if(isChecked){
					etPsw.setInputType(0x90);
					etPsw1.setInputType(0x90);
				}else{
					etPsw.setInputType(0x81);
					etPsw1.setInputType(0x81);
				}
				etPsw.setSelection(psw.length());
				etPsw1.setSelection(psw1.length());
			}
		});
		
	}
	

	@Override
	public void onClick(View v) {
		// TODO 自动生成的方法存根
		switch (v.getId()) {
		case R.id.btnRrgister:
			name=etName.getText().toString();
			psw=etPsw.getText().toString();
			psw1=etPsw1.getText().toString();
			if(TextUtils.isEmpty(name)){
				Toast.makeText(RegisterUserActivity.this, "请输入用户名", Toast.LENGTH_LONG).show();
				return;
			}
			if(TextUtils.isEmpty(psw) || TextUtils.isEmpty(psw1) ){
				Toast.makeText(RegisterUserActivity.this, "请输入密码", Toast.LENGTH_LONG).show();
				return;
			}
			if(psw==psw1){
				Toast.makeText(RegisterUserActivity.this, "密码前后不一致", Toast.LENGTH_LONG).show();
				return;
			}
			handler.sendEmptyMessage(handler_key.REGISTER.ordinal());
			break;
		}
		
	}
	/**
	 * 用户注册后回调
	 */
	@Override
	void didUserRegister(String resonse) {
		// TODO 自动生成的方法存根
		super.didUserRegister(resonse);
		progressDialog.cancel();
		int re=Integer.parseInt(resonse);
		if (re>0) {
			System.out.println("注册成功");
			spf.edit().putString("UserName", etName.getText().toString()).commit();
			spf.edit().putString("PassWord", etPsw.getText().toString()).commit();
			intent = new Intent(RegisterUserActivity.this, MainFrameActivity.class);
			startActivity(intent);
		}else
			handler.sendEmptyMessage(handler_key.FAIl_REG.ordinal());
	}

}
