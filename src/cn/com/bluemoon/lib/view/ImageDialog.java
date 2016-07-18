package cn.com.bluemoon.lib.view;

import android.annotation.SuppressLint;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import cn.com.bluemoon.lib.callback.DrawableCallback;
import cn.com.bluemoon.lib.callback.ImageDialogCallback;
import cn.com.bluemoon.lib.qrcode.R;
import cn.com.bluemoon.lib.utils.DownImageHelper;

@SuppressLint("NewApi")
public class ImageDialog extends DialogFragment {

	private View view;
	private Context context;
	private String codeUrl;
	private String savePath;
	private String loadString;
	private Bitmap bm;
	private ImageView imgView;
	private TextView txtLoad;
	private ImageDialogCallback cb;
	private SavePicDialog savePicDialog;

	public ImageDialog() {
		super();
	}


	public ImageDialog(Context context, ImageDialogCallback cb) {
		this.context = context;
		this.cb = cb;
	}
	
	public void setCallback(ImageDialogCallback cb){
		this.cb = cb;
	}


	public void setLoadString(String loadString) {
		this.loadString = loadString;
	}


	public void setCodeUrl(String codeUrl) {
		this.codeUrl = codeUrl;
		this.bm = null;
	}


	public void setBitmap(Bitmap bm) {
		this.bm = bm;
		this.codeUrl = null;
	}


	public void setSavePath(String savePath) {
		this.savePath = savePath;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		view = inflater.inflate(R.layout.dialog_image, container, false);
		imgView = (ImageView) view.findViewById(R.id.img_pic);
		txtLoad = (TextView) view.findViewById(R.id.txt_load);
		view.setOnClickListener(onclick);
		imgView.setOnClickListener(onclick);
		imgView.setOnLongClickListener(onLongClick);

		getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
		getDialog().getWindow().setBackgroundDrawable(
				new ColorDrawable(Color.TRANSPARENT));
		if (cb != null)
			cb.getImageView(imgView);
		doMain();
		return view;
	}

	OnClickListener onclick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if (v == view || v == imgView) {
				dismiss();
			}
		}
	};

	OnLongClickListener onLongClick = new OnLongClickListener() {

		@Override
		public boolean onLongClick(View v) {
			// TODO Auto-generated method stub
			if (v == imgView) {
				if (savePath == null || bm == null) {
					return false;
				}
				if (savePicDialog == null) {
					savePicDialog = new SavePicDialog(context, bm, savePath);
				}
				try {
					savePicDialog.getPic(v);
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
				return true;
			}
			return true;
		}
	};


	private void doMain() {
		if (bm != null) {
			imgView.setImageBitmap(bm);
		} else {
			if (codeUrl != null)
				setImageUrl(codeUrl);
		}
	}


	private void setImageUrl(String url) {
		if (url == null) {
			Toast.makeText(context,context.getString(R.string.error_photo_path), Toast.LENGTH_SHORT).show();
			return;
		}
		new DownImageHelper().loadImage(imgView, url, true,
				new DrawableCallback() {

					@Override
					public void onPreExec() {
						// TODO Auto-generated method stub
						super.onPreExec();
						if (loadString != null) {
							txtLoad.setVisibility(View.VISIBLE);
							txtLoad.setText(loadString);
						} else {
							txtLoad.setVisibility(View.GONE);
						}
					}

					@Override
					public void onPostExec() {
						// TODO Auto-generated method stub
						super.onPostExec();
						txtLoad.setVisibility(View.GONE);
					}

					@Override
					public void onSuccess(Drawable drawable) {
						// TODO Auto-generated method stub
						try {
							bm = ((BitmapDrawable) drawable).getBitmap();
						} catch (Exception e) {
							Toast.makeText(context, context.getString(R.string.error_get_data),
									Toast.LENGTH_SHORT).show();
						}
					}

					@Override
					public void onFailure(String errMsg) {
						// TODO Auto-generated method stub
						Toast.makeText(context,context.getString(R.string.error_get_data), Toast.LENGTH_SHORT)
								.show();
					}
				});

	}

	@Override
	public void show(FragmentManager manager, String tag) {
		// TODO Auto-generated method stub
		super.show(manager, tag);
		if (cb != null)
			cb.showResult();
	}

	@Override
	public void dismiss() {
		// TODO Auto-generated method stub
		super.dismiss();
		clear();
		if (cb != null)
			cb.dismissResult();
	}

	private void clear() {
		bm = null;
		savePath = null;
		codeUrl = null;
		loadString = null;
	}

}
