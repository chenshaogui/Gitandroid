package com.embestdkit.zigbee;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import com.embestdkit.R;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.media.MediaPlayer;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SimpleAdapter;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ListView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.AdapterView;
import android.database.Cursor;
import android.text.InputType;

public class RfidPresent extends NodePresent {
	static final String TAG = "RFID";

	DatabaseHelper mCardDB = new DatabaseHelper();

	View mInfoView;
//	View mCtrlView;
	View mMagView;
//	ImageView mRfidImageView;
//	Button mBtnDisable;
//	Button mBtnEnable;

	boolean mSensorEnable = true;

	ListView mListView;
	SimpleAdapter mAdapter;

	TabHost tabHost;
	EditText mIDEditText;
	EditText mPhoneEditText;
	Button mAddBtn;

	/**
	 * ������Ƶģ��
	 * 
	 * @param n
	 */
	RfidPresent(Node n) {
		super(R.layout.rfid, n);
		mInfoView = super.mView.findViewById(R.id.rfidInfoView);
//		mCtrlView = super.mView.findViewById(R.id.rfidCtrlView);
		mMagView = super.mView.findViewById(R.id.rfidMagView);

//		mRfidImageView = (ImageView) mCtrlView.findViewById(R.id.rfidImageView);

		mListView = (ListView) mMagView.findViewById(R.id.listView);

		mIDEditText = (EditText) mMagView.findViewById(R.id.cardId);
		mPhoneEditText = (EditText) mMagView.findViewById(R.id.phone);
		mPhoneEditText.setInputType(InputType.TYPE_CLASS_NUMBER);
		mAddBtn = (Button) mMagView.findViewById(R.id.addcard);

//		mBtnEnable = (Button) mCtrlView.findViewById(R.id.rfidBtnEnable);
//		mBtnEnable.setOnClickListener(new OnClickListener() {
//			public void onClick(View arg0) {
//				// if (!mSensorEnable) {
//				RfidPresent.super.sendRequest(0x0002, new byte[] { 0x11, 0x01,
//						0x01 });
//				// }
//			}
//		});

//		mBtnDisable = (Button) mCtrlView.findViewById(R.id.rfidBtnDisable);
//		mBtnDisable.setOnClickListener(new OnClickListener() {
//			public void onClick(View arg0) {
//				// if (mSensorEnable) {
//				RfidPresent.super.sendRequest(0x0002, new byte[] { 0x11, 0x01,
//						0x00 });
//				// }
//			}
//		});

		tabHost = ((TabHost) super.mView.findViewById(android.R.id.tabhost));
		tabHost.setup();

		tabHost.addTab(tabHost.newTabSpec("0")
				// .setIndicator("", new BitmapDrawable(Resource.imageNodeInfo))
				.setIndicator("�����Ϣ")
				.setContent(new TabHost.TabContentFactory() {
					@Override
					public View createTabContent(String tag) {
						return mInfoView;
					}
				}));

//		tabHost.addTab(tabHost.newTabSpec("1")
//				// .setIndicator("", new BitmapDrawable(Resource.imageNodeInfo))
//				.setIndicator("������")
//				.setContent(new TabHost.TabContentFactory() {
//					public View createTabContent(String tag) {
//						return mCtrlView;
//					}
//				}));

		tabHost.addTab(tabHost.newTabSpec("1")
				// .setIndicator("", new
				// BitmapDrawable(Resource.imageNodeConfig))
				.setIndicator("��������")
				.setContent(new TabHost.TabContentFactory() {
					@Override
					public View createTabContent(String tag) {
						return mMagView;
					}
				}));

		tabHost.setCurrentTab(1);
		changeLayout(tabHost);
//		mMyHandler = new MyHandler();

		/* list view ���� */
		// for (int i=0; i<10; i++) {
		// HashMap<String, String> m = new HashMap<String, String>();
		// m.put("ID", String.format("%d", i));
		// m.put("PHONE", String.format("%d %d", i, i));
		// mList.add(m);
		// }
		// HashMap<String, String> m = new HashMap<String, String>();
		// m.put("ID", "�û�ID");
		// m.put("PHONE", "��غ���");
		// mList.add(m);

		mCardDB.init();
		mAdapter = new SimpleAdapter(ZigBeeTool.getInstance(), mCardDB.mList,
				R.layout.card_list_item, new String[] { "ID", "PHONE" },
				new int[] { R.id.ID, R.id.PHONE });

		mListView.setAdapter(mAdapter);
		mListView.setOnItemLongClickListener(new OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView parent, View view,
					int position, long id) {
				mCardDB.remove(mCardDB.mList.get(position).get("ID"));
				mCardDB.mList.remove(position);
				mAdapter.notifyDataSetChanged();
				return true;
			}
		});

		mAddBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (mPhoneEditText.getText().length() == 0
						|| mIDEditText.getText().length() == 0) {
					Toast.makeText(ZigBeeTool.getInstance(), "��ˢ�������������ֻ����룡",
							Toast.LENGTH_SHORT).show();
					return;
				}

				HashMap<String, String> m = new HashMap<String, String>();
				m.put("ID", mIDEditText.getText().toString());
				m.put("PHONE", mPhoneEditText.getText().toString());
				mCardDB.add(mIDEditText.getText().toString(), mPhoneEditText
						.getText().toString());
				mAdapter.notifyDataSetChanged();

				Toast.makeText(ZigBeeTool.getInstance(), "��ӿ��ųɹ���",
						Toast.LENGTH_SHORT).show();
				mPhoneEditText.setText("");
				mIDEditText.setText("");
			}
		});

		mP1.setOnCompletionListener(onCompletion);
		mP2.setOnCompletionListener(onCompletion);
	}

	// ���Ըı�TabHost�ĸ߶Ⱥ������С
	private void changeLayout(TabHost tabHost) {
		int count = tabHost.getTabWidget().getChildCount();// TabHost����һ��getTabWidget()�ķ���
		for (int i = 0; i < count; i++) {
			View view = tabHost.getTabWidget().getChildTabViewAt(i);
			view.getLayoutParams().height = 60;// �ı�TabHost�ĸ߶�
			view.setBackgroundColor(android.graphics.Color.BLACK);// ����ǰѡ�еı�ǩҳ����Ϊ��ɫ
			final TextView tv = (TextView) view.findViewById(android.R.id.title);
			tv.setTextSize(16);// �ı����ִ�С
		}
	}

	@Override
	void setup() {
		super.sendRequest(0x0001, new byte[] { 0x11, 0x01 });
		Message msg = Message.obtain();
//		mMyHandler.sendMessageDelayed(msg, 300);
	}

	@Override
	void setdown() {

	}

	@Override
	void procData(int req, byte[] dat) {

	}

	@Override
	void procAppMsgData(int addr, int cmd, byte[] dat) {
		// Log.d(TAG,"command:"+ cmd + " dat:" + Tool.byte2string(dat));
		int i = -1;
		if (addr != super.mNode.mNetAddr)
			return;
		if (cmd == 0x0003)
			i = 0;
		if (cmd == 0x8001 && dat[0] == 0)
			i = 1;
		if (cmd == 0x8002 && dat[0] == 0) {
			if (mSensorEnable) {
				// mCo2SensorImageView.setImageBitmap(Resource.imageCo2SensorBlue);
				mSensorEnable = false;
			} else {
				// mCo2SensorImageView.setImageBitmap(Resource.imageCo2SensorEnable);
				mSensorEnable = true;
			}
			return;
		}
		while (i >= 0 && i < dat.length) {
			int pid = Tool.builduInt(dat[i], dat[i + 1]);
			if (pid == 0x1101) {
				if (dat[i + 2] == 0)
					mSensorEnable = false;
				else
					mSensorEnable = true;
				i += 3;
			} else if (pid == 0x1110) {
				byte[] card = new byte[4];
				card[0] = dat[i + 2];
				card[1] = dat[i + 3];
				card[2] = dat[i + 4];
				card[3] = dat[i + 5];
				// String id = Tool.byte2string(card);
				String id = String.format("%02X%02X%02X%02X", card[0], card[1],
						card[2], card[3]);
				Log.d(TAG, "Card:" + id);
				procCardId(id);
				i += 6;
			} else
				return;
		}

	}

	static MediaPlayer mP1 = MediaPlayer.create(ZigBeeTool.getInstance()
			.getBaseContext(), R.raw.allow);
	static MediaPlayer mP2 = MediaPlayer.create(ZigBeeTool.getInstance()
			.getBaseContext(), R.raw.refuse);

	static MediaPlayer.OnCompletionListener onCompletion = new MediaPlayer.OnCompletionListener() {
		@Override
		public void onCompletion(MediaPlayer arg0) {
			try {
				arg0.prepare();
			} catch (IllegalStateException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	};

	void procCardId(String id) {
		if (1 == tabHost.getCurrentTab()) {
			mIDEditText.setText(id);
			if (mCardDB.find(mIDEditText.getText().toString()).length() != 0) {
				Toast.makeText(ZigBeeTool.getInstance(),
						"ID:" + mIDEditText.getText().toString() + " ��ע�ᣡ",
						Toast.LENGTH_SHORT).show();
				mIDEditText.setText("");
				return;
			}

		} else {
			String phone = mCardDB.find(id);
			String m;
			if (phone.length() == 0) {
				m = "ID:" + id + " δע��";
			} else {
				m = "ID:" + id + " Phone:" + phone;
			}
			Toast.makeText(ZigBeeTool.getInstance(), m, Toast.LENGTH_SHORT).show();
			if (phone.length() == 0) {
				mP2.start();
			} else {
				mP1.start();
//				if (((CheckBox) mCtrlView.findViewById(R.id.rfidMsgEnableCheckBox)).isChecked()) {
//					String msg = "ID:" + id + " ǩ���ɹ�!";
//					Tool.sendShortMessage(phone, msg);
//				}
			}
		}
	}

	boolean mShowBlue = true;
	boolean mExit = false;

//	class MyHandler extends Handler {
//		public void handleMessage(Message msg) {
//			if (mSensorEnable && mShowBlue) {
//				mRfidImageView.setImageBitmap(Resource.imageGasSensorBlue);
//			} else {
//				mRfidImageView.setImageBitmap(Resource.imageGasSensorEnable);
//			}
//			mShowBlue = !mShowBlue;
//			if (!mExit) {
//				Message msg2 = Message.obtain();
//				mMyHandler.sendMessageDelayed(msg2, 300);
//			}
//
//		}
//	}

//	MyHandler mMyHandler;
	
	/***********************************************************************
	 * ���ݿ����:�ڲ���
	 ***********************************************************************/

	private static class DatabaseHelper extends SQLiteOpenHelper {
		private static int version = 1;
		private static final String mDBName = "CardDB";

		ArrayList<HashMap<String, String>> mList = new ArrayList<HashMap<String, String>>();

		public DatabaseHelper() {
			super(ZigBeeTool.getInstance(), mDBName, null, version);
		}

		// �����ݿ��һ�����ɵ�ʱ���������������һ������������������������ݱ�
		@Override
		public void onCreate(SQLiteDatabase db) {
			// �������ݿ��SQL
			String sql = "CREATE TABLE tcard(id TEXT , phone TEXT )";
			Log.i(TAG, "tcard:createDB sql:" + sql);
			db.execSQL(sql);
			// db.close();
		}

		// �����ݿ���Ҫ����ʱ��Androidϵͳ���Զ��������������һ������������дɾ���������±�Ĳ���
		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		}

		public void add(String id, String phone) {
			SQLiteDatabase db = this.getWritableDatabase();
			String sql = String
					.format("INSERT INTO tcard(id, phone) VALUES('%s','%s')",
							id, phone);
			try {
				db.execSQL(sql);
			} catch (Exception e) {
				e.printStackTrace();
			}
			HashMap<String, String> m = new HashMap<String, String>();
			m.put("ID", id);
			m.put("PHONE", phone);
			mList.add(m);
			db.close();
		}

		public void remove(String id) {
			String[] pa = new String[1];
			pa[0] = id;
			SQLiteDatabase db = this.getWritableDatabase();
			db.delete("tcard", "id=?", pa);
			db.close();
		}

		public void init() {
			SQLiteDatabase db = this.getReadableDatabase();
			Cursor cr = db.query("tcard", new String[] { "id", "phone" }, null,
					null, null, null, null);
			cr.moveToFirst();
			int cnt = cr.getCount();
			for (int i = 0; i < cnt; i++) {
				HashMap<String, String> m = new HashMap<String, String>();
				String id = cr.getString(0);
				String phone = cr.getString(1);
				m.put("ID", id);
				m.put("PHONE", phone);
				this.mList.add(m);
				cr.moveToNext();
			}
			cr.close();
			db.close();
			return;
		}

		public String find(String id) {
			String phone = "";
			SQLiteDatabase db = this.getReadableDatabase();
			String[] pa = new String[1];
			pa[0] = id;
			Cursor cr = db.query("tcard", new String[] { "phone" }, "id=?", pa,
					null, null, null);
			cr.moveToFirst();
			int cnt = cr.getCount();
			if (cnt != 0) {
				phone = cr.getString(0);
			}
			cr.close();
			db.close();
			return phone;
		}
	}
}
