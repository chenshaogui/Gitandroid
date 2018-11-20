package com.embestdkit.zigbee;

import com.embestdkit.R;

import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.widget.TextView;

public class CoordinatorPresent extends NodePresent {
	static final String TAG = "CoordinatorPresent";
	TextView mDataView;

	/**
	 * Ð­µ÷Æ÷
	 * 
	 * @param n
	 */
	CoordinatorPresent(Node n) {
		super(R.layout.coordinator, n);
		mDataView = (TextView) super.mView.findViewById(R.id.tvCoordinatorData);
		mDataView.setMovementMethod(ScrollingMovementMethod.getInstance());
	}

	static int i = 0;

	@Override
	void procData(int req, byte[] dat) {
		String msg = new String(dat);
		Log.d(TAG, "proc data....");
		if (mDataView.getLineCount() > 5) {
			String s = mDataView.getText().toString();
			s = s.substring(s.indexOf('\n') + 1);
			s = s + i + ":" + msg;
			mDataView.setText(s);
			i++;
			return;
		}
		mDataView.append(i + ":" + msg);
		++i;
	}

	@Override
	void procAppMsgData(int addr, int cmd, byte[] dat) {

	}

	@Override
	void setdown() {

	}

	@Override
	void setup() {

	}
}
