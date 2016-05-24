package cn.com.bluemoon.lib.view;

import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.WindowManager;

public class CommonTimePickerDialog extends TimePickerDialog {

	public CommonTimePickerDialog(Context context, OnTimeSetListener callBack,
			int hourOfDay, int minute, boolean is24HourView) {
		super(context, callBack, hourOfDay, minute, is24HourView);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
	}
	
	

}
