package com.example.weiboapp;

import java.util.ArrayList;
import java.util.List;

import com.example.weiboapp.adapter.HomeAdapters;
import com.example.weiboapp.util.tools;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import weibo4j.model.Paging;
import weibo4j.model.Status;
import weibo4j.model.StatusWapper;

/*
 * 主页页面
 * 显示微博列表页面
 */
public class HomeActivity extends Activity{
	
	TextView weibo_name;
	ListView home_lv;
	public int nowpage=1;    // 第几页
	public int pagesize=20;    // 每页条数
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home);
		
		weibo_name = (TextView)findViewById(R.id.we_name);
		home_lv = (ListView)findViewById(R.id.home_lv);
		init();
	}
	public void init() {
		Paging p = new Paging(nowpage,pagesize);
		List<Status> data = tools.getInstance().getHomeData(p);
		HomeAdapters adapter = new HomeAdapters(this,data);
		home_lv.setAdapter(adapter);
		home_lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				// TODO Auto-generated method stub
				if(arg3==-1) {    // 更多
					nowpage++;
					Paging p = new Paging(nowpage,pagesize);
					List<Status> moreStatus = tools.getInstance().getHomeData(p);
					((HomeAdapters)home_lv.getAdapter()).addMoreData(moreStatus);
				}else {
					Object obj=arg1.getTag();
					if(obj!=null) {
						String weiboID = obj.toString();
						Intent intent = new Intent(HomeActivity.this,ContentActivity.class);
						Bundle bundle = new Bundle();
						bundle.putString("weiboID", weiboID);
						intent.putExtras(bundle);
						startActivity(intent);
					}
				}
			}
			
		});
	}
}
