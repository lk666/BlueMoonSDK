package cn.com.bluemoon.lib.utils;

import java.util.regex.Pattern;

public class LibStringUtil {

    public static final boolean isEmpty(String str) {
        return str == null || "".equals(str.trim());
    }

    public static final boolean isNotEmpty(String s) {
        return s != null && s.length() > 0;
    }

    public static final String appendSemicolon(String... str) {
        StringBuffer strBuffer = new StringBuffer();
        return strBuffer.toString();
    }

    public static String ToDBC(String input) {
        char[] c = input.toCharArray();
        for (int i = 0; i < c.length; i++) {
            if (c[i] == 12288) {
                c[i] = (char) 32;
                continue;
            }
            if (c[i] > 65280 && c[i] < 65375)
                c[i] = (char) (c[i] - 65248);
        }
        return new String(c);
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


    public static String getStringForName(String source) {
        String str = "";
        source = source.replaceAll(" ", "");
        char[] chars = source.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            String codePoint = String.valueOf(chars[i]);

            if (isChinese(codePoint) || isNumeric(codePoint) || isLetter(codePoint)
                    || "-".equalsIgnoreCase(codePoint) || "_".equalsIgnoreCase(codePoint)
                    || "�C".equalsIgnoreCase(codePoint) || "��".equalsIgnoreCase(codePoint)) {
                str += codePoint;
            } else {
                str += "";
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

}
