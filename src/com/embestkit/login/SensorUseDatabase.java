package com.embestkit.login;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class SensorUseDatabase extends SQLiteOpenHelper{

	private Context mContext;
	
	public SensorUseDatabase(Context context,String name,SQLiteDatabase.CursorFactory factory,int version) {
		// TODO 自动生成的构造函数存根
		super(context, name, factory, version);
		mContext=context;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO 自动生成的方法存根
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO 自动生成的方法存根
		
	}

}
