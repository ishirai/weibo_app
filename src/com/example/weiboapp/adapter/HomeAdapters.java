package com.example.weiboapp.adapter;

import java.util.List;

import com.example.weiboapp.R;
import com.example.weiboapp.util.AsyncImageLoader;
import com.example.weiboapp.util.AsyncImageLoader.ImageCallback;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
// import weibo4j.examples.oauth2.Log;
import weibo4j.model.Status;
import weibo4j.model.StatusWapper;

/**
 * 微博内容adapter类
 * @author SHILEI
 *
 */

public class HomeAdapters extends BaseAdapter {

	// private StatusWapper weiboInfoList = null;
	private List<Status> weiboInfoList = null;
	private Context context = null;
	public HomeAdapters(Context context, List<Status> weiboInfoList) {
		this.context=context;
		this.weiboInfoList = weiboInfoList;	
	}
	
	class ContentHolder{
		public ImageView content_image;
		public ImageView content_user_head;
		public TextView content_user_name;
		public TextView content_comment;
		public TextView content_text;
	}
	
	AsyncImageLoader asyncImageLoader = null;
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return (int) this.weiboInfoList.size()+1;
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return this.weiboInfoList.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		if(arg0<this.getCount()-1) {
			return arg0-1;
		}else {
			return -1;    // 最后一项 加载更多
		}
	}
	
	public void addMoreData(List<Status> moreStatus) {
		this.weiboInfoList.addAll(moreStatus);
		this.notifyDataSetChanged();
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		if(arg0==getCount()-1) {    //最后一项 更多
			View view1 = LayoutInflater.from(context).inflate(R.layout.list_moreitems, null);
			TextView tv1 = (TextView)view1.findViewById(R.id.refresh);
			tv1.setText("more");
			return view1;
		}
		arg1 = View.inflate(context, R.layout.home_item, null);
		ContentHolder holder = new ContentHolder();
		// 关联组件，提高效率
		holder.content_image = (ImageView)arg1.findViewById(R.id.content_image);
		holder.content_user_head = (ImageView)arg1.findViewById(R.id.content_head);
		holder.content_user_name = (TextView)arg1.findViewById(R.id.content_user);
		holder.content_comment = (TextView)arg1.findViewById(R.id.content_comment);
		holder.content_text = (TextView)arg1.findViewById(R.id.content_text);
		Status weibo = weiboInfoList.get(arg0);
		if(weibo!=null) {
			// 设置标签 方便下次获得
			arg1.setTag(weibo.getId());
			holder.content_user_name.setText(weibo.getUser().getScreenName());
			String s = "Repost:" + weibo.getRepostsCount() + " Comment:"+weibo.getCommentsCount();
			holder.content_comment.setText(s);
			holder.content_text.setText(weibo.getText());
			String url = weibo.getThumbnailPic();
			// 判断微博中是否包含图片
			if(url!="") {
				// 使用异步下载图片
				Drawable image = AsyncImageLoader.loadDrawable(url, holder.content_image, new ImageCallback() {

					@Override
					public void imageSet(Drawable drawable, ImageView iv) {
						// TODO Auto-generated method stub
						iv.setImageDrawable(drawable);
					}
					
				});
				if(image==null) {
					holder.content_image.setImageResource(R.drawable.ic_launcher);
				}else {
					holder.content_image.setImageDrawable(image);
				}	
			}
			
			// 使用异步下载微博用户头像图片
			Drawable head_image = AsyncImageLoader.loadDrawable(weibo.getUser().getavatarLarge(), holder.content_user_head, new ImageCallback() {

				@Override
				public void imageSet(Drawable drawable, ImageView iv) {
					// TODO Auto-generated method stub
					iv.setImageDrawable(drawable);
				}
				
			});
			if(head_image==null) {
				holder.content_user_head.setImageResource(R.drawable.ic_launcher);
			}else {
				holder.content_user_head.setImageDrawable(head_image);
			}
			
		}
		
		return arg1;
	}

}
