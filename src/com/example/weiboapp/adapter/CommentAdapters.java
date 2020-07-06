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
import weibo4j.model.Comment;
import weibo4j.model.Status;


/**
 * 微博评论类
 * @author SHILEI
 *
 */
public class CommentAdapters extends BaseAdapter {

	private List<Comment> commentList = null;
	private Context context = null;
	public CommentAdapters(Context context, List<Comment> commentList) {
		this.context = context;
		this.commentList = commentList;
	}
	
	class CommentHolder{
		public ImageView comment_user_head;
		public TextView comment_user;
		public TextView comment_text;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return this.commentList.size()+1;
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return this.commentList.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		if(arg0<this.getCount()-1) {
			return arg0;
		}else {
			return -1;
		}
	}
	
	public void addMoreData(List<Comment> moreComments) {
		this.commentList.addAll(moreComments);
		this.notifyDataSetChanged();
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		// TODO Auto-generated method stub
		
		if(arg0==getCount()-1) {    //最后一项 更多
			View view1 = LayoutInflater.from(context).inflate(R.layout.list_moreitems, null);
			TextView tv1 = (TextView)view1.findViewById(R.id.refresh);
			tv1.setText("more");
			return view1;
		}
		
		arg1 = View.inflate(context, R.layout.comment_item, null);
		CommentHolder holder = new CommentHolder();
		holder.comment_user_head = (ImageView)arg1.findViewById(R.id.comment_user_head);
		holder.comment_user = (TextView)arg1.findViewById(R.id.comment_user);
		holder.comment_text = (TextView)arg1.findViewById(R.id.comment_text);
		Comment comment = commentList.get(arg0);
		
		if(comment!=null) {
			arg1.setTag(comment.getId());
			holder.comment_user.setText(comment.getUser().getScreenName());
			holder.comment_text.setText(comment.getText());
			Drawable head_image = AsyncImageLoader.loadDrawable(comment.getUser().getavatarLarge(), holder.comment_user_head, new ImageCallback() {

				@Override
				public void imageSet(Drawable drawable, ImageView iv) {
					// TODO Auto-generated method stub
					iv.setImageDrawable(drawable);
				}
				
			});
			if(head_image==null) {
				holder.comment_user_head.setImageResource(R.drawable.ic_launcher);
			}else {
				holder.comment_user_head.setImageDrawable(head_image);
			}
			
		}
		
		return arg1;
	}

}
