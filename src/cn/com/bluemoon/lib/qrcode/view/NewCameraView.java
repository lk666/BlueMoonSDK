package cn.com.bluemoon.lib.qrcode.view;

import android.content.Context;
import android.view.Gravity;
import android.view.SurfaceView;
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

/**
 * Created by allenli on 2016/7/6.
 */
public class NewCameraView extends CameraView {

    ImageView imgQr;
    ImageView imgBar;
    TextView txtQr;
    TextView txtBar;

    public NewCameraView(Context context, IScanMenuListener listener) {
        super(context, listener);
    }


    @Override
    protected void initialize() {
        density = mContext.getResources().getDisplayMetrics().density;
        LayoutParams mParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        mainlayout = new FrameLayout(mContext);
        addView(mainlayout, mParams);


        mParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        mParams.gravity = Gravity.TOP;
        surfaceView = new SurfaceView(mContext);
        surfaceView.setId(4);
        mainlayout.addView(surfaceView, mParams);


        mParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        mParams.gravity = Gravity.TOP;
        finderView = new ViewfinderView(mContext);
        finderView.setId(5);
        mainlayout.addView(finderView, mParams);

        //标题抬头RelativeLayout
        mParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        relative = new RelativeLayout(mContext);
        relative.setId(1);
        relative.setGravity(Gravity.CENTER_HORIZONTAL);
        mainlayout.addView(relative, mParams);

        RelativeLayout.LayoutParams rParams = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, (int) (45 * density));
        rParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        relativeTitle = new RelativeLayout(mContext);
        relativeTitle.setId(2);
        relativeTitle.setBackgroundColor(Configure.TITLE_BG_COLOR);
        relative.addView(relativeTitle, rParams);

        rParams = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
        rParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        btnCancel = new ImageView(mContext);
        btnCancel.setImageResource(R.drawable.icon_back);
        btnCancel.setPadding((int) (10 * density), (int) (10 * density), (int) (10 * density), (int) (10 * density));
        btnCancel.setOnClickListener(onclick);
        relativeTitle.addView(btnCancel, rParams);

        rParams = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        rParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        tvTitle = new TextView(mContext);
        tvTitle.setGravity(Gravity.CENTER);
        if (Configure.TITLE_TXT == null || "".equals(Configure.TITLE_TXT)) {
            Configure.TITLE_TXT = mContext.getString(R.string.qrcode_title_txt);
        }
        tvTitle.setText(Configure.TITLE_TXT);
        tvTitle.setTextColor(Configure.TITLE_TXT_COLOR);
        tvTitle.setTextSize(Configure.TITLE_TXT_SIZE);
        relativeTitle.addView(tvTitle, rParams);


        rParams = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        rParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        linearBotton = new LinearLayout(mContext);
        linearBotton.setId(3);
        linearBotton.setVisibility(Configure.MENU_VISIBILITY);
        linearBotton.setBackgroundColor(Configure.BOTTON_BG_COLOR);
        linearBotton.setOrientation(LinearLayout.HORIZONTAL);
        relative.addView(linearBotton, rParams);

        LinearLayout.LayoutParams lParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        lParams.weight = 1;
        lParams.gravity = Gravity.CENTER;
        btnPick = new ImageButton(mContext);
        btnPick.setVisibility(Configure.MENU_PICK_VISIBILITY);
        btnPick.setBackgroundColor(Configure.BOTTON_BG_COLOR);
        btnPick.setImageResource(R.drawable.pick_btn_bg);
        btnPick.setOnClickListener(onclick);
        linearBotton.addView(btnPick, lParams);

        lParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        lParams.weight = 1;
        lParams.gravity = Gravity.CENTER;
        btnLight = new ImageButton(mContext);
        btnLight.setVisibility(Configure.MENU_LIGHT_VISIBILITY);
        btnLight.setBackgroundColor(Configure.BOTTON_BG_COLOR);
        btnLight.setImageResource(R.drawable.scan_code_light);
        btnLight.setOnClickListener(onclick);
        linearBotton.addView(btnLight, lParams);

        rParams = new RelativeLayout.LayoutParams((int) (44 * density), (int) (44 * density));
        rParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        rParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        rParams.bottomMargin = (int) (110 * density);
        rParams.leftMargin = (int) (90 * density);
        imgQr = new ImageView(mContext);
        imgQr.setImageResource(R.drawable.icon_qr);

        relative.addView(imgQr, rParams);

        rParams = new RelativeLayout.LayoutParams((int) (44 * density), LayoutParams.WRAP_CONTENT);
        rParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        rParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        rParams.bottomMargin = (int) (80 * density);
        rParams.leftMargin = (int) (90 * density);
        txtQr = new TextView(mContext);
        txtQr.setGravity(Gravity.CENTER);
        txtQr.setText(getContext().getResources().getString(R.string.text_qr));
        txtQr.setTextColor(getContext().getResources().getColor(R.color.scan_text_blue));
        txtQr.setTextSize(13);
        relative.addView(txtQr, rParams);


        rParams = new RelativeLayout.LayoutParams((int) (44 * density), (int) (44 * density));
        rParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        rParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        rParams.addRule(RelativeLayout.RIGHT_OF, imgQr.getId());
        rParams.bottomMargin = (int) (110 * density);
        rParams.rightMargin = (int) (90 * density);
        imgBar = new ImageView(mContext);
        imgBar.setImageResource(R.drawable.icon_bar_code);

        relative.addView(imgBar, rParams);


        rParams = new RelativeLayout.LayoutParams((int) (44 * density), LayoutParams.WRAP_CONTENT);
        rParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        rParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        rParams.bottomMargin = (int) (80 * density);
        rParams.rightMargin = (int) (90 * density);
        txtBar = new TextView(mContext);
        txtBar.setGravity(Gravity.CENTER);
        txtBar.setText(getContext().getResources().getString(R.string.text_bar_code));
        txtBar.setTextColor(getContext().getResources().getColor(R.color.scan_text_blue));
        txtBar.setTextSize(13);
        relative.addView(txtBar, rParams);


        rParams = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, (int) (30 * density));
        rParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        rParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        rParams.bottomMargin = (int) (30 * density);
        btnSign = new Button(mContext);
        btnSign.setGravity(Gravity.CENTER);
        btnSign.setVisibility(Configure.BUTTON_VISIBILITY);
        btnSign.setBackgroundResource(Configure.BTN_DRAWABLE_RED_BG);
        btnSign.setText(Configure.BTN_CLICK_TXT);
        btnSign.setPadding((int) (18 * density), 0, (int) (18 * density), 0);
        btnSign.setCompoundDrawablePadding((int) (15 * density));
        btnSign.setCompoundDrawablesWithIntrinsicBounds(R.drawable.scan_number, 0, 0, 0);
        btnSign.setId(7);
        btnSign.setTextColor(Configure.BTN_TXT_CLICK_COLOR);
        btnSign.setTextSize(Configure.BTN_CLICK_TXT_SIZE);
        btnSign.setOnClickListener(onclick);
        relative.addView(btnSign, rParams);

        rParams = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        rParams.addRule(RelativeLayout.BELOW, 2);
        linTicket = new LinearLayout(mContext);
        linTicket.setId(8);
        linTicket.setPadding((int) (10 * density), (int) (10 * density), 0, (int) (10 * density));
        linTicket.setVisibility(Configure.TICKET_TITLE_VISIBILITY);
        linTicket.setBackgroundColor(Configure.TICKET_COUNT_BG);
        linTicket.setOrientation(LinearLayout.VERTICAL);
        relative.addView(linTicket, rParams);

        lParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        tvName = new TextView(mContext);
        tvName.setText(Configure.TICKET_COUNT_NAME);
        tvName.setTextSize(17);
        tvName.setTextColor(Configure.TICKET_VENUE_TXT_COLOR);
        linTicket.addView(tvName, lParams);

        lParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
        tvCount = new TextView(mContext);
        tvCount.setPadding(0, (int) (10 * density), 0, 0);
        tvCount.setText(Configure.TICKET_COUNT_TIME);
        tvCount.setTextSize(15);
        tvCount.setTextColor(Configure.TICKET_TIMES_TXT_COLOR);
        linTicket.addView(tvCount, lParams);

    }
}
