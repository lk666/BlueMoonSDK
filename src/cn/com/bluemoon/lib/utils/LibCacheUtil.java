package cn.com.bluemoon.lib.utils;

import java.io.File;
import java.math.BigDecimal;

import android.content.Context;
import android.text.TextUtils;

public class LibCacheUtil {
	

	  public static void deleteFolderFile(String filePath, boolean deleteThisPath) {
	    if (!TextUtils.isEmpty(filePath)) {
	      try {
	        File file = new File(filePath);
	        if (file.isDirectory()) {
	          File files[] = file.listFiles();
	          for (int i = 0; i < files.length; i++) {
	            deleteFolderFile(files[i].getAbsolutePath(), true);
	          }
	        }
	        if (deleteThisPath) {
	          if (!file.isDirectory()) {
	            file.delete();
	          } else {// Ŀ¼
	            if (file.listFiles().length == 0) {
	              file.delete();
	            }
	          }
	        }
	      } catch (Exception e) {
	        // TODO Auto-generated catch block
	        e.printStackTrace();
	      }
	    }
	  }


	public static long getFolderSize(File file) throws Exception {
	    long size = 0;
	    try {
	      File[] fileList = file.listFiles();
	      for (int i = 0; i < fileList.length; i++) {

	        if (fileList[i].isDirectory()) {
	          size = size + getFolderSize(fileList[i]);
	        } else {
	          size = size + fileList[i].length();
	        }
	      }
	    } catch (Exception e) {
	      e.printStackTrace();
	    }
	    return size;
	  }
	

	  public static String getFormatSize(double size) {
	    double kiloByte = size / 1024;
	    if (kiloByte < 1) {
	      return size + "B";
	    }

	    double megaByte = kiloByte / 1024;
	    if (megaByte < 1) {
	      BigDecimal result1 = new BigDecimal(Double.toString(kiloByte));
	      return result1.setScale(2, BigDecimal.ROUND_HALF_UP)
	          .toPlainString() + "KB";
	    }

	    double gigaByte = megaByte / 1024;
	    if (gigaByte < 1) {
	      BigDecimal result2 = new BigDecimal(Double.toString(megaByte));
	      return result2.setScale(2, BigDecimal.ROUND_HALF_UP)
	          .toPlainString() + "MB";
	    }

	    double teraBytes = gigaByte / 1024;
	    if (teraBytes < 1) {
	      BigDecimal result3 = new BigDecimal(Double.toString(gigaByte));
	      return result3.setScale(2, BigDecimal.ROUND_HALF_UP)
	          .toPlainString() + "GB";
	    }
	    BigDecimal result4 = new BigDecimal(teraBytes);
	    return result4.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString()
	        + "TB";
	  }
	  
	  
	  public static String getCacheSize(File file) throws Exception {
	    return getFormatSize(getFolderSize(file));
	  }
	  

	  public static String getWebViewCacheSize(Context context) {

		  try {
			  File file = new File("/data/data/"
					  + context.getPackageName()+"/app_webview");
			  if(file.exists()){
				  return getCacheSize(file);
			  }
		  }catch (Exception e){
			e.printStackTrace();
		  }
		  return "";
	  }
	  

	  public static String cleanWebViewCache(Context context)
	  {
//		  cleanCustomCache("/data/data/"
//			        + context.getPackageName() + "/app_webview");
		  File file = new File("/data/data/"
			        + context.getPackageName() + "/app_webview");
		  if(file.exists())
		  {
			  deleteFiles(file);
		  }
		  
		  return file.length()+"";
	  }

	public static String cleanWebViewCache(String packageName)
	{
		File file = new File("/data/data/"
				+ packageName + "/app_webview");
		if(file.exists())
		{
			deleteFiles(file);
		}

		return file.length()+"";
	}
	  

	  public static void deleteFiles(File dir)
	  {
		  File[] files = dir.listFiles();
		  for (int i = 0; i < files.length; i++) 
		  {
			if(files[i].isFile())
			{
				files[i].delete();
			}
			else if(files[i].isDirectory())
			{
				deleteFiles(files[i]);
			}
		  }
	  }
	  

}


