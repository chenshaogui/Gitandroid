package com.embestkit.util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;



public class DynamicLineChartManager {

	    private LineChart lineChart;
	    private YAxis leftAxis;
	    private YAxis rightAxis;
	    private XAxis xAxis;
	    private LineData lineData;
	    private LineDataSet lineDataSet;
	    private List<ILineDataSet> lineDataSets = new ArrayList<ILineDataSet>();
	    private SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");//�������ڸ�ʽ  
	    private List<String> timeList = new ArrayList<String>(); //�洢x���ʱ��

	    //һ������
	    public DynamicLineChartManager(LineChart mLineChart, String name, int color) {
	        this.lineChart = mLineChart;
	        leftAxis = lineChart.getAxisLeft();
	        rightAxis = lineChart.getAxisRight();
	        xAxis = lineChart.getXAxis();
	        initLineChart();
	        initLineDataSet(name, color);
	    }

	    //��������
	    public DynamicLineChartManager(LineChart mLineChart, List<String> names, List<Integer> colors) {
	        this.lineChart = mLineChart;
	        leftAxis = lineChart.getAxisLeft();
	        rightAxis = lineChart.getAxisRight();
	        xAxis = lineChart.getXAxis();
	        initLineChart();
	        initLineDataSet(names, colors);
	    }

	    /**
	     * ��ʼ��LineChar
	     */
	    private void initLineChart() {

	        lineChart.setDrawGridBackground(false);
	        //��ʾ�߽�
	        lineChart.setDrawBorders(true);
	        //����ͼ�� ��ǩ ����
	        Legend legend = lineChart.getLegend();
	        legend.setForm(Legend.LegendForm.LINE);
	        legend.setTextSize(11f);
	        //��ʾλ��
	        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
	        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
	        legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
	        legend.setDrawInside(false);

	        //X��������ʾλ���ڵײ�
	        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
	        xAxis.setGranularity(1f);
	        xAxis.setLabelCount(10);

	        xAxis.setValueFormatter(new IAxisValueFormatter() {
	            @Override
	            public String getFormattedValue(float value, AxisBase axis) {
	                return timeList.get((int) value % timeList.size());
	            }
	        });

	        //��֤Y���0��ʼ����Ȼ������һ��
	        leftAxis.setAxisMinimum(0f);
	        rightAxis.setAxisMinimum(0f);
	    }

	    /**
	     * ��ʼ������(һ����)
	     *
	     * @param name
	     * @param color
	     */
	    private void initLineDataSet(String name, int color) {

	        lineDataSet = new LineDataSet(null, name);
	        lineDataSet.setLineWidth(1.5f);
	        lineDataSet.setCircleRadius(1.5f);
	        lineDataSet.setColor(color);
	        lineDataSet.setCircleColor(color);
	        lineDataSet.setHighLightColor(color);
	        //�����������
	        lineDataSet.setDrawFilled(true);
	        lineDataSet.setAxisDependency(YAxis.AxisDependency.LEFT);
	        lineDataSet.setValueTextSize(10f);
	        lineDataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
	        //���һ���յ� LineData
	        lineData = new LineData();
	        lineChart.setData(lineData);
	        lineChart.invalidate();

	    }

	    /**
	     * ��ʼ�����ߣ������ߣ�
	     *
	     * @param names
	     * @param colors
	     */
	    private void initLineDataSet(List<String> names, List<Integer> colors) {

	        for (int i = 0; i < names.size(); i++) {
	            lineDataSet = new LineDataSet(null, names.get(i));
	            lineDataSet.setColor(colors.get(i));
	            lineDataSet.setLineWidth(1.5f);
	            lineDataSet.setCircleRadius(1.5f);
	            lineDataSet.setColor(colors.get(i));

	            lineDataSet.setDrawFilled(true);
	            lineDataSet.setCircleColor(colors.get(i));
	            lineDataSet.setHighLightColor(colors.get(i));
	            lineDataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
	            lineDataSet.setAxisDependency(YAxis.AxisDependency.LEFT);
	            lineDataSet.setValueTextSize(10f);
	            lineDataSets.add(lineDataSet);

	        }
	        //���һ���յ� LineData
	        lineData = new LineData();
	        lineChart.setData(lineData);
	        lineChart.invalidate();
	    }

	    /**
	     * ��̬������ݣ�һ������ͼ��
	     *
	     * @param number
	     */
	    public void addEntry(int number) {

	        //�ʼ��ʱ������ lineDataSet��һ��lineDataSet ����һ���ߣ�
	        if (lineDataSet.getEntryCount() == 0) {
	            lineData.addDataSet(lineDataSet);
	        }
	        lineChart.setData(lineData);
	        //���⼯�����ݹ��࣬��ʱ��գ��������Ĵ�������֪����û���ã��������������ˣ�
	        if (timeList.size() > 11) {
	            timeList.clear();
	        }

	        timeList.add(df.format(System.currentTimeMillis()));

	        Entry entry = new Entry(lineDataSet.getEntryCount(), number);
	        lineData.addEntry(entry, 0);
	        //֪ͨ�����Ѿ��ı�
	        lineData.notifyDataChanged();
	        lineChart.notifyDataSetChanged();
	        //����������ͼ����ʾ���������
	        lineChart.setVisibleXRangeMaximum(10);
	        //�Ƶ�ĳ��λ��
	        lineChart.moveViewToX(lineData.getEntryCount() - 5);
	    }

	    /**
	     * ��̬������ݣ���������ͼ��
	     *
	     * @param numbers
	     */
	    public void addEntry(List<Integer> numbers) {

	        if (lineDataSets.get(0).getEntryCount() == 0) {
	            lineData = new LineData(lineDataSets);
	            lineChart.setData(lineData);
	        }
	        if (timeList.size() > 11) {
	            timeList.clear();
	        }
	        timeList.add(df.format(System.currentTimeMillis()));
	        for (int i = 0; i < numbers.size(); i++) {
	            Entry entry = new Entry(lineDataSet.getEntryCount(), numbers.get(i));
	            lineData.addEntry(entry, i);
	            lineData.notifyDataChanged();
	            lineChart.notifyDataSetChanged();
	            lineChart.setVisibleXRangeMaximum(6);
	            lineChart.moveViewToX(lineData.getEntryCount() - 5);
	        }
	    }

	    /**
	     * ����Y��ֵ
	     *
	     * @param max
	     * @param min
	     * @param labelCount
	     */
	    public void setYAxis(float max, float min, int labelCount) {
	        if (max < min) {
	            return;
	        }
	        leftAxis.setAxisMaximum(max);
	        leftAxis.setAxisMinimum(min);
	        leftAxis.setLabelCount(labelCount, false);

	        rightAxis.setAxisMaximum(max);
	        rightAxis.setAxisMinimum(min);
	        rightAxis.setLabelCount(labelCount, false);
	        lineChart.invalidate();
	    }

	    /**
	     * ���ø�������
	     *
	     * @param high
	     * @param name
	     */
	    public void setHightLimitLine(float high, String name, int color) {
	        if (name == null) {
	            name = "��������";
	        }
	        LimitLine hightLimit = new LimitLine(high, name);
	        hightLimit.setLineWidth(4f);
	        hightLimit.setTextSize(10f);
	        hightLimit.setLineColor(color);
	        hightLimit.setTextColor(color);
	        leftAxis.addLimitLine(hightLimit);
	        lineChart.invalidate();
	    }

	    /**
	     * ���õ�������
	     *
	     * @param low
	     * @param name
	     */
	    public void setLowLimitLine(int low, String name) {
	        if (name == null) {
	            name = "��������";
	        }
	        LimitLine hightLimit = new LimitLine(low, name);
	        hightLimit.setLineWidth(4f);
	        hightLimit.setTextSize(10f);
	        leftAxis.addLimitLine(hightLimit);
	        lineChart.invalidate();
	    }

	    /**
	     * ����������Ϣ
	     *
	     * @param str
	     */
	    public void setDescription(String str) {
	        Description description = new Description();
	        description.setText(str);
	        lineChart.setDescription(description);
	        lineChart.invalidate();
	    }
}
