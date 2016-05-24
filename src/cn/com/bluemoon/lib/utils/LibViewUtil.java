package cn.com.bluemoon.lib.utils;

import android.app.Activity;
import android.content.Context;
import android.os.IBinder;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ListAdapter;
import android.widget.ListView;

public class LibViewUtil {


	public static void setListViewHeight2(ListView listView) { 
	    if(listView == null) return;

	    ListAdapter listAdapter = listView.getAdapter(); 
	    if (listAdapter == null) { 
	        // pre-condition 
	        return; 
	    } 

	    int totalHeight = 0; 
	    int desiredWidth = MeasureSpec.makeMeasureSpec(
	    		listView.getWidth(), MeasureSpec.AT_MOST);
	    for (int i = 0; i < listAdapter.getCount(); i++) { 
	        View listItem = listAdapter.getView(i, null, listView); 
	        listItem.measure(desiredWidth, 0); 
	        totalHeight += listItem.getMeasuredHeight(); 
	    } 

	    ViewGroup.LayoutParams params = listView.getLayoutParams(); 
	    params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1)); 
	    listView.setLayoutParams(params); 
	}
	

	public static void setListViewHeight(ListView listView) { 
	    if(listView == null) return;

	    ListAdapter listAdapter = listView.getAdapter(); 
	    if (listAdapter == null) { 
	        // pre-condition 
	        return; 
	    } 

	    int totalHeight = 0; 
	    for (int i = 0; i < listAdapter.getCount(); i++) { 
	        View listItem = listAdapter.getView(i, null, listView); 
	        listItem.measure(0, 0); 
	        totalHeight += listItem.getMeasuredHeight(); 
	    } 

	    ViewGroup.LayoutParams params = listView.getLayoutParams(); 
	    params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1)); 
	    listView.setLayoutParams(params); 
	}
	

	public static void hideIM(View v) {
		try {
			InputMethodManager im = (InputMethodManager) v.getContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
			IBinder windowToken = v.getWindowToken();
			if (windowToken != null) {
				im.hideSoftInputFromWindow(windowToken, 0);
			}
		} catch (Exception e) {

		}
	}
	
	public static void HideKeyboard(View v) {
		InputMethodManager imm = (InputMethodManager) v.getContext()
				.getSystemService(Context.INPUT_METHOD_SERVICE);     
        if (imm.isActive()) {     
            imm.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);   
        }  
    }
	public static void ShowKeyboard(View v) {
		InputMethodManager imm = (InputMethodManager)v.getContext()
				.getSystemService(Context.INPUT_METHOD_SERVICE);     
        imm.showSoftInput(v,InputMethodManager.SHOW_FORCED);    
    }

}
