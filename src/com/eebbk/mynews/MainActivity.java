package com.eebbk.mynews;


import com.eebbk.mynews.activity.*;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;

@SuppressWarnings("deprecation")
public class MainActivity extends TabActivity {
	private TabHost mTabHost;
	private static final String TAB_SUBSCTIPTION = "订阅";
	private static final String TAB_HOTSPOT = "热点";
	private static final String TAB_FUN = "玩乐";
	private static final String TAB_COMMUNITY = "社区";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		
		mTabHost = getTabHost();
		mTabHost.setFocusable(true);
		
		mTabHost.addTab(createTabSpec(TAB_SUBSCTIPTION, R.drawable.tab_subs_selector, new Intent(this, Subscription.class)));
		mTabHost.addTab(createTabSpec(TAB_HOTSPOT, R.drawable.tab_hot_selector, new Intent(this, HotSpot.class)));
		mTabHost.addTab(createTabSpec(TAB_FUN, R.drawable.tab_fun_selector, new Intent(this, Fun.class)));
		mTabHost.addTab(createTabSpec(TAB_COMMUNITY, R.drawable.tab_com_selector, new Intent(this, Community.class)));
		
	}
	
	public TabSpec createTabSpec(String tag,int icon,Intent intent){
		View view = getLayoutInflater().inflate(R.layout.activity_main_tabwidget_style, null);
		((ImageView) view.findViewById(R.id.iv_tabwidget)).setBackgroundResource(icon);
		((TextView) view.findViewById(R.id.tv_tabwidget)).setText(tag);;
		TabSpec tabspec = mTabHost.newTabSpec(tag).setIndicator(view).setContent(intent);
		return tabspec;
	}
	

	
}
