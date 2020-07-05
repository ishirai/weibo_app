package com.example.weiboapp.util;

import java.lang.ref.SoftReference;
import java.util.HashMap;

import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;

public class AsyncImageLoader {
	/**
	 * 图片数据缓存 key=url value=图片资源对象
	 */
	private static HashMap<String,SoftReference<Drawable>> imageCache;
	
	public AsyncImageLoader() {
		if(imageCache==null) {
			imageCache = new HashMap<String,SoftReference<Drawable>>();
			
		}
	}
	/**
	 * 异步下载图片
	 * @param url 图片的地址
	 * @param imageView 需要显示图片的组件
	 * @param callback 回调接口
	 * @return 图片资源
	 */
	public static Drawable loadDrawable(final String url, final ImageView imageView, final ImageCallback callback) {
		// 判断曾经是否已经下载过，如果下载过，直接获得并返回
		if(imageCache.containsKey(url)) {
			SoftReference<Drawable> soft = imageCache.get(url);
			Drawable dra=soft.get();
			if(dra!=null) {
				return dra;
			}
		}
		
		final Handler handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				// 图片资源设置操作
				callback.imageSet((Drawable)msg.obj, imageView);
			}
		};
		// 下载操作
		new Thread() {
			public void run() {
				Drawable drawable = tools.getDrawableFromUrl(url);
				// 设置缓存，避免重复下载相同的图片资源
				imageCache.put(url,new SoftReference<Drawable>(drawable));
				Message msg=handler.obtainMessage();
				msg.obj = drawable;
				handler.sendMessage(msg);
			}
		}.start();
		
		return null;
	}
	/**
	 * 回调接口
	 * @author SHILEI
	 *
	 */
	public interface ImageCallback{
		/**
		 * 图片资源设置操作
		 * @param drawable
		 * @param iv
		 */
		public void imageSet(Drawable drawable, ImageView iv);
	}

}
