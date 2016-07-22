package cn.com.bluemoon.lib.utils;

import android.annotation.SuppressLint;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;

import org.apache.commons.lang3.StringUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class LibFileUtil {

		public static void write(Context context, String fileName, String content) {
			if (content == null)
				content = "";

			try {
				FileOutputStream fos = context.openFileOutput(fileName,
						Context.MODE_PRIVATE);
				fos.write(content.getBytes());

				fos.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}


		public static String read(Context context, String fileName) {
			try {
				FileInputStream in = context.openFileInput(fileName);
				return readInStream(in);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return "";
		}


		public static String readInStream(InputStream inStream) {
			try {
				ByteArrayOutputStream outStream = new ByteArrayOutputStream();
				byte[] buffer = new byte[512];
				int length = -1;
				while ((length = inStream.read(buffer)) != -1) {
					outStream.write(buffer, 0, length);
				}

				outStream.close();
				inStream.close();
				return outStream.toString();
			} catch (IOException e) {
				Log.i("FileTest", e.getMessage());
			}
			return null;
		}


		public static File createFile(String folderPath, String fileName) {
			File destDir = new File(folderPath);
			if (!destDir.exists()) {
				destDir.mkdirs();
			}
			return new File(folderPath, fileName + fileName);
		}

		public static boolean writeFile(byte[] buffer, String folder,
				String fileName) {
			boolean writeSucc = false;

			boolean sdCardExist = Environment.getExternalStorageState().equals(
					android.os.Environment.MEDIA_MOUNTED);

			String folderPath = "";
			if (sdCardExist) {
				folderPath = Environment.getExternalStorageDirectory()
						+ File.separator + folder + File.separator;
			} else {
				writeSucc = false;
			}

			File fileDir = new File(folderPath);
			if (!fileDir.exists()) {
				fileDir.mkdirs();
			}

			File file = new File(folderPath + fileName);
			FileOutputStream out = null;
			try {
				out = new FileOutputStream(file);
				out.write(buffer);
				writeSucc = true;
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			return writeSucc;
		}


		public static String getFileName(String filePath) {
			if (StringUtils.isEmpty(filePath))
				return "";
			return filePath.substring(filePath.lastIndexOf(File.separator) + 1);
		}


		public static String getFileNameNoFormat(String filePath) {
			if (StringUtils.isEmpty(filePath)) {
				return "";
			}
			int point = filePath.lastIndexOf('.');
			return filePath.substring(filePath.lastIndexOf(File.separator) + 1,
					point);
		}


		public static String getFileFormat(String fileName) {
			if (StringUtils.isEmpty(fileName))
				return "";

			int point = fileName.lastIndexOf('.');
			return fileName.substring(point + 1);
		}


		public static long getFileSize(String filePath) {
			long size = 0;

			File file = new File(filePath);
			if (file != null && file.exists()) {
				size = file.length();
			}
			return size;
		}


		public static String getFileSize(long size) {
			if (size <= 0)
				return "0";
			java.text.DecimalFormat df = new java.text.DecimalFormat("##.##");
			float temp = (float) size / 1024;
			if (temp >= 1024) {
				return df.format(temp / 1024) + "M";
			} else {
				return df.format(temp) + "K";
			}
		}


		public static String formatFileSize(long fileS) {
			java.text.DecimalFormat df = new java.text.DecimalFormat("#.00");
			String fileSizeString = "";
			if (fileS < 1024) {
				fileSizeString = df.format((double) fileS) + "B";
			} else if (fileS < 1048576) {
				fileSizeString = df.format((double) fileS / 1024) + "KB";
			} else if (fileS < 1073741824) {
				fileSizeString = df.format((double) fileS / 1048576) + "MB";
			} else {
				fileSizeString = df.format((double) fileS / 1073741824) + "G";
			}
			return fileSizeString;
		}

		public static long getDirSize(File dir) {
			if (dir == null) {
				return 0;
			}
			if (!dir.isDirectory()) {
				return 0;
			}
			long dirSize = 0;
			File[] files = dir.listFiles();
			for (File file : files) {
				if (file.isFile()) {
					dirSize += file.length();
				} else if (file.isDirectory()) {
					dirSize += file.length();
					dirSize += getDirSize(file); 
				}
			}
			return dirSize;
		}


		public long getFileList(File dir) {
			long count = 0;
			File[] files = dir.listFiles();
			count = files.length;
			for (File file : files) {
				if (file.isDirectory()) {
					count = count + getFileList(file);
					count--;
				}
			}
			return count;
		}

		public static byte[] toBytes(InputStream in) throws IOException {
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			int ch;
			while ((ch = in.read()) != -1) {
				out.write(ch);
			}
			byte buffer[] = out.toByteArray();
			out.close();
			return buffer;
		}

		public static boolean checkFileExists(String name) {
			boolean status;
			if (!name.equals("")) {
				File path = Environment.getExternalStorageDirectory();
				File newPath = new File(path.toString() + name);
				status = newPath.exists();
			} else {
				status = false;
			}
			return status;
		}

		public static boolean checkFilePathExists(String path) {
			return new File(path).exists();
		}


		public static long getFreeDiskSpace() {
			String status = Environment.getExternalStorageState();
			long freeSpace = 0;
			if (status.equals(Environment.MEDIA_MOUNTED)) {
				try {
					File path = Environment.getExternalStorageDirectory();
					StatFs stat = new StatFs(path.getPath());
					long blockSize = stat.getBlockSize();
					long availableBlocks = stat.getAvailableBlocks();
					freeSpace = availableBlocks * blockSize / 1024;
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
				return -1;
			}
			return (freeSpace);
		}


		public static boolean createDirectory(String directoryName) {
			boolean status;
			if (!directoryName.equals("")) {
				File path = Environment.getExternalStorageDirectory();
				File newPath = new File(path.toString() + directoryName);
				status = newPath.mkdir();
				status = true;
			} else
				status = false;
			return status;
		}


		public static boolean checkSaveLocationExists() {
			String sDCardStatus = Environment.getExternalStorageState();
			boolean status;
			if (sDCardStatus.equals(Environment.MEDIA_MOUNTED)) {
				status = true;
			} else
				status = false;
			return status;
		}
		

		public static boolean checkExternalSDExists() {
			
			Map<String, String> evn = System.getenv();
			return evn.containsKey("SECONDARY_STORAGE");
		}


		public static boolean deleteDirectory(String fileName) {
			boolean status;
			SecurityManager checker = new SecurityManager();
			
			if (!fileName.equals("")) {

				File path = Environment.getExternalStorageDirectory();
				File newPath = new File(path.toString() + fileName);
				checker.checkDelete(newPath.toString());
				if (newPath.isDirectory()) {
					String[] listfile = newPath.list();
					try {
						for (int i = 0; i < listfile.length; i++) {
							File deletedFile = new File(newPath.toString() + "/"
									+ listfile[i].toString());
							deletedFile.delete();
						}
						newPath.delete();
						status = true;
					} catch (Exception e) {
						e.printStackTrace();
						status = false;
					}

				} else
					status = false;
			} else
				status = false;
			return status;
		}


		public static boolean deleteFile(String fileName) {
			boolean status;
			SecurityManager checker = new SecurityManager();

			if (!fileName.equals("")) {

				File path = Environment.getExternalStorageDirectory();
				File newPath = new File(path.toString() + fileName);
				checker.checkDelete(newPath.toString());
				if (newPath.isFile()) {
					try {
						newPath.delete();
						status = true;
					} catch (SecurityException se) {
						se.printStackTrace();
						status = false;
					}
				} else
					status = false;
			} else
				status = false;
			return status;
		}


		public static int deleteBlankPath(String path) {
			File f = new File(path);
			if (!f.canWrite()) {
				return 1;
			}
			if (f.list() != null && f.list().length > 0) {
				return 2;
			}
			if (f.delete()) {
				return 0;
			}
			return 3;
		}
		

	    public static void copyfile(File fromFile, File toFile,Boolean rewrite ){  
	          
	        if(!fromFile.exists()){  
	            return;  
	        }  
	          
	        if(!fromFile.isFile()){  
	            return;  
	        }  
	        if(!fromFile.canRead()){  
	            return;  
	        }  
	        if(!toFile.getParentFile().exists()){  
	            toFile.getParentFile().mkdirs();  
	        }  
	        if(toFile.exists() && rewrite)
	        {  
	            toFile.delete();  
	        }  
	          
	          
	        try {  
	            FileInputStream fosfrom = new FileInputStream(fromFile);  
	            FileOutputStream fosto = new FileOutputStream(toFile);  
	              
	            byte[] bt = new byte[1024];  
	            int c;  
	            while((c=fosfrom.read(bt)) > 0){  
	                fosto.write(bt,0,c);  
	            }  

	            fosfrom.close();  
	            fosto.close();  
	              
	              
	        } catch (FileNotFoundException e) {  
	            // TODO Auto-generated catch block  
	            e.printStackTrace();  
	        } catch (IOException e) {  
	            // TODO Auto-generated catch block  
	            e.printStackTrace();  
	        }  
	          
	    }  

		public static boolean reNamePath(String oldName, String newName) {
			File f = new File(oldName);
			return f.renameTo(new File(newName));
		}


		public static boolean deleteFileWithPath(String filePath) {
			SecurityManager checker = new SecurityManager();
			File f = new File(filePath);
			checker.checkDelete(filePath);
			if (f.isFile()) {
				f.delete();
				return true;
			}
			return false;
		}
		

		public static void clearFileWithPath(String filePath) {
			List<File> files = LibFileUtil.listPathFiles(filePath);
			if (files.isEmpty()) {
				return;
			}
			for (File f : files) {
				if (f.isDirectory()) {
					clearFileWithPath(f.getAbsolutePath());
				} else {
					f.delete();
				}
			}
		}


		public static String getSDRoot() {
			
			return Environment.getExternalStorageDirectory().getAbsolutePath();
		}
		

		public static String getExternalSDRoot() {
			
			Map<String, String> evn = System.getenv();
			
			return evn.get("SECONDARY_STORAGE");
		}


		public static List<String> listPath(String root) {
			List<String> allDir = new ArrayList<String>();
			SecurityManager checker = new SecurityManager();
			File path = new File(root);
			checker.checkRead(root);

			if (path.isDirectory()) {
				for (File f : path.listFiles()) {
					if (f.isDirectory() && !f.getName().startsWith(".")) {
						allDir.add(f.getAbsolutePath());
					}
				}
			}
			return allDir;
		}
		

		public static List<File> listPathFiles(String root) {
			List<File> allDir = new ArrayList<File>();
			SecurityManager checker = new SecurityManager();
			File path = new File(root);
			checker.checkRead(root);
			File[] files = path.listFiles();
			for (File f : files) {
				if (f.isFile())
					allDir.add(f);
				else 
					listPath(f.getAbsolutePath());
			}
			return allDir;
		}

		public enum PathStatus {
			SUCCESS, EXITS, ERROR
		}


		public static PathStatus createPath(String newPath) {
			File path = new File(newPath);
			if (path.exists()) {
				return PathStatus.EXITS;
			}
			if (path.mkdir()) {
				return PathStatus.SUCCESS;
			} else {
				return PathStatus.ERROR;
			}
		}


		public static String getPathName(String absolutePath) {
			int start = absolutePath.lastIndexOf(File.separator) + 1;
			int end = absolutePath.length();
			return absolutePath.substring(start, end);
		}
		

		public static String getAppCache(Context context, String dir) {
			String savePath = context.getCacheDir().getAbsolutePath() + "/" + dir + "/";
			File savedir = new File(savePath);
			if (!savedir.exists()) {
				savedir.mkdirs();
			}
			savedir = null;
			return savePath;
		}

	public static File downLoadFile(String httpUrl) {

		String fileName = httpUrl.substring(httpUrl.lastIndexOf("/") + 1);

		if (fileName.length() > 20) {
			fileName = fileName.substring(fileName.length() - 20);
		}

		String dirPath = getSDRoot() + File.separator + "download";

		File tmpFile = new File(dirPath);
		if (!tmpFile.exists()) {
			tmpFile.mkdirs();
		}
		File file = new File(dirPath + File.separator + fileName);

		URL url;
		try {
			url = new URL(httpUrl);
			try {
				HttpURLConnection conn = (HttpURLConnection) url
						.openConnection();
				InputStream is = conn.getInputStream();
				FileOutputStream fos = new FileOutputStream(file);
				byte[] buf = new byte[256];
				conn.connect();
				double count = 0;
				if (conn.getResponseCode() >= 400) {

				} else {
					while (count <= 100) {
						if (is != null) {
							int numRead = is.read(buf);
							if (numRead <= 0) {
								break;
							} else {
								fos.write(buf, 0, numRead);
							}
						} else {
							break;
						}
					}
				}
				conn.disconnect();
				fos.close();
				is.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return file;

	}


	public static void openFile(String filepath, Context context) {
		File file = new File(filepath);
		if (file.exists()) {
			openFile(file, context);
		} else {
			Log.e("debug", "file is not exists!");
		}
	}

	public static void openFile(File file, Context context) {

		Log.e("OpenFile", file.getName());

		Intent intent = new Intent();

		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

		intent.setAction(android.content.Intent.ACTION_VIEW);

		intent.setDataAndType(Uri.fromFile(file),
				"application/vnd.android.package-archive");

		context.startActivity(intent);
	}


	/**
	 * @param uri The Uri to check.
	 * @return Whether the Uri authority is ExternalStorageProvider.
	 */
	public static boolean isExternalStorageDocument(Uri uri) {
		return "com.android.externalstorage.documents".equals(uri.getAuthority());
	}

	/**
	 * @param uri The Uri to check.
	 * @return Whether the Uri authority is DownloadsProvider.
	 */
	public static boolean isDownloadsDocument(Uri uri) {
		return "com.android.providers.downloads.documents".equals(uri.getAuthority());
	}

	/**
	 * @param uri The Uri to check.
	 * @return Whether the Uri authority is MediaProvider.
	 */
	public static boolean isMediaDocument(Uri uri) {
		return "com.android.providers.media.documents".equals(uri.getAuthority());
	}

	/**
	 * Get the value of the data column for this Uri. This is useful for
	 * MediaStore Uris, and other file-based ContentProviders.
	 *
	 * @param context       The context.
	 * @param uri           The Uri to query.
	 * @param selection     (Optional) Filter used in the query.
	 * @param selectionArgs (Optional) Selection arguments used in the query.
	 * @return The value of the _data column, which is typically a file path.
	 */
	public static String getDataColumn(Context context, Uri uri, String selection,
									   String[] selectionArgs) {

		Cursor cursor = null;
		final String column = "_data";
		final String[] projection = {
				column
		};

		try {
			cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
					null);
			if (cursor != null && cursor.moveToFirst()) {
				final int column_index = cursor.getColumnIndexOrThrow(column);
				return cursor.getString(column_index);
			}
		} finally {
			if (cursor != null)
				cursor.close();
		}
		return null;
	}

	/**
	 * Get a file path from a Uri. This will get the the path for Storage Access
	 * Framework Documents, as well as the _data field for the MediaStore and
	 * other file-based ContentProviders.
	 *
	 * @param context The context.
	 * @param uri     The Uri to query.
	 * @author paulburke
	 */
	@SuppressLint("NewApi")
	public static String getPath(final Context context, final Uri uri) {

		final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

		// DocumentProvider
		if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
			// ExternalStorageProvider
			if (isExternalStorageDocument(uri)) {
				final String docId = DocumentsContract.getDocumentId(uri);
				final String[] split = docId.split(":");
				final String type = split[0];

				if ("primary".equalsIgnoreCase(type)) {
					return Environment.getExternalStorageDirectory() + "/" + split[1];
				}

				// TODO handle non-primary volumes
			}
			// DownloadsProvider
			else if (isDownloadsDocument(uri)) {

				final String id = DocumentsContract.getDocumentId(uri);
				final Uri contentUri = ContentUris.withAppendedId(
						Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

				return getDataColumn(context, contentUri, null, null);
			}
			// MediaProvider
			else if (isMediaDocument(uri)) {
				final String docId = DocumentsContract.getDocumentId(uri);
				final String[] split = docId.split(":");
				final String type = split[0];

				Uri contentUri = null;
				if ("image".equals(type)) {
					contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
				} else if ("video".equals(type)) {
					contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
				} else if ("audio".equals(type)) {
					contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
				}

				final String selection = "_id=?";
				final String[] selectionArgs = new String[]{
						split[1]
				};

				return getDataColumn(context, contentUri, selection, selectionArgs);
			}
		}
		// MediaStore (and general)
		else if ("content".equalsIgnoreCase(uri.getScheme())) {
			return getDataColumn(context, uri, null, null);
		}
		// File
		else if ("file".equalsIgnoreCase(uri.getScheme())) {
			return uri.getPath();
		}

		return null;
	}
}
  
