package com.example.weiboapp;

import java.util.List;

import com.example.weiboapp.adapter.CommentAdapters;
import com.example.weiboapp.util.AsyncImageLoader;
import com.example.weiboapp.util.tools;
import com.example.weiboapp.util.AsyncImageLoader.ImageCallback;

import android.app.ActionBar.LayoutParams;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import weibo4j.model.Comment;
import weibo4j.model.CommentWapper;
import weibo4j.model.Paging;
import weibo4j.model.Status;
import weibo4j.model.User;

public class ContentActivity extends Activity {
	
	ListView comment_lv;
	public int nowpage=1;    // 第几页
	public int pagesize=20;    // 每页条数
	public String id = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.content);
		comment_lv = (ListView)findViewById(R.id.comments_lv);
		Intent in = this.getIntent();
		if(in!=null) {
			Bundle b = in.getExtras();
			if(b!=null) {
				if(b.containsKey("weiboID")) {
					String weiboID = b.getString("weiboID");
					init(weiboID);
				}
			}
		}
	}
	public void init(String weiboID) {
		Status data = tools.getInstance().loadShow(weiboID);
		
		id = data.getId();
		Paging p = new Paging(nowpage,pagesize);
		List<Comment> commentlv = tools.getInstance().getComents(id,p).getComments();
		CommentAdapters adapter = new CommentAdapters(this,commentlv);
		
		if(data!=null) {
			// 获得微博发送者信息
			User user = data.getUser();
			String user_name = user.getScreenName();
			String user_head = user.getProfileImageUrl();
			String comments = "repost:"+data.getRepostsCount()+" Comment:"+data.getCommentsCount();
			String text = data.getText();
			TextView tv = (TextView)this.findViewById(R.id.user_name);
			tv.setText(user_name);
			TextView cm = (TextView)this.findViewById(R.id.comments);
			cm.setText(comments);
			TextView txt = (TextView)this.findViewById(R.id.text);
			txt.setText(text);
			ImageView head = (ImageView)this.findViewById(R.id.user_head);
			
			Drawable head_image = AsyncImageLoader.loadDrawable(user_head, head, new ImageCallback() {

				@Override
				public void imageSet(Drawable drawable, ImageView iv) {
					// TODO Auto-generated method stub
					iv.setImageDrawable(drawable);
				}
				
			});
			if(head_image==null) {
				head.setImageResource(R.drawable.ic_launcher);
			}else {
				head.setImageDrawable(head_image);
			}
			
			// 获得微博图片
			if(data.getBmiddlePic()!="") {
				String picurl = data.getBmiddlePic();
				String picurl2 = data.getOriginalPic();
				ImageView pic = findViewById(R.id.pic);
				pic.setTag(picurl2);
				pic.setOnClickListener(new OnClickListener() {
					public void onClick(View v) {
						Object obj = v.getTag();
						Intent intent = new Intent(ContentActivity.this,null);
						Bundle b = new Bundle();
						b.putString("url", obj.toString());
						intent.putExtras(b);
						startActivity(intent);
					}
				});
				Drawable cachedImage2 = AsyncImageLoader.loadDrawable(picurl, pic, new ImageCallback() {
					public void imageSet(Drawable imageDrawable,ImageView imageView) {
						showImg(imageView,imageDrawable);
					}
				});
				if(cachedImage2!=null) {
					showImg(pic,cachedImage2);
				}
			}
			
			
		}
		
		comment_lv.setAdapter(adapter);
		comment_lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				// TODO Auto-generated method stub
				if(arg3==-1) {    // 更多
					nowpage++;
					Paging p = new Paging(nowpage,pagesize);
					List<Comment> moreComment = tools.getInstance().getComents(id, p).getComments();
					((CommentAdapters)comment_lv.getAdapter()).addMoreData(moreComment);
				}
				}
		});
		
	}

	
	/**
	 * 动态调整图片宽高
	 * @param view
	 * @param img
	 */
	@SuppressLint("NewApi")
	private void showImg(ImageView view, Drawable img) {
		int w = img.getIntrinsicWidth();
		int h = img.getIntrinsicHeight();
		if(w>300) {
			int hh = 300*h/w;
			LayoutParams para = (LayoutParams) view.getLayoutParams();
			para.width = 300;
			para.height = hh;
			view.setLayoutParams(para);
		}
		view.setImageDrawable(img);
	}
}
