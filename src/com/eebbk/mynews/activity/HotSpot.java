package com.eebbk.mynews.activity;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import com.eebbk.mynews.MainActivity;
import com.eebbk.mynews.R;
import com.eebbk.mynews.subscription.HotDetails;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class HotSpot extends Activity{

	//配置您申请的KEY
	public static final String APPKEY ="c2b2f7b346e807ae1c557b77b7c84f06";

	private Handler handler = new  Handler(){
		public void handleMessage(android.os.Message msg) {
			lists = (List<String>) msg.obj;
			switch (msg.what) {
			case 100:
				Log.i("aaa", "ok");
				for(int i = 0;i<lists.size();i++){
					Log.i("aaa", lists.get(i)+"wj");
				}
				ArrayAdapter<String> mAdapter = new ArrayAdapter<String>(HotSpot.this, R.layout.item_hotspot_news_list,
						R.id.tv_hot_item, lists);
				mListView.setAdapter(mAdapter);
				break;
			default:
				break;
			}
			mShowHot.setText(lists.toString());
		};
	};
	private TextView mShowHot;
	private ListView mListView;
	private List<String> lists = new ArrayList<String>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_hotspot_main);
		mShowHot = (TextView) findViewById(R.id.tv_show_hot);
		mListView = (ListView) findViewById(R.id.lv_show_news);
		lists.add("aaa");
		lists.add("aaa");lists.add("aaa");lists.add("aaa");lists.add("aaa");lists.add("aaa");
		ArrayAdapter<String> mAdapter = new ArrayAdapter<String>(HotSpot.this, R.layout.item_hotspot_news_list,
				R.id.tv_hot_item, lists);
		mListView.setAdapter(mAdapter);
		mListView.setOnItemClickListener(new MyItemListener());
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		getResult();

	}
	
	//2.实时热点
	private List<String> parseNews(String strs){
		String str = null;
		List<String> lists = new ArrayList<String>();
		try {
			JSONObject object = new JSONObject(strs);
			if(object.getInt("error_code")==0){
				JSONArray array = (JSONArray)object.get("result");
				for(int i = 0;i<array.length();i++){
					lists.add(array.getString(i));
					Log.i("aaa", array.getString(i));
				}
				str = object.get("result").toString();
			}else{
				str = object.get("error_code")+":"+object.get("reason");

			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return lists;

	}

	private void getResult() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				String rs = null;
				try {
					HttpClient client = new DefaultHttpClient();
					HttpGet get = new HttpGet("http://op.juhe.cn/onebox/news/words?key="+APPKEY);
					HttpResponse response = client.execute(get);
					if(response.getStatusLine().getStatusCode() == 200){
						HttpEntity entity = response.getEntity();
						rs = EntityUtils.toString(entity, "utf-8");
					}  
				} catch (IOException e) {
					e.printStackTrace();
				} 
				List<String> str = parseNews(rs);
				Message msg = handler.obtainMessage(100, str);
				handler.sendMessage(msg);
			}
		}).start();

	}
	
	
	class MyItemListener implements OnItemClickListener{

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			Intent i = new Intent(HotSpot.this, HotDetails.class);
			i.putExtra("find", position);
			i.putExtra("search",lists.get(position));
			startActivity(i);
			
		}
		
	}
	
	


}
