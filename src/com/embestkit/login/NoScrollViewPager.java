package com.embestkit.login;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * @author Administrator
 *
 */
public class NoScrollViewPager extends ViewPager{
	
	private boolean noScroll=false;

	public NoScrollViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public NoScrollViewPager(Context context) {
		// TODO Auto-generated constructor stub
		super(context);
	}
	
	public void setNoScroll(boolean noScroll) {
		this.noScroll = noScroll;
	}
	
	/*
	 * 重写父类的方法 
	 * @see android.view.View#scrollTo(int, int)
	 */
	@Override
	public void scrollTo(int x, int y) {
		// TODO Auto-generated method stub
		super.scrollTo(x, y);
	}

	@Override
	public boolean onTouchEvent(MotionEvent arg0) {
		// TODO Auto-generated method stub
		if(noScroll)
			return false;
		else
		   return super.onTouchEvent(arg0);
	}
	
	@Override
	public boolean onInterceptTouchEvent(MotionEvent arg0) {
		// TODO Auto-generated method stub
		if(noScroll)
			return false;
		else
		return super.onInterceptTouchEvent(arg0);
	}
	
	@Override
	public void setCurrentItem(int item, boolean smoothScroll) {
		// TODO Auto-generated method stub
		super.setCurrentItem(item, smoothScroll);
	}
	
	@Override
	public void setCurrentItem(int item) {
		// TODO Auto-generated method stub
		super.setCurrentItem(item);
	}
	
}
