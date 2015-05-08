package com.tmf.plugin.uextabbarwithpopmenu;

import java.util.ArrayList;
import java.util.List;

import org.zywx.wbpalmstar.base.BUtility;
import org.zywx.wbpalmstar.engine.universalex.EUExUtil;

import android.R;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import android.text.style.BulletSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class PopAdapter extends BaseAdapter {
	private Context mcContext;
	private List<PopItemBean> data;
	private LayoutInflater inf;
	private int itemId, imageId, textId;

	public PopAdapter(Context ctx, List<PopItemBean> data) {
		this.mcContext = ctx;
		this.data = data;
		inf = LayoutInflater.from(ctx);
		itemId = EUExUtil.getResLayoutID("plugin_tab_gridview_item");
		imageId = EUExUtil.getResIdID("plugin_tab_grid_image");
		textId = EUExUtil.getResIdID("plugin_tab_grid_title");

	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return data.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return data.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@SuppressLint("NewApi")
	@Override
	public View getView(int arg0, View convertView, ViewGroup arg2) {
		// TODO Auto-generated method stub
		ViewHolder viewHolder;
		if (convertView == null) {
			convertView = inf.inflate(itemId, null);
			viewHolder = new ViewHolder();
			viewHolder.title = (TextView) convertView.findViewById(textId);
			viewHolder.image = (ImageView) convertView.findViewById(imageId);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		PopItemBean dataItem = data.get(arg0);
		viewHolder.title.setText(dataItem.text);
		viewHolder.title.setTextColor(dataItem.textColor);
		/*
		 * viewHolder.image.setBackgroundDrawable(getImageviewDrawable(
		 * dataItem.norDrawable, dataItem.highLightDrawable));
		 */
		
		if(Build.VERSION.SDK_INT<16){
			viewHolder.image.setBackgroundDrawable(getImageviewDrawable(
					 dataItem.norDrawable, dataItem.highLightDrawable));
		}else{
			viewHolder.image.setBackground(getImageviewDrawable(
					 dataItem.norDrawable, dataItem.highLightDrawable));
		}
		return convertView;

	}

	static class ViewHolder {
		public ImageView image;
		public TextView title;

	}

	private StateListDrawable getImageviewDrawable(Drawable normal,
			Drawable press) {
		StateListDrawable sd = new StateListDrawable();

		sd.addState(new int[] { android.R.attr.state_enabled,
				android.R.attr.state_focused }, press);
		sd.addState(new int[] { android.R.attr.state_pressed,
				android.R.attr.state_enabled }, press);
		sd.addState(new int[] { android.R.attr.state_focused }, press);
		sd.addState(new int[] { android.R.attr.state_pressed }, press);
		sd.addState(new int[] { android.R.attr.state_enabled }, normal);
		sd.addState(new int[] {}, normal);

		return sd;
	}

	public void setData(List<PopItemBean> items) {
		// TODO Auto-generated method stub
		data = items;
	}

}
