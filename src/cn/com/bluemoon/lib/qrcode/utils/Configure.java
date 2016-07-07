package cn.com.bluemoon.lib.qrcode.utils;

import android.view.View;
import cn.com.bluemoon.lib.qrcode.R;

/**
 * 二维码工具类的参数设置
 * @author chenchongsen
 *
 */

public class Configure {

	/*********************color************************/
	/**
	 * 顶部横条的背景色
	 */
	public static int TITLE_BG_COLOR = 0xaa000000;
	/**
	 * 按钮的背景色(R.drawable.scan_black_btn)
	 */
	public static int BTN_DRAWABLE_BG = R.drawable.btn_blue_shape;

	public static int BTN_DRAWABLE_RED_BG = R.drawable.btn_red_shape;

	/**
	 * 底部横条的背景色
	 */
	public static int BOTTON_BG_COLOR = 0xaa000000;
	/**
	 * 标题文字颜色
	 */
	public static int TITLE_TXT_COLOR = 0xbbffffff;
	/**
	 * 按钮文字初始的颜色
	 */
	public static int BTN_TXT_COLOR = 0xffffffff;
	/**
	 * 描述文字颜色
	 */
	public static int BTN_TXT_CONTENT_COLOR = 0xff333333;
	/**
	 * 按钮文字颜色
	 */
	public static int BTN_TXT_CLICK_COLOR = 0xbbffffff;
	/**
	 * 扫描框外部半透明颜色
	 */
	public static int MASK_COLOR = 0x60000000;
	/**
	 * 返回结果半透明颜色
	 */
	public static int RESULT_COLOR =0xb0000000;
	/**
	 * 扫描框边框颜色
	 */
	public static int FRAME_COLOR =0x9955c4ec;
	/**
	 * 激光线颜色
	 */
	public static int LASER_COLOR =0xff00ff00;
	/**
	 * 扫描框内闪点的颜色
	 */
	public static int RESULT_POINT_COLOR =0xc0ffff00;
	/**
	 * 门票场馆字体颜色
	 */
	public static int TICKET_VENUE_TXT_COLOR = 0xffffffff;
	/**
	 * 门票场次字体颜色
	 */
	public static int TICKET_TIMES_TXT_COLOR = 0xffd9d9d9;
	/**
	 * 门票场馆场次背景
	 */
	public static int TICKET_COUNT_BG = 0x99011930;

	/*********************txt************************/
	/**
	 * 标题文字
	 */
	public static String TITLE_TXT = "";
	/**
	 * 事件按钮文字
	 */
	public static String BTN_CLICK_TXT = "";
	/**
	 * 门票场次时间
	 */
	public static String TICKET_COUNT_TIME = "";
	/**
	 * 门票场馆名字
	 */
	public static String TICKET_COUNT_NAME = "";

	/*********************size************************/
	/**
	 * 标题文字大小
	 */
	public static int TITLE_TXT_SIZE = 18;
	/**
	 * 按钮文字大小
	 */
	public static int BTN_TXT_SIZE = 16;
	/**
	 * 描述文字大小
	 */
	public static int CONTENT_TXT_SIZE = 16;
	/**
	 * 按钮事件文字大小
	 */
	public static int BTN_CLICK_TXT_SIZE = 16;
	/**
	 * 扫描框中的中间线的宽度
	 */
	public static int LINE_HEIGHT = 4;
	/**
	 * 中间那条线每次刷新移动的距离
	 */
	public static int LINE_SIZE = 5;
	/**
	 * 扫描框中的中间线的与扫描框左右的间隙
	 */
	public static int MIDDLE_LINE_PADDING = 3;
	/**
	 * 四个绿色边角对应的长度
	 */
	public static int SCREEN_RATE = 20;
	/**
	 * 四个绿色边角对应的宽度
	 */
	public static int CORNER_WIDTH = 5;
	/**
	 * 字体距离扫描框上面的距离
	 */
	public static int TEXT_PADDING_BOTTOM = 20;

	/*********************time****************************/
	/**
	 * 激光线刷新时间
	 */
	public static long ANIMATION_DELAY = 15L;
	/**
	 * 激光扫描线的动态变化值
	 */
	public static int[] SCANNER_ALPHA = {192};


	/*********************menu visibility****************************/
	/**
	 * 设置扫描界面的底部菜单显示
	 * {@value 整条菜单     }
	 * @param int (View.VISIBLE,View.GONE)
	 */
	public static int MENU_VISIBILITY = View.VISIBLE;
	/**
	 * 设置扫描界面的底部菜单显示
	 * {@value 选取相册二维码解码菜单}
	 * @param int (View.VISIBLE,View.GONE)
	 */
	public static int MENU_PICK_VISIBILITY = View.VISIBLE;

	/**
	 * 设置扫描界面的底部菜单显示
	 * {@value 开启闪光灯菜单}
	 * @param int (View.VISIBLE,View.GONE)
	 */
	public static int MENU_LIGHT_VISIBILITY = View.VISIBLE;
	/**
	 * 设置描述文字显示
	 * @param int (View.VISIBLE,View.GONE)
	 */
	public static int CONTENT_VISIBILITY = View.VISIBLE;
	/**
	 * 设置事件按钮显示
	 * @param int (View.VISIBLE,View.GONE)
	 */
	public static int BUTTON_VISIBILITY = View.GONE;
	/**
	 * 设置顶部门票显示
	 * @param int (View.VISIBLE,View.GONE)
	 */
	public static int TICKET_TITLE_VISIBILITY = View.GONE;

	/*********************boolean**************************/
	/**
	 * 设置是否持续扫描
	 * @param int (View.VISIBLE,View.GONE)
	 */
	public static boolean IS_KEEP_OPEN = false;


}
