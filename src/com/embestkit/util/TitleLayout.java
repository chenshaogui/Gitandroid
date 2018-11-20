package com.embestkit.util;

import com.embestdkit.R;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

public class TitleLayout extends LinearLayout {

	private TextView tv_title;
	public TitleLayout(Context context) {
		super(context,null);
		// TODO 自动生成的构造函数存根
	}
	
	 public TitleLayout(final Context context, AttributeSet attrs) {
	        super(context, attrs);
	        //引入布局
	        LayoutInflater.from(context).inflate(R.layout.title_bar,this);
	        tv_title=(TextView)findViewById(R.id.title_bar);
	    }
	//显示活的的标题
	    public void setTitle(String title)
	    {
	        if(!TextUtils.isEmpty(title))
	        {
	            tv_title.setText(title);
	        }
	    }

}
