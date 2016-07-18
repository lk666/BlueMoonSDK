package cn.com.bluemoon.lib.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.google.zxing.Result;

import cn.com.bluemoon.lib.qrcode.R;
import cn.com.bluemoon.lib.qrcode.utils.BarcodeUtil;
import cn.com.bluemoon.lib.utils.LibImageUtil;
import cn.com.bluemoon.lib.utils.LibPublicUtil;

public class SavePicDialog {

	private LinearLayout ll_popup;
	private PopupWindow pop;
	private Context context;
	private SavePicDialogListener listener;
	private Button btnDecode;
	private View lineDecode;
	private Bitmap bm;
	private String savePath;
	
	public SavePicDialog(Context context)
	{
		this.context = context;
		popupInit();
	}
	
	public SavePicDialog(Context context,Bitmap bm,String savePath)
	{
		this.context = context;
		this.bm = bm;
		this.savePath = savePath;
		popupInit();
	}
	
	public SavePicDialog(Context context,Bitmap bm,String savePath,SavePicDialogListener listener)
	{
		this.context = context;
		this.bm = bm;
		this.savePath = savePath;
		this.listener = listener;
		popupInit();
	}
	
	public void setSavePicDialogListener(SavePicDialogListener listener){
		this.listener = listener;
	}
	
	public void setBitmap(Bitmap bm){
		this.bm = bm;
	}
	
	public void setSavePath(String savePath){
		this.savePath = savePath;
	}
	
	public void isDecode(boolean isDecode){
		if(isDecode){
			btnDecode.setVisibility(View.VISIBLE);
			lineDecode.setVisibility(View.VISIBLE);
		}else{
			btnDecode.setVisibility(View.GONE);
			lineDecode.setVisibility(View.GONE);
		}
	}

	public void getPic(View v) {
		ll_popup.startAnimation(AnimationUtils.loadAnimation(context,
				R.anim.activity_translate_in_p));
		pop.showAtLocation(v, Gravity.CENTER, 0, 0);
	}
	
	public void popupInit() {

		pop = new PopupWindow(context);

		LayoutInflater inflater = LayoutInflater.from(context);
		View view = inflater.inflate(R.layout.dialog_save, null);

		ll_popup = (LinearLayout) view.findViewById(R.id.ll_popup);

		pop.setWidth(LayoutParams.MATCH_PARENT);
		pop.setHeight(LayoutParams.MATCH_PARENT);
		pop.setFocusable(true);
		pop.setOutsideTouchable(true);
		pop.setContentView(view);
//		pop.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.bg_transparent));
		pop.setBackgroundDrawable(null);
		RelativeLayout parent = (RelativeLayout) view.findViewById(R.id.parent);
		Button btnSave = (Button) view.findViewById(R.id.btn_save);
		Button btnCancel = (Button) view.findViewById(R.id.btn_cancel);
		btnDecode =(Button)view.findViewById(R.id.btn_decode);
		lineDecode = view.findViewById(R.id.line_decode);
		parent.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				pop.dismiss();
				ll_popup.clearAnimation();
			}
		});
		btnSave.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				pop.dismiss();
				ll_popup.clearAnimation();
				save();
			}
		});
		btnDecode.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				pop.dismiss();
				ll_popup.clearAnimation();
				decode();
			}
		});
		btnCancel.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				pop.dismiss();
				ll_popup.clearAnimation();
			}
		});
	}

	private void decode() {
		try {
			if (bm != null) {
				Result result = BarcodeUtil.decodeBitmap(bm);
				if (result != null) {
					if(listener!=null){
						listener.decodePic(result);
					}else{
						LibPublicUtil.showMessage(context, 
								context.getString(R.string.decode_success), result.getText());
					}
					return;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(listener!=null){
			listener.decodePic(null);
		}else{
			LibPublicUtil.showToast(context, context.getString(R.string.decode_fail));
		}
	}

	private void save()
	{
		if(bm!=null&&savePath!=null
				&&LibImageUtil.savaBitmap(bm, savePath)){
			LibImageUtil.refreshImg(context, savePath);
			if(listener!=null){
				listener.savePic(true);
			}else{
				LibPublicUtil.showToast(context, context.getString(R.string.save_success));
			}
			return;
		}
		if(listener!=null){
			listener.savePic(true);
		}else{
			LibPublicUtil.showToast(context, context.getString(R.string.save_fail));
		}
	}
	
	public interface SavePicDialogListener{
		public void savePic(boolean isSuccess);
		public void decodePic(Result result);
	}
}
