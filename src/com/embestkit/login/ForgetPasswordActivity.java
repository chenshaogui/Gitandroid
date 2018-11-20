package com.embestkit.login;

import java.util.Timer;
import java.util.TimerTask;

import com.embestdkit.R;


import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
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
import android.widget.EditText;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class ForgetPasswordActivity extends UserModuleActivity implements OnClickListener {

	private EditText etName;
	private EditText etCode;
	private EditText etPsw;
	
	private Button btnReset;
	private CheckBox cbLaws;
	
	private ProgressDialog progressDialog;
	String name,code,psw;
	
	private enum handle_key{
		RESET,
	}
	
	Handler hander=new Handler(){
		@Override
		public void handleMessage(android.os.Message msg) {
			super.handleMessage(msg);
			handle_key key=handle_key.values()[msg.what];
			switch (key) {
			case RESET:
				progressDialog=new ProgressDialog(ForgetPasswordActivity.this);
				progressDialog.setMessage("正在修改密码");
				progressDialog.setCanceledOnTouchOutside(false);
				progressDialog.show();
				//后台修改密码
				break;

			default:
				break;
			}
		};
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.forget_password);
		//设置actionBAr
		this.setTitle("重置密码");
		initView();
		initEvent();
		
	};
	private void initView() {
		// TODO 自动生成的方法存根
		etName=(EditText) findViewById(R.id.etName);
		etCode=(EditText) findViewById(R.id.etCode);
		etPsw=(EditText) findViewById(R.id.etPsw);
		btnReset=(Button) findViewById(R.id.btnReset);
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
				btnReset.setOnClickListener(this);
				cbLaws.setOnCheckedChangeListener(new OnCheckedChangeListener() {
					
					@Override
					public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
						// TODO 自动生成的方法存根
						String psw=etPsw.getText().toString();
						if(isChecked){
							etPsw.setInputType(0x90);
						}else{
							etPsw.setInputType(0x81);
						}
						etPsw.setSelection(psw.length());
					}
				});
	}
	
	@Override
	public void onClick(View v) {
		// TODO 自动生成的方法存根
		switch (v.getId()) {
		case R.id.btnReset:
			name=etName.getText().toString();
			psw=etPsw.getText().toString();
			code=etCode.getText().toString();
			if(TextUtils.isEmpty(name)){
				Toast.makeText(ForgetPasswordActivity.this, "请输入用户名", Toast.LENGTH_LONG).show();
				return;
			}
			if(TextUtils.isEmpty(psw)){
				Toast.makeText(ForgetPasswordActivity.this, "请输入密码", Toast.LENGTH_LONG).show();
				return;
			}
			if(code!="123456"){
				Toast.makeText(ForgetPasswordActivity.this, "验证码不正确", Toast.LENGTH_LONG).show();
				return;
			}
			hander.sendEmptyMessage(handle_key.RESET.ordinal());
			break;
		default:
			break;
		}
	}
	/**
	 * 重置密码回调
	 */
	public void didChnangeUserPassword() {
		// TODO 自动生成的方法存根

	}

}
