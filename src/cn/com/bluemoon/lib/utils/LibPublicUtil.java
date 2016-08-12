package cn.com.bluemoon.lib.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.hardware.Camera;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Environment;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.conn.util.InetAddressUtils;

import java.io.File;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;

import cn.com.bluemoon.lib.qrcode.R;
import cn.com.bluemoon.lib.view.CommonAlertDialog;

@SuppressLint("NewApi")
public class LibPublicUtil {

    private static final String TAG = "LibPublicUtil";
    public static float sDensity;
    public static float sDensityDpi;
    public static int sScreenW;
    public static int sScreenH;
    public static ActivityManager sActivityManager;

    /**********************
     * system info
     **********************/


    public static void init(Activity activiy) {
        Display dis = activiy.getWindowManager().getDefaultDisplay();
        DisplayMetrics dm = new DisplayMetrics();
        dis.getMetrics(dm);
        int h = dis.getHeight();
        int w = dis.getWidth();
        sScreenW = w < h ? w : h;
        sScreenH = w < h ? h : w;
        sDensity = dm.density;
        sDensityDpi = dm.densityDpi;
        sActivityManager = (ActivityManager) activiy
                .getSystemService(Activity.ACTIVITY_SERVICE);
    }

    public static int getScreenW() {
        return sScreenW;
    }

    public static int getScreenH() {
        return sScreenH;
    }


    public static boolean hasSDCard() {
        if (Environment.getExternalStorageState().equals(
                android.os.Environment.MEDIA_MOUNTED)) {
            return true;
        }
        return false;
    }


    public static int getMaxMemory() {
        return (int) (Runtime.getRuntime().maxMemory() / 1024.0f / 1024.0f);
    }


    public static String getAppVersionNoSuffix(Context context) {
        PackageManager pm = context.getPackageManager();
        if (pm != null) {
            PackageInfo pi;
            try {
                pi = pm.getPackageInfo(context.getPackageName(), 0);
                if (pi != null) {
                    String ver = pi.versionName;
                    return ver;
                }
            } catch (NameNotFoundException e) {
            }
        }
        return null;
    }


    public static String getSdcardPath() {
        File sdcard = Environment.getExternalStorageDirectory();
        if (sdcard == null) {
            return "";
        }
        return sdcard.getPath();
    }

    public static double getAvailableMemory(Activity activiy) {
        ActivityManager sActivityManager = (ActivityManager) activiy
                .getSystemService(Activity.ACTIVITY_SERVICE);
        if (sActivityManager != null) {
            MemoryInfo outInfo = new MemoryInfo();
            sActivityManager.getMemoryInfo(outInfo);
            return outInfo.availMem / 1024.0f / 1024.0f;
        }
        return 0;
    }


    public String getIPString(int ipAddress) {
        String ipString = "";
        if (ipAddress != 0) {
            ipString = ((ipAddress & 0xff) + "." + (ipAddress >> 8 & 0xff)
                    + "." + (ipAddress >> 16 & 0xff) + "." + (ipAddress >> 24 & 0xff));
        }
        return ipString;
    }

    public static void unInstall(String packageName, Context context) {

        Uri packageURI = Uri.parse("package:" + packageName);

        Intent uninstallIntent = new Intent(Intent.ACTION_DELETE, packageURI);

        context.startActivity(uninstallIntent);

    }

    public static boolean hasIntenet(Context context) {
        if (context == null)
            return false;
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = connectivityManager.getActiveNetworkInfo();
        if (info != null) {
            if (info.isAvailable() == true)
                return true;
            /*
			 * if(info.getType() == ConnectivityManager.TYPE_WIFI) return true;
			 */
        }
        return false;
    }


    public static boolean isWifi(Context context) {
        if (context == null)
            return false;
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = connectivityManager.getActiveNetworkInfo();
        if (info != null) {
            if (info.getType() == ConnectivityManager.TYPE_WIFI)
                return true;
        }
        return false;
    }


    public static int getWifiApState(Context context) {
        WifiManager wifiManager = (WifiManager) context
                .getSystemService(Context.WIFI_SERVICE);
        try {
            Method method = wifiManager.getClass().getMethod("getWifiApState");
            return (Integer) method.invoke(wifiManager);
        } catch (Exception e) {
            Log.e("anson", "Cannot get WiFi AP state", e);
            return 0;
        }
    }


    public static String getLocalIpAddress() {
        try {
            Enumeration<NetworkInterface> en = NetworkInterface
                    .getNetworkInterfaces();
            while (en.hasMoreElements()) {
                NetworkInterface nif = en.nextElement();
                Enumeration<InetAddress> enumIpAddr = nif.getInetAddresses();
                while (enumIpAddr.hasMoreElements()) {
                    InetAddress mInetAddress = enumIpAddr.nextElement();
                    if (!mInetAddress.isLoopbackAddress()
                            && InetAddressUtils.isIPv4Address(mInetAddress
                            .getHostAddress())) {
                        return mInetAddress.getHostAddress().toString();
                    }
                }
            }
        } catch (SocketException ex) {
            Log.e("anson", ex.getMessage());
        }

        return null;
    }

    public static String getPhotoPath(String photoPath, String id) {
        String imageName = System.currentTimeMillis() + ".jpg";
        if (!StringUtils.isEmpty(id)) {
            imageName = id + "_" + imageName;
        }
        File file = new File(photoPath);
        file.mkdirs();
        return photoPath + File.separator + imageName;
    }


    public static String getPhoneNumber(Context context) {
        String phoneNumber = null;
        TelephonyManager phoneMgr = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        phoneNumber = phoneMgr.getLine1Number();
        if (phoneNumber == null) {
            phoneNumber = "0";
        }
        if (phoneNumber.startsWith("+86")) {
            phoneNumber = phoneNumber.substring(3);
        }

        return phoneNumber;
    }


    public static String getMachineMode() {
        Log.e("mode", android.os.Build.MODEL);
        return android.os.Build.MODEL;
    }


    public static String getMachineImei(Context context) {
        TelephonyManager tm = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        String IMIE = tm.getDeviceId();
        Log.e("imie", IMIE);
        return IMIE;
    }

    public static void callPhone(Context context, String phone) {
        try {
            Intent intent = new Intent(Intent.ACTION_CALL);
            intent.setData(Uri.parse("tel:" + phone));
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public static boolean checkApkExist(Context context, String packageName) {
        if (packageName == null || "".equals(packageName))
            return false;
        try {
            context.getPackageManager().getApplicationInfo(packageName,
                    PackageManager.GET_UNINSTALLED_PACKAGES);
            return true;
        } catch (NameNotFoundException e) {
            return false;
        }
    }

    public static boolean checkApkExist(Context context, Intent intent) {
        List<ResolveInfo> list = context.getPackageManager()
                .queryIntentActivities(intent, 0);
        if (list.size() > 0) {
            return true;
        }
        return false;
    }

    public static boolean isExistWeiXin(Context context) {
        return checkApkExist(context, "com.tencent.mm");
    }

    public static void showWeixinApp(Context context, String toast) {
        try {
            Intent intent = new Intent();
            ComponentName cmp = new ComponentName("com.tencent.mm",
                    "com.tencent.mm.ui.LauncherUI");
            intent.setAction(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_LAUNCHER);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setComponent(cmp);
            if (checkApkExist(context, intent)) {
                context.startActivity(intent);
            } else {
                showToast(context, toast);
            }
        } catch (Exception e) {
            // TODO: handle exception
            showToast(context, context.getString(R.string.not_found_weixin));
        }
    }


    public static void openUrl(Context context, String url) {
        Uri uri = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        context.startActivity(intent);
    }

    /**********************
     * string and byte
     **********************/

    public static float getNPoint(float num, int n) {
        int s = 1;
        for (int i = 0; i < n; i++) {
            s = s * 10;
        }
        float m = (float) (Math.round(num * s)) / s;
        return m;
    }


    public static String byteToSize(float size, int n) {
        String[] list = {"KB", "M", "G", "T", "PB", "EB", "ZB", "YB", "BB",
                "DB"};
        String sizeString = size + "B";
        for (int i = 0; i < 10; i++) {
            if (size > 1024) {
                size = size / 1024;
            } else {
                if (i > 0) {
                    size = getNPoint(size, n);
                    sizeString = size + list[i - 1];
                }
                break;
            }
        }

        return sizeString;
    }


    public static String byteToSize(float size) {

        return byteToSize(size, 2);
    }


    public static String format(int x) {
        String s = "" + x;
        if (s.length() == 1)
            s = "0" + s;
        return s;
    }

	public static String makePhotoName(Context context) {
		Date date = new Date();
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmssSSSS",
				Locale.CHINA);
		String strDate = df.format(date);
		String strRand = String.valueOf((int) (Math.random() * 200));
		String strRand2 = String.valueOf((int) (Math.random() * 20));
		if (strRand.length() < 4) {
			strRand = "0000".substring(strRand.length()) + strRand;
		}
		if (strRand2.length() < 2) {
			strRand2 = "00".substring(strRand2.length()) + strRand2;
		}
		if (context == null) {
			return strDate + strRand + strRand2 + ".jpg";
		}
		String str = "BA" + strDate + strRand + strRand2 + "-00-000000.jpg";
		return str;
	}

	public static String makeSoundName(Context context) {
		Date date = new Date();
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss",
				Locale.CHINA);
		String strDate = df.format(date);
		String strRand = String.valueOf((int) (Math.random() * 100));
		if (strRand.length() < 4) {
			strRand = "0000".substring(strRand.length()) + strRand;
		}
		if (context == null) {
			return strDate + strRand + ".mp3";
		}
		String str = "MI" + strDate + strRand + "-00-000000.mp3";
		return str;
	}

	public static String makeLocalToken() {
		Date date = new Date();
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss",
				Locale.CHINA);
		String strDate = df.format(date);
		String strRand = String.valueOf((int) (Math.random() * 20));
		if (strRand.length() < 3) {
			strRand = "000".substring(strRand.length()) + strRand;
		}
		String str = strDate + strRand;
		return str;
	}


    public static boolean isPhone(String phone) {
        String telRegex = "[1][3456789]\\d{9}";

        if (phone.matches(telRegex)) {
            return true;
        }
        return false;
    }


    public static String getPriceFrom(double price) {
        DecimalFormat df = new DecimalFormat("0.00");

        return df.format(price);
    }


	public static String getPriceFrom(String price) {
		double p = 0.00;
		try {
			p = Double.parseDouble(price);
		} catch (Exception e) {

		}
		return getPriceFrom(p);
	}
	

    public static String getPhotoName(String picFrom) {
        return System.currentTimeMillis() + picFrom;
    }


	public static String getCameraPath() {
		if (!hasSDCard())
			return null;
		String fileName;
		String pathUrl = LibConstants.PATH_CAMERA;
		File file = new File(pathUrl);
		if(!file.exists()){
			file.mkdirs();
		}
		fileName = pathUrl + File.separator + getPhotoName(".jpg");
		return fileName;
	}

    /**********************
     * time and click count
     **********************/

    private static long lastClickTime;


    public static boolean isFastDoubleClick(int ms) {
        long time = System.currentTimeMillis();
        long timeD = time - lastClickTime;
        System.out.println(timeD + "");
        if (0 < timeD && timeD < ms) {
            return true;
        }
        lastClickTime = time;
        return false;
    }


    public static boolean isFastDoubleClick() {
        long time = System.currentTimeMillis();
        long timeD = time - lastClickTime;
        if (0 < timeD && timeD < 500) {
            return true;
        }
        lastClickTime = time;
        return false;
    }

    private static int fastClickCount = 1;


    public static int getFastClickCount(int ms) {
        long time = System.currentTimeMillis();
        long timeD = time - lastClickTime;
        System.out.println(timeD + "");
        lastClickTime = time;
        if (0 < timeD && timeD < ms) {
            fastClickCount++;
            return fastClickCount;
        }
        fastClickCount = 1;
        return fastClickCount;
    }


    public static boolean isClickTrue(int ms, int maxTimes) {
        int times = getFastClickCount(ms);
        if (times >= maxTimes) {
            return true;
        }
        return false;
    }

    /**********************
     * message dialog
     **********************/


    public static void showMessage(Context context, String title, String msg) {
        CommonAlertDialog.Builder dialog = new CommonAlertDialog.Builder(context);
        dialog.setTitle(title);
        dialog.setMessage(msg);
        dialog.setPositiveButton(R.string.btn_ok, null);
        dialog.show();
    }


    public static void showMessage(Context context, String msg) {
        showMessage(context, context.getString(R.string.dialog_title), msg);
    }


    public static void showMessageNoTitle(Context context, String msg) {
        new CommonAlertDialog.Builder(context).setMessage(msg)
                .setNegativeButton(R.string.btn_ok, null).show();
    }


    public static void showMessageNoInternet(final Context context) {
        new CommonAlertDialog.Builder(context)
                .setTitle(context.getString(R.string.dialog_title))
                .setMessage(context.getString(R.string.request_no_internet))
                .setPositiveButton(R.string.btn_internet,
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                // TODO Auto-generated method stub
                                try {
                                    Intent intent = new Intent(
                                            Settings.ACTION_AIRPLANE_MODE_SETTINGS);
                                    context.startActivity(intent);
                                } catch (Exception e) {
                                    showToast(context, context.getString(R.string
											.content_can_not_to_network_setting));
                                }

                            }
                        }).setNegativeButton(R.string.btn_cancel, null).show();
    }


    public static void showToast(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }


    public static void showCustomToast(Context context, String message,
                                       Drawable drawable, int gravity) {
        try {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            View layout = inflater.inflate(R.layout.toast_custome, null);
            if (drawable != null) {
                ImageView imageView = (ImageView) layout
                        .findViewById(R.id.ivForToast);
                imageView.setImageDrawable(drawable);
                imageView.setVisibility(View.VISIBLE);
            }
            TextView textView = (TextView) layout.findViewById(R.id.tvForToast);
            textView.setText(message);
            if (gravity != -1) {
                textView.setGravity(gravity);
            }
            Toast toast = new Toast(context);
            toast.setDuration(Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
            toast.setView(layout);
            toast.show();
        } catch (Exception e) {
            // TODO: handle exception
            LibLogUtils.e(TAG, "please sure context is Activity");
        }
    }


    public static void showCustomToast(Context context, String message,
                                       int gravity) {
        showCustomToast(context, message, null, gravity);
    }

    public static void showCustomToast(Context context, String message,
                                       boolean isRight, int gravity) {
        if (isRight) {
            showCustomToast(context, message, context.getResources()
                    .getDrawable(R.drawable.pass), gravity);
            return;
        }
        showCustomToast(context, message,
                context.getResources().getDrawable(R.drawable.error2), gravity);
    }

    public static void showCustomToast(Context context, String message) {
        showCustomToast(context, message, null, -1);
    }


    public static void showCustomToast(Context context, String message,
                                       boolean isRight) {
        if (isRight) {
            showCustomToast(context, message, context.getResources()
                    .getDrawable(R.drawable.pass), -1);
            return;
        }
        showCustomToast(context, message,
                context.getResources().getDrawable(R.drawable.error2), -1);
    }


    public static void showToastNoInternet(Context context) {
        showToast(context, context.getString(R.string.request_no_internet));
    }


    public static void showToastServerBusy(Context context) {
        showToast(context, context.getString(R.string.request_server_busy));
    }


    public static void showToastServerOvertime(Context context) {
        showToast(context, context.getString(R.string.request_server_overtime));
    }


    public static void showToastErrorData(Context context) {
        showToast(context, context.getString(R.string.get_data_busy));
    }

    public static boolean hasPermission(Context context, String perName) {
        return PackageManager.PERMISSION_GRANTED ==
                context.checkCallingOrSelfPermission(perName);

    }

    public static boolean checkCameraAuthority() {
        Camera camera = null;
        try {
            camera = Camera.open();
            if (camera != null) {
                camera.release();
                camera = null;
            }
            return true;
        } catch (Exception e) {
            if (camera != null) {
                camera.release();
            }
            return false;
        }
    }

    public static void showMessageCameraError(Context context) {
        showMessage(context, context.getString(R.string.error_camera_title),
                context.getString(R.string.error_camera_content));
    }

}
