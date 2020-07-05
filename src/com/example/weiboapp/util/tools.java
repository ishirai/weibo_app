package com.example.weiboapp.util;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
// import java.util.ArrayList;
import java.util.List;

// import com.example.weiboapp.pojo.WeiboInfo;

import android.graphics.drawable.Drawable;
import weibo4j.Comments;
import weibo4j.Timeline;
import weibo4j.Users;
import weibo4j.model.CommentWapper;
import weibo4j.model.Paging;
import weibo4j.model.Status;
// import weibo4j.examples.oauth2.Log;
// import weibo4j.examples.oauth2.Log;
// import weibo4j.model.PostParameter;
// import weibo4j.model.Status;
import weibo4j.model.StatusWapper;
import weibo4j.model.User;
import weibo4j.model.WeiboException;
// import weibo4j.util.WeiboConfig;

/**
 * 工具类
 * @author SHILEI
 *
 */

public class tools {
	
	// private static final String TAG = "Tools";
	private static tools instance = null;
	// 修改为获得的access_token和uid
	private static String access_token = "2.00O1x6UHwlLtBE071b6fbf2cBsNJZD";
	private static String uid = "6862406154";
	
	public static tools getInstance(){
		if (instance==null) {
			instance = new tools();
		}
		return instance;
	}
	
	public Status loadShow(String weiboID) {
		Timeline tm = new Timeline(access_token);
		try {
			Status status = tm.showStatus(weiboID);
			return status;
		} catch (WeiboException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 根据id获得微博评论
	 * @param id
	 * @return
	 */
	public CommentWapper getComents(String id) {
		Comments cm = new Comments(access_token);
		try {
			CommentWapper comments = cm.getCommentById(id);
			return comments;
			// Log.logInfo(comment.toString());
		} catch (WeiboException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static Drawable getDrawableFromUrl(String url) {
		try {
			URLConnection urls = new URL(url).openConnection();
			return Drawable.createFromStream(urls.getInputStream(), "image");
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 获取微博首页信息
	 * @return
	 */
	public List<Status> getHomeData(Paging paging) {
		// String access_token = "2.00O1x6UHwlLtBE071b6fbf2cBsNJZD";
		Timeline tm = new Timeline(access_token);
		try {
			tm.getHomeTimeline(null, null, paging);
			List<Status> statusm = tm.getHomeTimeline().getStatuses();
			return statusm;
			// Log.logInfo(statusm.toString());
		} catch (WeiboException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 获取当前用户信息
	 * @return
	 */
	public User getUserInfo() {
		// String access_token = "2.00O1x6UHwlLtBE071b6fbf2cBsNJZD";
		// String uid = "6862406154";
		Users um = new Users(access_token);
		try {
			User user = um.showUserById(uid);
			// 保存当前登录用户
			UserSession.nowUser = user;
			return user;
			// Log.logInfo(user.toString());
		} catch (WeiboException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static class UserSession{
		public static User nowUser;
	}
}
	
	
