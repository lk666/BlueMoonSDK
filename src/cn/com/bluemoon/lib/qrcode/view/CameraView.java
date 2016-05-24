package cn.com.bluemoon.lib.qrcode.view;

import android.content.Context;
import android.view.Gravity;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.com.bluemoon.lib.qrcode.R;
import cn.com.bluemoon.lib.qrcode.callback.IScanMenuListener;
import cn.com.bluemoon.lib.qrcode.utils.Configure;
import cn.com.bluemoon.lib.utils.LibPublicUtil;

public class CameraView extends FrameLayout{
	
	private Context mContext;
	private FrameLayout mainlayout;
	public ViewfinderView finderView;
	public SurfaceView surfaceView;
	private RelativeLayout relative;
	private RelativeLayout relativeTitle;
	private LinearLayout linearBotton;
	private LinearLayout linTicket;
	private TextView tvCount;
	private TextView tvName;
	private TextView tvTitle;
	private Button btnSign;
	private ImageView btnCancel;
	private ImageButton btnPick;
	private ImageButton btnLight;
	private IScanMenuListener listener;
	private float density;

	public CameraView(Context context,IScanMenuListener listener) {
		super(context);
		this.mContext = context;
		this.listener = listener;
		initialize();
	}

	private void initialize() {
		density = mContext.getResources().getDisplayMetrics().density;
		LayoutParams mParams = new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
		mainlayout = new FrameLayout(mContext);
		addView(mainlayout,mParams);
		

		mParams = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
		mParams.gravity = Gravity.CENTER;
		surfaceView = new SurfaceView(mContext);
		surfaceView.setId(4);
		mainlayout.addView(surfaceView,mParams);
		

		mParams = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
		finderView = new ViewfinderView(mContext);
		finderView.setId(5);
		mainlayout.addView(finderView,mParams);
		
		mParams = new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
		relative = new RelativeLayout(mContext);
		relative.setId(1);
		relative.setGravity(Gravity.CENTER_HORIZONTAL);
		mainlayout.addView(relative,mParams);
		

		RelativeLayout.LayoutParams rParams = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT,(int) (45*density));
		rParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
		relativeTitle = new RelativeLayout(mContext);
		relativeTitle.setId(2);
		relativeTitle.setBackgroundColor(Configure.TITLE_BG_COLOR);
		relative.addView(relativeTitle,rParams);
		
		rParams = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.MATCH_PARENT);
		rParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
		btnCancel = new ImageView(mContext);
		btnCancel.setImageResource(R.drawable.icon_back);
		btnCancel.setPadding((int)(10*density), (int)(10*density), (int)(10*density), (int)(10*density));
		btnCancel.setOnClickListener(onclick);
		relativeTitle.addView(btnCancel,rParams);
		
		rParams = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
		rParams.addRule(RelativeLayout.CENTER_IN_PARENT);
		tvTitle = new TextView(mContext);
		tvTitle.setGravity(Gravity.CENTER);
		if(Configure.TITLE_TXT==null||"".equals(Configure.TITLE_TXT)){
			Configure.TITLE_TXT = mContext.getString(R.string.qrcode_title_txt);
		}
		tvTitle.setText(Configure.TITLE_TXT);
//		tv_title.setPadding(0, 10, 0, 10);
//		tvTitle.setTypeface(Typeface.create("System", Typeface.BOLD));
		tvTitle.setTextColor(Configure.TITLE_TXT_COLOR);
		tvTitle.setTextSize(Configure.TITLE_TXT_SIZE);
		relativeTitle.addView(tvTitle,rParams);
		
		
		

		rParams = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
		rParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		linearBotton = new LinearLayout(mContext);
		linearBotton.setId(3);
		linearBotton.setVisibility(Configure.MENU_VISIBILITY);
		linearBotton.setBackgroundColor(Configure.BOTTON_BG_COLOR);
		linearBotton.setOrientation(LinearLayout.HORIZONTAL);
		relative.addView(linearBotton,rParams);
		
		LinearLayout.LayoutParams lParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
		lParams.weight = 1;
		lParams.gravity = Gravity.CENTER;
		btnPick = new ImageButton(mContext);
		btnPick.setVisibility(Configure.MENU_PICK_VISIBILITY);
		btnPick.setBackgroundColor(Configure.BOTTON_BG_COLOR);
		btnPick.setImageResource(R.drawable.pick_btn_bg);
		btnPick.setOnClickListener(onclick);
		linearBotton.addView(btnPick,lParams);
		
		lParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
		lParams.weight = 1;
		lParams.gravity = Gravity.CENTER;
		btnLight = new ImageButton(mContext);
		btnLight.setVisibility(Configure.MENU_LIGHT_VISIBILITY);
		btnLight.setBackgroundColor(Configure.BOTTON_BG_COLOR);
		btnLight.setImageResource(R.drawable.scan_code_light);
		btnLight.setOnClickListener(onclick);
		linearBotton.addView(btnLight,lParams);
		

		rParams = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,(int)(30*density));
		rParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
		rParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		rParams.bottomMargin = (int)(80*density);
		btnSign = new Button(mContext);
		btnSign.setGravity(Gravity.CENTER);
		btnSign.setVisibility(Configure.BUTTON_VISIBILITY);
		btnSign.setBackgroundResource(Configure.BTN_DRAWABLE_BG);
		btnSign.setText(Configure.BTN_CLICK_TXT);
		btnSign.setPadding((int)(18*density), 0, (int)(18*density), 0);
		btnSign.setCompoundDrawablePadding((int)(15*density));
		btnSign.setCompoundDrawablesWithIntrinsicBounds(R.drawable.scan_number, 0, 0, 0);
		btnSign.setId(7);
		btnSign.setTextColor(Configure.BTN_TXT_CLICK_COLOR);
		btnSign.setTextSize(Configure.BTN_CLICK_TXT_SIZE);
		btnSign.setOnClickListener(onclick);
		relative.addView(btnSign,rParams);		
		
		rParams = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
		rParams.addRule(RelativeLayout.BELOW, 2);
		linTicket = new LinearLayout(mContext);
		linTicket.setId(8);
		linTicket.setPadding((int)(10*density), (int)(10*density),0, (int)(10*density));
		linTicket.setVisibility(Configure.TICKET_TITLE_VISIBILITY);
		linTicket.setBackgroundColor(Configure.TICKET_COUNT_BG);
		linTicket.setOrientation(LinearLayout.VERTICAL);
		relative.addView(linTicket,rParams);
		
		lParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
		tvName = new TextView(mContext);
		tvName.setText(Configure.TICKET_COUNT_NAME);
		tvName.setTextSize(17);
		tvName.setTextColor(Configure.TICKET_VENUE_TXT_COLOR);
		linTicket.addView(tvName,lParams);
		
		lParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.MATCH_PARENT);
		tvCount = new TextView(mContext);
		tvCount.setPadding(0, (int)(10*density),0, 0);
		tvCount.setText(Configure.TICKET_COUNT_TIME);
		tvCount.setTextSize(15);
		tvCount.setTextColor(Configure.TICKET_TIMES_TXT_COLOR);
		linTicket.addView(tvCount,lParams);
		
	}
	
	OnClickListener onclick = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			
			if (v == btnCancel) {
				if (!LibPublicUtil.isFastDoubleClick(1000))
					listener.closeScan();
			} else if (v == btnLight) {
				listener.openLight(btnLight);
			} else if (v == btnPick) {
				if (!LibPublicUtil.isFastDoubleClick(1000)){
					listener.openPick(btnPick);
					btnLight.setImageResource(R.drawable.scan_code_light);
				}
					
			} else if(v == btnSign){
				if (!LibPublicUtil.isFastDoubleClick(1000))
					listener.onClick(btnSign);
			}
			
		}
	};

}
