package cn.com.bluemoon.lib.utils;

import android.annotation.SuppressLint;

import org.apache.commons.lang3.StringUtils;

import java.text.DecimalFormat;
import java.util.Locale;
import java.util.regex.Pattern;

public class LibStringUtil {

    private static final Pattern emailer = Pattern.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");
    private static final Pattern phone = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");

    public static boolean isEmpty(CharSequence input) {
        if (input != null && !"".equals(input)) {
            for (int i = 0; i < input.length(); ++i) {
                char c = input.charAt(i);
                if (c != 32 && c != 9 && c != 13 && c != 10) {
                    return false;
                }
            }

            return true;
        } else {
            return true;
        }
    }

    public static boolean isEmpty(CharSequence... strs) {
        CharSequence[] var4 = strs;
        int var3 = strs.length;

        for (int var2 = 0; var2 < var3; ++var2) {
            CharSequence str = var4[var2];
            if (isEmpty(str)) {
                return true;
            }
        }

        return false;
    }

    public static boolean isEmail(CharSequence email) {
        return isEmpty(email)?false:emailer.matcher(email).matches();
    }

    public static boolean isPhone(CharSequence phoneNum) {
        return isEmpty(phoneNum)?false:phone.matcher(phoneNum).matches();
    }

    public static int toInt(String str, int defValue) {
        try {
            return Integer.parseInt(str);
        } catch (Exception var3) {
            return defValue;
        }
    }

    public static int toInt(Object obj) {
        return obj == null?0:toInt(obj.toString(), 0);
    }

    public static long toLong(String obj) {
        try {
            return Long.parseLong(obj);
        } catch (Exception var2) {
            return 0L;
        }
    }

    public static double toDouble(String obj) {
        try {
            return Double.parseDouble(obj);
        } catch (Exception var2) {
            return 0.0D;
        }
    }

    public static boolean toBool(String b) {
        try {
            return Boolean.parseBoolean(b);
        } catch (Exception var2) {
            return false;
        }
    }

    public static boolean isNumber(CharSequence str) {
        try {
            Integer.parseInt(str.toString());
            return true;
        } catch (Exception var2) {
            return false;
        }
    }

    public static final String byteArrayToHexString(byte[] data) {
        StringBuilder sb = new StringBuilder(data.length * 2);
        byte[] var5 = data;
        int var4 = data.length;

        for(int var3 = 0; var3 < var4; ++var3) {
            byte b = var5[var3];
            int v = b & 255;
            if(v < 16) {
                sb.append('0');
            }

            sb.append(Integer.toHexString(v));
        }

        return sb.toString().toUpperCase(Locale.getDefault());
    }

    public static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] d = new byte[len / 2];

        for(int i = 0; i < len; i += 2) {
            d[i / 2] = (byte)((Character.digit(s.charAt(i), 16) << 4) + Character.digit(s.charAt(i + 1), 16));
        }

        return d;
    }

    public static String formatPrice(String price) {
        DecimalFormat df = new DecimalFormat("0.00");
        return df.format(Double.valueOf(price));
    }

    public static String formatPrice(double price) {
        DecimalFormat df = new DecimalFormat("0.00");
        return df.format(price);
    }

    public static String formatArea(double area) {
        DecimalFormat df = new DecimalFormat("0.0");
        return df.format(area);
    }

    @SuppressLint("DefaultLocale")
    public static String formatPriceByFen(long price) {
        return String.format("%.2f", (double) price / 100);
    }

    @SuppressLint("DefaultLocale")
    public static String formatPriceByFen(int price) {
        return String.format("%.2f", (double) price / 100);
    }

    @SuppressLint("DefaultLocale")
    public static String formatBoxesNum(double boxes) {
        return String.format("%.1f", boxes);
    }

    private static boolean isChinese(char c) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_B) {
            return true;
        }
        return false;
    }


    public static boolean isChinese(String strName) {
        char[] ch = strName.toCharArray();
        for (int i = 0; i < ch.length; i++) {
            char c = ch[i];
            if (isChinese(c)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        return pattern.matcher(str).matches();
    }

    public static boolean isLetter(String str) {
        Pattern pattern = Pattern.compile("[a-zA-Z]*");
        return pattern.matcher(str).matches();
    }


    public static boolean containsEmoji(String source) {
        int len = source.length();
        for (int i = 0; i < len; i++) {
            char codePoint = source.charAt(i);
            if (!isEmojiCharacter(codePoint)) {
                return true;
            }
        }
        return false;
    }


    private static boolean isEmojiCharacter(char codePoint) {
        return (codePoint == 0x0) || (codePoint == 0x9) || (codePoint == 0xA)
                || (codePoint == 0xD)
                || ((codePoint >= 0x20) && (codePoint <= 0xD7FF))
                || ((codePoint >= 0xE000) && (codePoint <= 0xFFFD))
                || ((codePoint >= 0x10000) && (codePoint <= 0x10FFFF));
    }


    public static String getStringNotEmoji(String source) {
        String str = "";
        for (int i = 0; i < source.length(); i++) {
            char codePoint = source.charAt(i);

            if (isEmojiCharacter(codePoint)) {
                str += codePoint;
            }
        }
        return str;
    }


    public static String getStringNotEmoji2(String source) {
        String str = "";
        char[] chars = source.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            char codePoint = chars[i];

            if (isEmojiCharacter(codePoint)) {
                str += codePoint;
            } else {
                str += "?";
            }
        }
        return str;
    }

    public static byte[] hex16StringToByte(String hex) {
        int len = (hex.length() / 2);
        byte[] result = new byte[len];
        char[] achar = hex.toCharArray();
        for (int i = 0; i < len; i++) {
            int pos = i * 2;
            result[i] = (byte) (toByte(achar[pos]) << 4 | toByte(achar[pos + 1]));
        }
        return result;
    }

    private static byte toByte(char c) {
        byte b = (byte) "0123456789ABCDEF".indexOf(c);
        return b;
    }

    public static final String hexBytesTo16String(byte[] bArray) {
        StringBuffer sb = new StringBuffer(bArray.length);
        String sTemp;
        for (int i = 0; i < bArray.length; i++) {
            sTemp = Integer.toHexString(0xFF & bArray[i]);
            if (sTemp.length() < 2)
                sb.append(0);
            sb.append(sTemp.toUpperCase());
        }
        return sb.toString();
    }

    /**
     * 用指定连接符号连接多个字符串，忽略空字符串
     *
     * @param split
     * @param params
     * @return
     */
    public static String getStringParamsByFormat(String split, String... params) {
        String format = "%1$s" + split + "%2$s";
        String str = "";
        if (params == null) return str;
        for (int i = 0; i < params.length; i++) {
            if (!StringUtils.isEmpty(params[i])) {
                if (StringUtils.isEmpty(str)) {
                    str = params[i];
                } else {
                    str = String.format(format, str, params[i]);
                }
            }
        }
        return str;
    }

    public static String getStringByLengh(String str, int count) {
        if (str != null && str.length() > count) {
            return str.substring(0, count) + "...";
        }
        return str;
    }

}
