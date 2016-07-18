package cn.com.bluemoon.lib.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;

import cn.com.bluemoon.lib.qrcode.R;

public class MonthDatePickerDialog extends CommonDatePickerDialog {

	private Context context;
	
	@SuppressLint("NewApi")
	public MonthDatePickerDialog(Context context, OnDateSetListener callBack, int year, int monthOfYear, int dayOfMonth) {  
        super(context, callBack, year, monthOfYear, dayOfMonth); 
        this.context = context;
        this.setTitle(year + context.getString(R.string.year) 
        		+ (monthOfYear + 1)+context.getString(R.string.month));  
          
        ((ViewGroup) ((ViewGroup) this.getDatePicker().getChildAt(0)).getChildAt(0)).getChildAt(2).setVisibility(View.GONE);  
    }  
  
    @Override  
    public void onDateChanged(DatePicker view, int year, int month, int day) {  
        super.onDateChanged(view, year, month, day);  
         this.setTitle(year + context.getString(R.string.year) 
		+ (month + 1)+context.getString(R.string.month));
    } 
	
	

}
