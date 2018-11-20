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
		// TODO �Զ����ɵĹ��캯�����
	}
	
	 public TitleLayout(final Context context, AttributeSet attrs) {
	        super(context, attrs);
	        //���벼��
	        LayoutInflater.from(context).inflate(R.layout.title_bar,this);
	        tv_title=(TextView)findViewById(R.id.title_bar);
	    }
	//��ʾ��ĵı���
	    public void setTitle(String title)
	    {
	        if(!TextUtils.isEmpty(title))
	        {
	            tv_title.setText(title);
	        }
	    }

}
