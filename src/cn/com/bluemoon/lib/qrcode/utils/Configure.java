package cn.com.bluemoon.lib.qrcode.utils;

/**
 * 二维码工具类的参数设置
 * @author chenchongsen
 *
 */

public class Configure {

	/*********************color************************/
	/**
	 * 描述文字颜色
	 */
	public final static int CONTENT_COLOR = 0xFF333333;
	/**
	 * 扫描框外部半透明颜色
	 */
	public final static int MASK_COLOR = 0x60000000;
	/**
	 * 返回结果半透明颜色
	 */
	public final static int RESULT_COLOR =0xff000000;
	/**
	 * 扫描框边框颜色
	 */
	public final static int FRAME_COLOR =0x9955C4EC;
	/**
	 * 四个边角的颜色
	 */
	public final static int CORNER_COLOR =0xEE00EE00;
	/**
	 * 激光线颜色
	 */
	public final static int LASER_COLOR =0xFF00EE00;
	/**
	 * 激光线颜色半透明
	 */
	public final static int LASER_COLOR_LIGHT =0x9900DD00;
	/**
	 * 扫描框内闪点的颜色
	 */
	public final static int RESULT_POINT_COLOR =0xC0FFFF00;

	/*********************size************************/
	/**
	 * 描述文字大小
	 */
	public final static int CONTENT_SIZE = 16;
	/**
	 * 扫描框中的中间线的宽度
	 */
	public final static int LASER_HEIGHT = 4;
	/**
	 * 中间那条线每次刷新移动的距离
	 */
	public final static int LASER_SPACE = 2;
	/**
	 * 扫描框中的中间线的与扫描框左右的间隙
	 */
	public final static int LASER_PADDING = 3;
	/**
	 * 四个边角对应的长度
	 */
	public final static int CORNER_HEIGHT = 20;
	/**
	 * 四个边角对应的宽度
	 */
	public final static int CORNER_WIDTH = 3;
	/**
	 * 字体距离扫描框上面的距离
	 */
	public final static int CONTENT_PADDING_BOTTOM = 20;

	/*********************time****************************/
	/**
	 * 激光线刷新时间
	 */
	public final static long ANIMATION_DELAY = 15L;
	/**
	 * 扫描时间控制，超时则自动退出界面
	 */
	public static final int INACTIVITY_DELAY_SECONDS = 5 * 60;

	/***********************Alpha**************************/
	public static final int OPAQUE = 0xFF;


}
