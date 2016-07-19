package cn.com.bluemoon.lib.utils;

import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import cn.com.bluemoon.lib.callback.DrawableCallback;

public class DownImageHelper {
	
	private String TAG = "DownLoadImageHelper";

	private static InputStream download(String urlString) throws MalformedURLException, IOException {
	    InputStream inputStream = (InputStream) new URL(urlString).getContent();
	    return inputStream;
	}
	
	public static Map<String, Drawable> cache = new HashMap<String, Drawable>();

	public void loadImage(final ImageView imageView, final String urlString, boolean useCache,final DrawableCallback cb) {
	    if(urlString == null)
	    {
	    	if(cb!=null) cb.onFailure("url id null");
	    	return;
	    }
		
		if (useCache && cache.containsKey(urlString)) {
			imageView.setImageDrawable(cache.get(urlString));
			if(cb!=null) cb.onSuccess(cache.get(urlString));
	        return;
	    }
		
	    //Show a "Loading" image here
//	    imageView.setImageResource(R.drawable.bluemoon_image_default_white);

	    Log.v(TAG, "Image url:" + urlString);

	    final Handler handler = new Handler() {
	        @Override
	        public void handleMessage(Message message) {
	        	if(cb!=null) cb.onPostExec();
	        	try {
	        		if(message.obj!=null)
	        		{
	        			imageView.setImageDrawable((Drawable) message.obj);
	        			if(cb!=null) cb.onSuccess((Drawable) message.obj);
	        		}
	        		else
	        		{
	        			if(cb!=null) cb.onFailure("drawable is null");
	        		}
	    	            
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
					if(cb!=null) cb.onFailure(e.toString());
				}
	        }
	    };

	    Runnable runnable = new Runnable() {
	        public void run() {
	            Drawable drawable = null;
	            try {
	                InputStream is = download(urlString);
	                drawable = Drawable.createFromStream(is, "src");

	                if (drawable != null) {
	                    cache.put(urlString, drawable);
	                }
	            } catch (Exception e) {
	                Log.e(this.getClass().getSimpleName(), "Image download failed", e);
	                //Show a "download fail" image 
//	                drawable = imageView.getResources().getDrawable(R.drawable.test_accout_img1);
	            }
	            
	            //Notify UI thread to show this image using Handler
	            Message msg = handler.obtainMessage(1, drawable);
	            handler.sendMessage(msg);
	            
	        }
	    };
	    if(cb!=null) cb.onPreExec();
	    new Thread(runnable).start();
	   }
	
	public void loadImage(ImageView imageView,String urlString, boolean useCache)
	{
		loadImage(imageView, urlString, useCache, null);
	}

}
