package com.eebbk.mynews.subscription;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.eebbk.mynews.R;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;

public class HotDetails extends Activity {

	private Map<String,String> mMaps = new HashMap<String,String>();
	private TextView mTv;
	//配置您申请的KEY
	public static final String APPKEY ="c2b2f7b346e807ae1c557b77b7c84f06";
	private String mSearchKey;
	
	// 首先在您的Activity中添加如下成员变量
		final UMSocialService mController = UMServiceFactory.getUMSocialService("com.umeng.share");
		private Button mBtnShare;
	

	private Handler handler = new  Handler(){
		public void handleMessage(android.os.Message msg) {
			mMaps = (Map<String, String>) msg.obj;
			switch (msg.what) {
			case 100:
				StringBuffer sb = new StringBuffer();
				sb.append(mSearchKey+"\n"+mMaps.get("pdata")+"\n"+mMaps.get("src")+"\n"+
						mMaps.get("img")+"\n"+
						mMaps.get("url")+"\n"+
						mMaps.get("content"));
				mTv.setText(sb.toString());
				Log.i("aaa", "ok");
				break;
			default:
				break;
			}
		};
	};


	public void onCreate(android.os.Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_hot_details);
		mTv = (TextView) findViewById(R.id.tv_show_details);

		Intent i = getIntent();
		int find = i.getIntExtra("find",-1);
		mSearchKey = i.getStringExtra("search");
		mTv.setText("aaa:"+find+""+mSearchKey);
		
		initShare();
		
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		getResult();

	}


	//2.实时查询
	private Map parseDetails(String strs){
		String str = null;
		Map<String,String> map = new HashMap<String,String>();
		try {
			JSONObject object = new JSONObject(strs);
			if(object.getInt("error_code")==0){
				JSONObject infos = ((JSONArray)object.get("result")).getJSONObject(0);
				map.put("content", infos.getString("content"));
				map.put("img", infos.getString("img"));
				map.put("pdata", infos.getString("pdata"));
				map.put("src", infos.getString("src"));
				map.put("url", infos.getString("url"));

				str = ((JSONArray)object.get("result")).getJSONObject(0).toString();
			}else{
				str = object.get("error_code")+":"+object.get("reason");

			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return map;

	}



	private void getResult() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				String rs = null;
				try {
					HttpClient client = new DefaultHttpClient();
					HttpGet get = new HttpGet("http://op.juhe.cn/onebox/news/query?key="+APPKEY+"&"
							+"q="+mSearchKey);
					HttpResponse response = client.execute(get);
					if(response.getStatusLine().getStatusCode() == 200){
						HttpEntity entity = response.getEntity();
						rs = EntityUtils.toString(entity, "utf-8");
					}  
				} catch (IOException e) {
					e.printStackTrace();
				} 
				Map<String,String> map = parseDetails(rs);
				Message msg = handler.obtainMessage(100, map);
				handler.sendMessage(msg);
			}
		}).start();

	}
	
	
	private void initShare(){
		mBtnShare = (Button) findViewById(R.id.btn_share);

		// 首先在您的Activity中添加如下成员变量
		final UMSocialService mController = UMServiceFactory.getUMSocialService("com.umeng.share");
		// 设置分享内容
		mController.setShareContent("This is a share function test!");
		// 设置分享图片, 参数2为图片的url地址
		//		mController.setShareMedia(new UMImage(this, 
		//		                                      "http://www.umeng.com/images/pic/banner_module_social.png"));
		// 设置分享图片，参数2为本地图片的资源引用
		//		mController.setShareMedia(new UMImage(this, R.raw.genius));
		// 设置分享图片，参数2为本地图片的路径(绝对路径)
		//mController.setShareMedia(new UMImage(getActivity(), 
		//		                                BitmapFactory.decodeFile("/mnt/sdcard/icon.png")));

		// 设置分享音乐
		//UMusic uMusic = new UMusic("http://sns.whalecloud.com/test_music.mp3");
		//uMusic.setAuthor("GuGu");
		//uMusic.setTitle("天籁之音");
		// 设置音乐缩略图
		//uMusic.setThumb("http://www.umeng.com/images/pic/banner_module_social.png");
		//mController.setShareMedia(uMusic);

		// 设置分享视频
		//UMVideo umVideo = new UMVideo(
		//		          "http://v.youku.com/v_show/id_XNTE5ODAwMDM2.html?f=19001023");
		// 设置视频缩略图
		//umVideo.setThumb("http://www.umeng.com/images/pic/banner_module_social.png");
		//umVideo.setTitle("友盟社会化分享!");
		//mController.setShareMedia(umVideo);

		mController.getConfig().removePlatform( SHARE_MEDIA.RENREN, SHARE_MEDIA.DOUBAN);
		mBtnShare.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// 是否只有已登录用户才能打开分享选择页
				mController.openShare(HotDetails.this, false);
			}
		});
		
	}





}
