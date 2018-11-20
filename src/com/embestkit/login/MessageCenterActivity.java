package com.embestkit.login;

import com.embestdkit.R;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

public class MessageCenterActivity extends UserModuleActivity{

	private View redpoint;
	private LinearLayout smes;
	private LinearLayout devicesahred;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);
		setContentView(R.layout.message_center);
		initView();
	}
	
	
	private void initView() {
		// TODO 自动生成的方法存根
		redpoint=findViewById(R.id.redpoint);
		smes=(LinearLayout) findViewById(R.id.smes);
		devicesahred=(LinearLayout) findViewById(R.id.deviceshared);
		
	}
	@Override
	protected void onResume() {
		// TODO 自动生成的方法存根
		super.onResume();
	}
}
