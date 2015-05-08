package com.tmf.plugin.uextabbarwithpopmenu;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.zywx.wbpalmstar.base.BUtility;
import org.zywx.wbpalmstar.engine.universalex.EUExBase;
import org.zywx.wbpalmstar.engine.universalex.EUExUtil;

import android.R;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothClass.Service;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class TableBarWithPopMenu implements OnClickListener ,OnItemClickListener{

	private RelativeLayout tabbarWithPopMenu;

	private List<LinearLayout> menuitems = new ArrayList<LinearLayout>();
	private ImageButton mainItem;
	private LinearLayout popCover;

	private GridView popItemGrid;
	
	private int layoutId, mainItemId, popCoverId,popItemGridId;
	private int[] menuItemsId = new int[] { 0, 0, 0, 0 };
	private int[] popMenuItemsId=new int[]{0,0,0,0,0};
	private String[] popMenuItemText=new String[]{"联系人","保存","拍照","打印文件","定位"};
	
	
	private int menuOpenAnimId,menuCloseAnimId;

	private EUExTabbarWithPopMenu callback;
	private Context mContext;

	private boolean isOpenMenu = false;
	private PopAdapter popAdapter;
	private boolean isInit;



	public TableBarWithPopMenu(Context ctx, EUExTabbarWithPopMenu callback) {
		initId();
		this.callback = callback;
		this.mContext = ctx;
		

	}
	
	public boolean isInitView(){
		return isInit ;
	}
	
	public void initView(){
		initId();
		LayoutInflater lif = LayoutInflater.from(mContext);
		tabbarWithPopMenu = (RelativeLayout) lif.inflate(layoutId, null);

		mainItem = (ImageButton) tabbarWithPopMenu.findViewById(mainItemId);
		mainItem.setOnClickListener(this);
		for(int i=0;i<4;i++){
			
			LinearLayout item = (LinearLayout) tabbarWithPopMenu.findViewById(menuItemsId[i]);
			menuitems.add(item);
			item.setOnClickListener(this);
		}
	
		popItemGrid=(GridView) tabbarWithPopMenu.findViewById(popItemGridId);
		ArrayList<PopItemBean> data=new ArrayList<PopItemBean>();
		for(int i=0;i<5;i++){
			PopItemBean itemdata=new PopItemBean();
			itemdata.text=popMenuItemText[i];
			itemdata.textColor=Color.GRAY;
			itemdata.norDrawable=itemdata.highLightDrawable=mContext.getResources().getDrawable(popMenuItemsId[i]);
			data.add(itemdata);
		}
		
		popAdapter=new PopAdapter(mContext, data);
		
		popItemGrid.setAdapter(popAdapter);
		popItemGrid.setOnItemClickListener(this);
		
		popCover = (LinearLayout) tabbarWithPopMenu.findViewById(popCoverId);
		popCover.setOnClickListener(this);
		isInit=true;
		
	}

	private void initId() {
		// TODO Auto-generated method stub
		layoutId = EUExUtil.getResLayoutID("plugin_uextabbarwithpopmenu");
		
		mainItemId = EUExUtil.getResIdID("plugin_tab_itemmenu");
		popCoverId = EUExUtil.getResIdID("plugin_tab_popcover");
		popItemGridId=EUExUtil.getResIdID("plugin_tab_popitems");
		popMenuItemsId[0]=EUExUtil.getResDrawableID("plugin_tabbar_contact");
		popMenuItemsId[1]=EUExUtil.getResDrawableID("plugin_tabbar_save");
		popMenuItemsId[2]=EUExUtil.getResDrawableID("plugin_tabbar_camera");
		popMenuItemsId[3]=EUExUtil.getResDrawableID("plugin_tabbar_print");
		popMenuItemsId[4]=EUExUtil.getResDrawableID("plugin_tabbar_location");
		
		
		menuItemsId[0] = EUExUtil.getResIdID("plugin_tab_item1");
		menuItemsId[1] = EUExUtil.getResIdID("plugin_tab_item2");
		menuItemsId[2] = EUExUtil.getResIdID("plugin_tab_item3");
		menuItemsId[3] = EUExUtil.getResIdID("plugin_tab_item4");
		
		menuOpenAnimId=EUExUtil.getResAnimID("plugin_tabpop_mainmenu_open");
		menuCloseAnimId=EUExUtil.getResAnimID("plugin_tabpop_mainmenu_close");
		

	}

	
	public void setTabItems(List<TabItemBean> items) {
		// TODO Auto-generated method stub
		for (int i = 0; i < 4; i++) {

			TextView tv = (TextView) menuitems.get(i).findViewWithTag("tv");

			// text: “动态”,textColor:, //文本颜色

			String text = items.get(i).text;
			if (null != text && !text.equals("")) {
				tv.setText(text);
			}
			int color = items.get(i).textColor;
			if (color != 0) {
				tv.setTextColor(color);
			}

		}
		 tabbarWithPopMenu.postInvalidate();

	}

	@SuppressLint("NewApi")
	public void setPopItems(List<PopItemBean> items) {
		// TODO Auto-generated method stub
       popAdapter=new PopAdapter(mContext,items);
       popItemGrid.setAdapter(popAdapter);
       tabbarWithPopMenu.postInvalidate();
      
	}

	private void startOpenAnimation() {
       Animation anim=AnimationUtils.loadAnimation(mContext, menuOpenAnimId);
       anim.setFillAfter(true);
       mainItem.startAnimation(anim);
	}

	private void startCloseAnimation(int i) {
		 Animation anim=AnimationUtils.loadAnimation(mContext, menuCloseAnimId);
		 anim.setFillAfter(true);
	     mainItem.startAnimation(anim);
	}

	

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		if (arg0.equals(mainItem)) {
			if (isOpenMenu) {
				closePopMenu(0);
			} else {

				openPopMenu();
			}
		} else if (arg0.equals(menuitems.get(0))) {
			OnTabItemClick(0);
		} else if (arg0.equals(menuitems.get(1))) {
			OnTabItemClick(1);
		} else if (arg0.equals(menuitems.get(2))) {
			OnTabItemClick(2);
		} else if (arg0.equals(menuitems.get(3))) {
			OnTabItemClick(3);
		} else if (arg0.equals(popCover)||arg0.equals(popItemGrid)) {
			closePopMenu(-1);
		}
	}

	private void OnTabItemClick(int i) {

		callback.onTabItemClick(i);

	}

	private void closePopMenu(int i) {
		isOpenMenu = false;
		startCloseAnimation(i);
		popCover.setVisibility(View.GONE);
		popItemGrid.setVisibility(View.GONE);

	}

	private void openPopMenu() {
		isOpenMenu = true;
		
		popCover.setVisibility(View.VISIBLE);
		popItemGrid.setVisibility(View.VISIBLE);
		startOpenAnimation();

	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		callback.onPopMenuItemClick(arg2);
		closePopMenu(arg2 + 1);
		
	}

	public void clean() {
		// TODO Auto-generated method stub
		tabbarWithPopMenu=null;
		isInit=false;
	}

	public View getAllView() {
		// TODO Auto-generated method stub
		return tabbarWithPopMenu;
	}



}