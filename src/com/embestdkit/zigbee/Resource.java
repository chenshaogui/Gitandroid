package com.embestdkit.zigbee;

import com.embestdkit.R;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;

public class Resource {

	final static Bitmap imageCoordinator = BitmapFactory.decodeResource(
			ZigBeeTool.getInstance().getResources(), R.drawable.coordinator);

	final static Bitmap imageDeviceTempSensor = BitmapFactory.decodeResource(
			ZigBeeTool.getInstance().getResources(), R.drawable.devtempsensor);
	final static Bitmap imageDeviceLight = BitmapFactory.decodeResource(
			ZigBeeTool.getInstance().getResources(), R.drawable.devlight);
	final static Bitmap imageDeviceSwitch = BitmapFactory.decodeResource(
			ZigBeeTool.getInstance().getResources(), R.drawable.devswitch);
	final static Bitmap imageDevGasSensor = BitmapFactory.decodeResource(
			ZigBeeTool.getInstance().getResources(), R.drawable.devgassensor);
	final static Bitmap imageDevInfrared = BitmapFactory.decodeResource(
			ZigBeeTool.getInstance().getResources(), R.drawable.devinfrared);
	// final static Bitmap imageDevDistance =
	// BitmapFactory.decodeResource(ZigBeeTool.
	// getInstance().getResources(), R.drawable.devdistancesensor);
	final static Bitmap imageDevHumidity = BitmapFactory.decodeResource(
			ZigBeeTool.getInstance().getResources(), R.drawable.devhumidity);
	final static Bitmap imageDevRfid = BitmapFactory.decodeResource(ZigBeeTool
			.getInstance().getResources(), R.drawable.devrfid);
	final static Bitmap imageDevUnknow = BitmapFactory.decodeResource(
			ZigBeeTool.getInstance().getResources(), R.drawable.devunknow);

	final static Bitmap imageLightOn = BitmapFactory.decodeResource(ZigBeeTool
			.getInstance().getResources(), R.drawable.lighton);
	final static Bitmap imageLightOff = BitmapFactory.decodeResource(ZigBeeTool
			.getInstance().getResources(), R.drawable.lightoff);

	final static Bitmap imageSwitchEnable = BitmapFactory.decodeResource(
			ZigBeeTool.getInstance().getResources(), R.drawable.switch1);
	final static Bitmap imageSwitchDisable = BitmapFactory.decodeResource(
			ZigBeeTool.getInstance().getResources(), R.drawable.switch2);

	final static Bitmap imageInfraredEnable = BitmapFactory.decodeResource(
			ZigBeeTool.getInstance().getResources(), R.drawable.infraredenable);
	// final static Bitmap imageInfraredDisable =
	// BitmapFactory.decodeResource(ZigBeeTool.
	// getInstance().getResources(), R.drawable.infrareddisable);
	final static Bitmap imageInfraredAlarm = BitmapFactory.decodeResource(
			ZigBeeTool.getInstance().getResources(), R.drawable.infraredalarm);
	final static Bitmap imageInfraredBlue = BitmapFactory.decodeResource(
			ZigBeeTool.getInstance().getResources(),
			R.drawable.infraredalarmblue);

	final static Bitmap imageGasSensorEnable = BitmapFactory
			.decodeResource(ZigBeeTool.getInstance().getResources(),
					R.drawable.gassensorenable);
	final static Bitmap imageGasSensorBlue = BitmapFactory.decodeResource(
			ZigBeeTool.getInstance().getResources(), R.drawable.gassensorblue);
	final static Bitmap imageGasSensorAlarm = BitmapFactory.decodeResource(
			ZigBeeTool.getInstance().getResources(), R.drawable.gassensoralarm);

	// final static Bitmap imageNodeConfig =
	// BitmapFactory.decodeResource(ZigBeeTool.
	// getInstance().getResources(), R.drawable.nodeconfig);
	// final static Bitmap imageNodeInfo =
	// BitmapFactory.decodeResource(ZigBeeTool.
	// getInstance().getResources(), R.drawable.nodeinfo);
	// final static Bitmap imageNodeCurve =
	// BitmapFactory.decodeResource(ZigBeeTool.
	// getInstance().getResources(), R.drawable.nodecurve);
	// final static Bitmap imageNodeValue =
	// BitmapFactory.decodeResource(ZigBeeTool.
	// getInstance().getResources(), R.drawable.nodevalue);

	// final static Bitmap imageDevFpm10a =
	// BitmapFactory.decodeResource(ZigBeeTool.
	// getInstance().getResources(), R.drawable.devfpm10a);
	//
	// final static Bitmap imageDevHighfrid =
	// BitmapFactory.decodeResource(ZigBeeTool.
	// getInstance().getResources(), R.drawable.devfpm10a);
	final static Bitmap imageDevThreeAcc = BitmapFactory.decodeResource(
			ZigBeeTool.getInstance().getResources(), R.drawable.devrfid);
	final static Bitmap imageDevPressure = BitmapFactory.decodeResource(
			ZigBeeTool.getInstance().getResources(), R.drawable.devrfid);
	final static Bitmap imageDevRain = BitmapFactory.decodeResource(ZigBeeTool
			.getInstance().getResources(), R.drawable.devrainsensor);

	final static Bitmap imageDevVIBRATE = BitmapFactory.decodeResource(
			ZigBeeTool.getInstance().getResources(),
			R.drawable.devvibratesensor);
	// final static Bitmap imageDevALCOHOL =
	// BitmapFactory.decodeResource(ZigBeeTool.
	// getInstance().getResources(), R.drawable.devalcoholsensor);
	final static Bitmap imageDevHALL = BitmapFactory.decodeResource(ZigBeeTool
			.getInstance().getResources(), R.drawable.devalhallsensor);
	final static Bitmap imageDevDYNAMO = BitmapFactory.decodeResource(
			ZigBeeTool.getInstance().getResources(),
			R.drawable.devaldynamosensor);
	final static Bitmap imageDevDECIBLE = BitmapFactory.decodeResource(
			ZigBeeTool.getInstance().getResources(),
			R.drawable.devaldecibelsensor);
	final static Bitmap imageDevFLAME = BitmapFactory.decodeResource(ZigBeeTool
			.getInstance().getResources(), R.drawable.devflamesensor);
	final static Bitmap imageDevULTRASONIC = BitmapFactory.decodeResource(
			ZigBeeTool.getInstance().getResources(),
			R.drawable.devalultrasensor);
	final static Bitmap imageDevIRREMOTE = BitmapFactory.decodeResource(
			ZigBeeTool.getInstance().getResources(),
			R.drawable.devalirremotesensor);

	final static Bitmap imageDynamoMon = BitmapFactory.decodeResource(
			ZigBeeTool.getInstance().getResources(), R.drawable.dynamo_move_on);
	final static Bitmap imageDynamoMoff = BitmapFactory
			.decodeResource(ZigBeeTool.getInstance().getResources(),
					R.drawable.dynamo_move_off);
	final static Bitmap imageDynamoOleft = BitmapFactory.decodeResource(
			ZigBeeTool.getInstance().getResources(),
			R.drawable.dynamo_orientation_left);
	final static Bitmap imageDynamoOright = BitmapFactory.decodeResource(
			ZigBeeTool.getInstance().getResources(),
			R.drawable.dynamo_orientation_right);
	final static Bitmap imageDynamoSadd = BitmapFactory.decodeResource(
			ZigBeeTool.getInstance().getResources(),
			R.drawable.dynamo_speed_add);
	final static Bitmap imageDynamoSsub = BitmapFactory.decodeResource(
			ZigBeeTool.getInstance().getResources(),
			R.drawable.dynamo_speed_sub);

	// final static Bitmap imageRainOn =
	// BitmapFactory.decodeResource(ZigBeeTool.
	// getInstance().getResources(), R.drawable.rainon);
	// final static Bitmap imageRainOff =
	// BitmapFactory.decodeResource(ZigBeeTool.
	// getInstance().getResources(), R.drawable.rainoff);
	// final static Bitmap imageLightStrong =
	// BitmapFactory.decodeResource(ZigBeeTool.
	// getInstance().getResources(), R.drawable.lightstrong);
	// final static Bitmap imageLightWeak =
	// BitmapFactory.decodeResource(ZigBeeTool.
	// getInstance().getResources(), R.drawable.lightweak);
	// final static Bitmap imageDecibelOn =
	// BitmapFactory.decodeResource(ZigBeeTool.
	// getInstance().getResources(), R.drawable.decibel_on);
	// final static Bitmap imageDecibelOff =
	// BitmapFactory.decodeResource(ZigBeeTool.
	// getInstance().getResources(), R.drawable.decibel_off);

	/*
	 * final static Bitmap imageLight[] = {
	 * BitmapFactory.decodeResource(ZigBeeTool. getInstance().getResources(),
	 * R.drawable.lightoff), BitmapFactory.decodeResource(ZigBeeTool.
	 * getInstance().getResources(), R.drawable.lighton) };
	 */
	/**
	 * 将mb缩放成想要的尺寸
	 * 
	 * @param bm
	 * @param newWidth
	 *            ，想要得到的尺寸
	 * @param newHeight
	 * @return
	 */
	static Bitmap scale(Bitmap bm, int newWidth, int newHeight) { // bm现在的大小
		int width = bm.getWidth();
		int height = bm.getHeight();

		if (newWidth == width && newHeight == height)
			return bm;
		float scaleWidth = ((float) newWidth) / width;
		float scaleHeight = ((float) newHeight) / height;

		// 取得想要缩放的matrix参数
		Matrix matrix = new Matrix();
		matrix.postScale(scaleWidth, scaleHeight);
		// 得到新的图片
		Bitmap newbm = Bitmap.createBitmap(bm, 0, 0, width, height, matrix,
				true);
		return newbm;
	}

}
