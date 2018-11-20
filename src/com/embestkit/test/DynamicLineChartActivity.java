package com.embestkit.test;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import com.embestdkit.R;
import com.embestkit.login.BaseActivity;
import com.embestkit.util.DynamicLineChartManager;
import com.github.mikephil.charting.charts.LineChart;

public class DynamicLineChartActivity extends BaseActivity{

	
	 private DynamicLineChartManager dynamicLineChartManager1;
	    private DynamicLineChartManager dynamicLineChartManager2;
	    private List<Integer> list = new ArrayList<Integer>(); //数据集合
	    private List<String> names = new ArrayList<String>(); //折线名字集合
	    private List<Integer> colour = new ArrayList<Integer>();//折线颜色集合

	    @Override
	    protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.activity_dynamic_linechart);
	        LineChart mChart1 = (LineChart) findViewById(R.id.dynamic_chart1);
	        LineChart mChart2 = (LineChart) findViewById(R.id.dynamic_chart2);
	        //折线名字
	        names.add("温度");
	        names.add("压强");
	        names.add("其他");
	        //折线颜色
	        colour.add(Color.CYAN);
	        colour.add(Color.GREEN);
	        colour.add(Color.BLUE);

	        dynamicLineChartManager1 = new DynamicLineChartManager(mChart1, names.get(0), colour.get(0));
	        dynamicLineChartManager2 = new DynamicLineChartManager(mChart2, names, colour);

	        dynamicLineChartManager1.setYAxis(100, 0, 10);
	        dynamicLineChartManager2.setYAxis(100, 0, 10);

	        //死循环添加数据
	        new Thread(new Runnable() {
	            @Override
	            public void run() {
	                while (true) {
	                    try {
	                        Thread.sleep(500);
	                    } catch (InterruptedException e) {
	                        e.printStackTrace();
	                    }
	                    runOnUiThread(new Runnable() {
	                        @Override
	                        public void run() {
	                            list.add((int) (Math.random() * 50) + 10);
	                            list.add((int) (Math.random() * 80) + 10);
	                            list.add((int) (Math.random() * 100));
	                            dynamicLineChartManager2.addEntry(list);
	                            list.clear();
	                        }
	                    });
	                }
	            }
	        }).start();
	    }

	    //按钮点击添加数据
	    public void addEntry(View view) {
	        dynamicLineChartManager1.addEntry((int) (Math.random() * 100));
	    }
}
