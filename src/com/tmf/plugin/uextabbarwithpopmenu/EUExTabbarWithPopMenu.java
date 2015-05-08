package com.tmf.plugin.uextabbarwithpopmenu;

import java.io.InputStream;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.zywx.wbpalmstar.base.BUtility;
import org.zywx.wbpalmstar.engine.EBrowserView;
import org.zywx.wbpalmstar.engine.universalex.EUExBase;
import org.zywx.wbpalmstar.engine.universalex.EUExCallback;
import org.zywx.wbpalmstar.engine.universalex.EUExUtil;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.RelativeLayout.LayoutParams;

/**
 * @author meifang.tan
 * 
 *         1. open(params) var params = { x:0, y:0, w:100, h:100 }; 2. close()
 *         3. setTabItems(params) //设置tabbar数据项 var params = { tabItems:[ item:
 *         { text: “动态”, textColor:, //文本颜色 }, item: { text: “与我相关”, textColor:,
 *         //文本颜色
 * 
 * 
 *         }, item: { text: “我的空间”, textColor:, //文本颜色 }, item: { text: “玩吧”,
 *         textColor:, //文本颜色 },
 * 
 *         ] }; 4.setPopMenuItems(params) //设置弹出菜单的数据项
 * 
 *         var params = { popMenuItems:[ item:{ text:”说说”， //文本
 *         imgNormal:”res://img_n.png”, //图片 imgHighlight:”res:img_h.png”,
 *         //高亮图片 textColor:#00ff00ff, //文本颜色 }, … //其他数据 ] };
 *         5.onTabItemClick(data) // tabbar项被点击监听事件 data为json格式字符串 { index:0 }
 *         6.onPopMenuItemClick(data) // 弹出菜单项被点击监听时间 { index:0 }
 * 
 */

public class EUExTabbarWithPopMenu extends EUExBase {

	public final static String function_cb_onTabItemClick = "uexTabBarWithPopMenu.onTabItemClick";
	public final static String function_cb_onPopMenuItemClick = "uexTabBarWithPopMenu.onPopMenuItemClick";
	private TableBarWithPopMenu tabBarView;
	private  Object lock=new Object();

	public EUExTabbarWithPopMenu(Context arg0, EBrowserView arg1) {
		super(arg0, arg1);
		// TODO Auto-generated constructor stub
		tabBarView = new TableBarWithPopMenu(arg0, this);
	}

	@Override
	protected boolean clean() {
		// TODO Auto-generated method stub
		synchronized (lock) {
			if (null != tabBarView&&tabBarView.isInitView()) {
				removeViewFromCurrentWindow(tabBarView.getAllView());
				tabBarView.clean();
				
			}
			tabBarView=null;
		}
		
		return true;
	}

	public void open(String[] params) {
		if (params.length < 4) {
			return;
		}
		String inX = params[0];
		String inY = params[1];
		String inW = params[2];
		String inH = params[3];

		int x = 0;
		int y = 0;
		int w = 0;
		int h = 0;
		try {
			x = Integer.parseInt(inX);
			y = Integer.parseInt(inY);
			w = Integer.parseInt(inW);
			h = Integer.parseInt(inH);
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
		
		 final RelativeLayout.LayoutParams lparm = new RelativeLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		lparm.leftMargin = x;
		lparm.topMargin = y;
		synchronized (lock) {
			((Activity)mContext).runOnUiThread(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					if (tabBarView == null) {
						tabBarView = new TableBarWithPopMenu(mContext, EUExTabbarWithPopMenu.this);
						tabBarView.initView();
					}
					if(tabBarView.getAllView().getParent()!=null){
						removeViewFromCurrentWindow(tabBarView.getAllView());
					}
					addViewToCurrentWindow(tabBarView.getAllView(), lparm);
				}
			});
			
		}
		
		
		
		
	}

	public void close(String[] params) {
		synchronized (lock) {
			if (tabBarView != null&&tabBarView.isInitView()) {

				removeViewFromCurrentWindow(tabBarView.getAllView());
				
			}
		}
		
		

	}

	public void setTabItems(String[] params) {
		if (params.length < 1) {
			return;
		}
		String jsonParam = params[0];
		/*
		 * var params = { tabItems:[item: {text: “动态”,textColor:, //文本颜色}, item:
		 * {text: “与我相关”,textColor:, //文本颜色}, item: {text: “我的空间”,textColor:,
		 * //文本颜色}, item: {text: “玩吧”,textColor:, //文本颜色},] };
		 */
		try {
			final ArrayList<TabItemBean> tabBeanList=new ArrayList<TabItemBean>();
			JSONObject tabItems = new JSONObject(jsonParam);
			JSONArray items = tabItems.getJSONArray("tabItems");
			for(int i=0;i<items.length();i++){
				JSONObject item=(JSONObject) items.get(i);
				JSONObject itemdata=item.getJSONObject("item");
				TabItemBean	tabbean=new TabItemBean();
				tabbean.text=itemdata.getString("text");
				tabbean.textColor=BUtility.parseColor(itemdata.getString("textColor"));
				tabBeanList.add(tabbean);
				
			}
			synchronized (lock) {
			
		((Activity)mContext).runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						if (tabBeanList.size() == 4) {
							if (tabBarView == null) {
								tabBarView = new TableBarWithPopMenu(mContext, EUExTabbarWithPopMenu.this);
								tabBarView.initView();
							}
						tabBarView.setTabItems(tabBeanList);
					}
				}
			
			});
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void setPopMenuItems(String[] params) {
		if (params.length < 1) {
			return;
		}
		String jsonParam = params[0];
		/*
		 * var params = { popMenuItems:[ item:{ text:”说说”， //文本
		 * imgNormal:”res://img_n.png”, //图片 imgHighlight:”res:img_h.png”,
		 * //高亮图片 textColor:#00ff00ff, //文本颜色 }, … //其他数据 ]
		 */
		try {
			
			JSONObject popItems = new JSONObject(jsonParam);
			JSONArray items = popItems.getJSONArray("popMenuItems");
			final ArrayList<PopItemBean> popBeanList=new ArrayList<PopItemBean>();
			for(int i=0;i<items.length();i++){
				JSONObject item=(JSONObject) items.get(i);
				JSONObject tabItem=item.getJSONObject("item");
				PopItemBean	PopItembean=new PopItemBean();
				PopItembean.text=tabItem.getString("text");
				PopItembean.textColor=BUtility.parseColor(tabItem.getString("textColor"));
				InputStream in = BUtility.getInputStreamByResPath(mContext,
						(String) tabItem.get("imgNormal"));

				InputStream in1 = BUtility.getInputStreamByResPath(mContext,
						(String) tabItem.get("imgHighlight"));
				PopItembean.norDrawable = new BitmapDrawable(mContext.getResources(),in);
				PopItembean.highLightDrawable = new BitmapDrawable(mContext.getResources(), in1);
				
				popBeanList.add(PopItembean);
				
			}
			synchronized (lock) {
			((Activity)mContext).runOnUiThread(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					if (tabBarView == null) {
						tabBarView = new TableBarWithPopMenu(mContext, EUExTabbarWithPopMenu.this);
						tabBarView.initView();
					}
					tabBarView.setPopItems(popBeanList);
				}
			});
	     }
		
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void onTabItemClick(int i) {
		JSONObject json = new JSONObject();
		try {
			json.put("index", i);
			jsCallback(function_cb_onTabItemClick, 0, EUExCallback.F_C_JSON,
					json.toString());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void onPopMenuItemClick(int i) {
		JSONObject json = new JSONObject();
		try {
			json.put("index", i);
			jsCallback(function_cb_onPopMenuItemClick, 0,
					EUExCallback.F_C_JSON, json.toString());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
}
