package com.embestkit.login;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class SensorUseDatabase extends SQLiteOpenHelper{

	private Context mContext;
	
	public SensorUseDatabase(Context context,String name,SQLiteDatabase.CursorFactory factory,int version) {
		// TODO �Զ����ɵĹ��캯�����
		super(context, name, factory, version);
		mContext=context;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO �Զ����ɵķ������
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO �Զ����ɵķ������
		
	}

}
