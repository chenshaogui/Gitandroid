package com.embestkit.login;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.embestdkit.R;
import com.embestdkit.zigbee.ZigBeeTool;

import android.app.Activity;
import android.app.LocalActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

public class MainFrameActivity extends BaseActivity{

	
	private int offset = 0;// ����ƫ����
	private int currIndex = 0;// ��ǰҳ�����
	private int bmpW;// ����ͼƬ���
	private ImageView cursor;// ����ͼƬ
	private int viewPagerSelected = 0;
	
	Context context=null;
	NoScrollViewPager pager;
	//�����������л�ҳ��
	TabHost tabHost;
	@SuppressWarnings("deprecation")
	LocalActivityManager manager=null;
	
	LinearLayout t1,t2,t3;
	public static Activity instance = null;
	private ImageView img1;
	private ImageView img2;
	private ImageView img3;
	private TextView tx1;
	private TextView tx2;
	private TextView tx3;
	
	//�������������
	Handler myHandler;  
	ZigBeeTool activity;
	MessageCenterActivity center;
	
	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_frame);
		//�õ���ǰ������
		context=MainFrameActivity.this;
		//�õ���ǰ��ʵ��
		instance=this;
		//��û����
		manager=new LocalActivityManager(this, true);
		manager.dispatchCreate(savedInstanceState);
		//Manager��������Ѿ�����ʼ������ʼ���Ĺ�������dispatchCreate()���������ȱ���ɵģ�
		initTextView();
		initPagerViewer();
		initHandler();
	}
	
	/**
	 * ��ʼ��HAndler
	 */
	private void initHandler() {
		// TODO �Զ����ɵķ������
		activity=(ZigBeeTool)manager.getActivity("A");
		center=(MessageCenterActivity) manager.getActivity("B");
		myHandler=activity.getMyhandler();
	}
	/**
	 * ��ʼ������
	 */
	private void initTextView() {
		t1 = (LinearLayout) findViewById(R.id.text1);
		t2 = (LinearLayout) findViewById(R.id.text2);
		t3 = (LinearLayout) findViewById(R.id.text3);

		img1 = (ImageView) findViewById(R.id.img1);
		img2 = (ImageView) findViewById(R.id.img2);
		img3 = (ImageView) findViewById(R.id.img3);

		tx1 = (TextView) findViewById(R.id.tx1);
		tx2 = (TextView) findViewById(R.id.tx2);
		tx3 = (TextView) findViewById(R.id.tx3);

		t1.setOnClickListener(new MyOnClickListener(0));
		t2.setOnClickListener(new MyOnClickListener(1));
		t3.setOnClickListener(new MyOnClickListener(2));

	}
	
	@SuppressWarnings("deprecation")
	private void initPagerViewer() {
		// TODO Auto-generated method stub
		pager=(NoScrollViewPager) findViewById(R.id.viewpage);
		pager.setNoScroll(true);
		
		final ArrayList<View> list=new ArrayList<View>();
		
		Intent intent = new Intent(context, ZigBeeTool.class);
		list.add(getView("A", intent));
		Intent intent2 = new Intent(context, MessageCenterActivity.class);
		list.add(getView("B", intent2));
		Intent intent3 = new Intent(context, PersonalCenterActivity.class);
		list.add(getView("C", intent3));

		pager.setAdapter(new MyPagerAdapter(list));
		pager.setCurrentItem(0);
		// t1.setBackgroundColor(getResources().getColor(R.color.gray));
		// t2.setBackgroundColor(getResources().getColor(R.color.white));
		// t3.setBackgroundColor(getResources().getColor(R.color.white));

		img1.setBackgroundResource(R.drawable.grid);
		img2.setBackgroundResource(R.drawable.message_grey);
		img3.setBackgroundResource(R.drawable.user_grey);

		tx1.setTextColor(getResources().getColor(R.color.black));
		tx2.setTextColor(getResources().getColor(R.color.gray));
		tx3.setTextColor(getResources().getColor(R.color.gray));
		//setActionBar(false, false, "�ҵ��豸");
		this.setTitle("�ҵ��豸");
	
		pager.setOnPageChangeListener(new MyOnPageChangeListener());
	}
	
	//�����Ƿ���
	/**
	 * ͨ��activity��ȡ��ͼ
	 * 
	 * @param id
	 * @param intent
	 * @return
	 */
	@SuppressWarnings("deprecation")
	private View getView(String id, Intent intent) {
		return manager.startActivity(id, intent).getDecorView();
	}
	
	
	public class MyOnClickListener implements View.OnClickListener{
		private int index=0;

		public MyOnClickListener(int i) {
			// TODO Auto-generated constructor stub
			index=i;
		}
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			pager.setCurrentItem(index);
			switch (index) {
			case 0:
				img1.setBackgroundResource(R.drawable.grid);
				img2.setBackgroundResource(R.drawable.message_grey);
				img3.setBackgroundResource(R.drawable.user_grey);

				tx1.setTextColor(getResources().getColor(R.color.black));
				tx2.setTextColor(getResources().getColor(R.color.gray));
				tx3.setTextColor(getResources().getColor(R.color.gray));
				break;
			case 1:
				img1.setBackgroundResource(R.drawable.grid_grey);
				img2.setBackgroundResource(R.drawable.message);
				img3.setBackgroundResource(R.drawable.user_grey);

				tx1.setTextColor(getResources().getColor(R.color.gray));
				tx2.setTextColor(getResources().getColor(R.color.black));
				tx3.setTextColor(getResources().getColor(R.color.gray));
				break;
			case 2:
				img1.setBackgroundResource(R.drawable.grid_grey);
				img2.setBackgroundResource(R.drawable.message_grey);
				img3.setBackgroundResource(R.drawable.user);

				tx1.setTextColor(getResources().getColor(R.color.gray));
				tx2.setTextColor(getResources().getColor(R.color.gray));
				tx3.setTextColor(getResources().getColor(R.color.black));
				break;

			default:
				break;
			}
		}
		// TODO Auto-generated method stub

	}
	
	public class MyPagerAdapter extends PagerAdapter{


		List<View> list=new ArrayList<View>();
		public MyPagerAdapter(ArrayList<View> list) {
			// TODO Auto-generated constructor stub
			this.list=list;
		}
		@Override
		public void destroyItem(View container, int position, Object aobject) {
			// TODO Auto-generated method stub
			ViewPager pViewPager=(ViewPager) container;
			pViewPager.removeView(list.get(position));
		}

		@Override
		public void finishUpdate(View arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return list.size();
		}

		@Override
		public Object instantiateItem(View arg0, int position) {
			// TODO Auto-generated method stub
			ViewPager pViewPager=((ViewPager) arg0);
			pViewPager.addView(list.get(position));
			return list.get(position);
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			// TODO Auto-generated method stub
			return arg0==arg1;
		}

		@Override
		public void restoreState(Parcelable arg0, ClassLoader arg1) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public Parcelable saveState() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void startUpdate(View arg0) {
			// TODO Auto-generated method stub
			
		}
		
	}
	
	 /**
	 * @author Administrator
	 *ҳ���л�����
	 */
	public class MyOnPageChangeListener implements OnPageChangeListener{
		int one = offset * 2 + bmpW;// ҳ��1 -> ҳ��2 ƫ����
		int two = one * 2;// ҳ��1 -> ҳ��3 ƫ����

		@Override
		public void onPageScrollStateChanged(int arg0) {
			// TODO Auto-generated method stub
			
			
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onPageSelected(int arg0) {
			// TODO Auto-generated method stub
			viewPagerSelected = arg0;

			Animation animation = null;
			switch (arg0) {
			case 0:
				/*if (currIndex == 1) {

					animation = new TranslateAnimation(one, 0, 0, 0);
				} else if (currIndex == 2) {
					animation = new TranslateAnimation(two, 0, 0, 0);
				}*/
				activity.onResume();
				//setActionBar(false, false, "�ҵ��豸");
				MainFrameActivity.this.setTitle("�ҵ��豸");
				
				break;
			case 1:
/*
				if (currIndex == 0) {
					animation = new TranslateAnimation(offset, one, 0, 0);
				} else if (currIndex == 2) {
					animation = new TranslateAnimation(two, one, 0, 0);
				}
				//setActionBar(false, false, "��Ϣ����");*/
				activity.onPause();
				center.onResume();
				MainFrameActivity.this.setTitle("��Ϣ����");
				break;

			case 2:
				activity.onPause();
				/*
				if (currIndex == 0) {
					animation = new TranslateAnimation(offset, two, 0, 0);
				} else if (currIndex == 1) {
					animation = new TranslateAnimation(one, two, 0, 0);
				}
				//setActionBar(false, false, "��������");
*/				MainFrameActivity.this.setTitle("��������");
				break;
			}
			/*currIndex = arg0;
			animation.setFillAfter(true);// True ͼƬͣ�ڶ�������λ��
			animation.setDuration(300);
			cursor.startAnimation(animation);*/
		}
		
	}
	
	
	/**
	 * �˵������ؼ���Ӧ
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			exitBy2Click();
		}
		return false;
	}
	
	/**
	 * ˫���˳�����
	 */
	private static Boolean isExit = false;
	
	public void exitBy2Click() {
		Timer tExit = null;
		if (isExit == false) {
			isExit = true; // ׼���˳���
			String doubleClick;
			/*if (!TextUtils.isEmpty(spf.getString("UserName", ""))
					&& !TextUtils.isEmpty(spf.getString("PassWord", ""))) {
				//doubleClick = (String) getText('�ٴε������ע����¼');
			else {
					//doubleClick = (String) getText(R.string.doubleclick_back);
				}
			}*/

			Toast.makeText(this, "˫���Ƴ�����", 2000).show();
			tExit = new Timer();
			tExit.schedule(new TimerTask() {
				@Override
				public void run() {
					isExit = false; // ȡ���˳�
				}
			}, 2000); // ���2������û�а��·��ؼ�����������ʱ��ȡ�����ղ�ִ�е�����

		
		} else {
			logoutToClean();
		}
	}


	/** ע������ */
	void logoutToClean() {
		spf.edit().putString("UserName", "").commit();
		spf.edit().putString("PassWord", "").commit();
		finish();
	}
	
	@Override
	public void onPause() {
		super.onPause();
		//activity.onPause();
	}

	@Override
	protected void onResume() {
		
		// TODO Auto-generated method stub
		super.onResume();
		/*switch (viewPagerSelected) {
		case 0:
			activity.onResume();
			break;

		case 1:
			center.onResume();
			break;

		default:
			break;
		}*/

	}
	

	
}
