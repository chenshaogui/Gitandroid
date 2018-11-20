package com.embestkit.login;

import com.embestdkit.R;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

public class PersonalCenterActivity extends UserModuleActivity implements OnClickListener{

	private static final int SETTINGS = 123;
	private LinearLayout llAbout;
	Intent intent;
	private LinearLayout usermanager;
	private LinearLayout lllogin;
	
	private TextView username;
	private LinearLayout deviceshared;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);
		setContentView(R.layout.personal_center);
		
		initView();
		initEvent();
	}
	
	private void initView() {
		llAbout = (LinearLayout) findViewById(R.id.llAbout);

		usermanager = (LinearLayout) findViewById(R.id.usermanager);

		lllogin = (LinearLayout) findViewById(R.id.lllogin);

		deviceshared = (LinearLayout) findViewById(R.id.deviceshared);

		username = (TextView) findViewById(R.id.phoneusername);

		if (!TextUtils.isEmpty(spf.getString("UserName", "")) && !TextUtils.isEmpty(spf.getString("PassWord", ""))) {
			usermanager.setVisibility(View.VISIBLE);

			lllogin.setVisibility(View.GONE);
			username.setText(spf.getString("UserName", ""));
		} else {
			usermanager.setVisibility(View.GONE);
			lllogin.setVisibility(View.VISIBLE);
		}
	}
	
	private void initEvent() {
		// TODO 自动生成的方法存根
		llAbout.setOnClickListener(this);
		usermanager.setOnClickListener(this);
		lllogin.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO 自动生成的方法存根
		switch (v.getId()) {
		case R.id.llAbout:
			/*intent = new Intent(GosSettiingsActivity.this, GosAboutActivity.class);
			startActivity(intent);
			llAbout.setEnabled(false);
			llAbout.postDelayed(new Runnable() {
				@Override
				public void run() {
					llAbout.setEnabled(true);
				}
			}, 1000);*/
			break;

		case R.id.usermanager:
			/*intent = new Intent(GosSettiingsActivity.this, GosUserManager.class);
			startActivityForResult(intent, SETTINGS);

			usermanager.setEnabled(false);
			usermanager.postDelayed(new Runnable() {
				@Override
				public void run() {
					usermanager.setEnabled(true);
				}
			}, 1000);*/

			break;

		case R.id.lllogin:
			setResult(SETTINGS);
			finish();
			break;

		default:
			break;
		}
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO 自动生成的方法存根
		switch (item.getItemId()) {
		case android.R.id.home:
			this.finish();
			break;
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO 自动生成的方法存根
		if (resultCode == 234) {
			setResult(SETTINGS);
			finish();
		}
	}
	
}
