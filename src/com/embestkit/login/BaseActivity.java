package com.embestkit.login;

import com.embestdkit.R;
import com.embestkit.util.TitleLayout;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.SpannableString;
import android.text.Spanned;

public class BaseActivity extends Activity{

	/** 存储器默认名称 */
	public static final String SPF_Name = "user";

	/** Toast time */
	public int toastTime = 2000;

	/** 存储器 */
	public SharedPreferences spf;

	/** 等待框 */
	public ProgressDialog progressDialog;

	/** 标题栏 */
	

	/** 实现WXEntryActivity与GosUserLoginActivity共用 */
	public static Handler baseHandler;

	public static boolean isclean = false;

	public void setBaseHandler(Handler basehandler) {
		if (null != basehandler) {
			baseHandler = basehandler;
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		/**
		 * 设置为竖屏
		 */
		if (getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		}

		spf = getSharedPreferences(SPF_Name, Context.MODE_PRIVATE);
		
		// 初始化
		setProgressDialog();
		
	}
	/**
	 * 设置ProgressDialog
	 */
	public void setProgressDialog() {
		progressDialog = new ProgressDialog(this);
		progressDialog.setMessage("正在加载");
		progressDialog.setCanceledOnTouchOutside(false);
	}

	/**
	 * 设置ProgressDialog
	 * 
	 * @param Message
	 * @param Cancelable
	 * @param CanceledOnTouchOutside
	 */
	public void setProgressDialog(String Message, boolean Cancelable, boolean CanceledOnTouchOutside) {
		progressDialog = new ProgressDialog(this);
		progressDialog.setMessage(Message);
		progressDialog.setCancelable(Cancelable);
		progressDialog.setCanceledOnTouchOutside(CanceledOnTouchOutside);
	}
	
	/**
	 * 设置ActionBar
	 */
	/*public void setActionBar(CharSequence Title){
		SpannableString ssTitle=new SpannableString(Title);
		ssTitle.setSpan(Color.parseColor("#0099EE"), 0, ssTitle.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
		actionBar=getSupportActionBar();
		//actionBar.setBackgroundDrawable(getResources().getDrawable(R.color.blue));
		//actionBar.setHomeButtonEnabled(false);
		actionBar.setTitle(ssTitle);
		//actionBar.setDisplayShowHomeEnabled(true);
	}*/
	
}
