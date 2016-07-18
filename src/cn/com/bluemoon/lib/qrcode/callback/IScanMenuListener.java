package cn.com.bluemoon.lib.qrcode.callback;

import android.widget.Button;
import android.widget.ImageButton;

public interface IScanMenuListener {

	public void closeScan();
	public void openLight(ImageButton btn);
	public void openPick(ImageButton btn);
	public void onClick(Button btn);
}
