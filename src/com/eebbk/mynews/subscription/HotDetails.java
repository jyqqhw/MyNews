package com.eebbk.mynews.subscription;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import com.eebbk.mynews.R;
import com.eebbk.mynews.activity.HotSpot;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class HotDetails extends Activity {
	
	private 
	private TextView mTv;
	//配置您申请的KEY
	public static final String APPKEY ="c2b2f7b346e807ae1c557b77b7c84f06";
	private Handler handler = new  Handler(){
		public void handleMessage(android.os.Message msg) {
			lists = (List<String>) msg.obj;
			switch (msg.what) {
			case 100:
				Log.i("aaa", "ok");
				break;
			default:
				break;
			}
		};
	};
	
	
	public void onCreate(android.os.Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_hot_details);
		mTv = (TextView) findViewById(R.id.tv_show_details);

		Intent i = getIntent();
		String find = i.getStringExtra("find");

		mTv.setText(text);
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









}
