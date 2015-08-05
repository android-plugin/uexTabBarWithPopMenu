package org.zywx.wbpalmstar.plugin.uextabbarwithpopmenu;

import android.R;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.zywx.wbpalmstar.base.BUtility;
import org.zywx.wbpalmstar.engine.universalex.EUExUtil;
import org.zywx.wbpalmstar.plugin.uextabbarwithpopmenu.vo.DataItemVO;
import org.zywx.wbpalmstar.plugin.uextabbarwithpopmenu.vo.OpenDataVO;

import java.util.List;

public class PopAdapter extends BaseAdapter {
	private Context mContext;
	private OpenDataVO data;
	private LayoutInflater inf;
	private int itemId, imageId, textId;
    private List<DataItemVO> mList;

	public PopAdapter(Context ctx, OpenDataVO data) {
		this.mContext = ctx;
		this.data = data;
		inf = LayoutInflater.from(ctx);
		itemId = EUExUtil.getResLayoutID("plugin_tab_gridview_item");
		imageId = EUExUtil.getResIdID("plugin_tab_grid_image");
		textId = EUExUtil.getResIdID("plugin_tab_grid_title");
        this.mList = data.getPopMenu().getData();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mList.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return mList.get(arg0);
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
        final int index = arg0;
		DataItemVO dataItem = mList.get(index);
        Log.i("djf", "title = " + dataItem.getTitle());
        viewHolder.title.setText(dataItem.getTitle());
		viewHolder.title.setTextColor(TabBarUtils.createColorSelector(
                data.getPopMenu().getTextNColor(), data.getPopMenu().getTextHColor(),
                data.getPopMenu().getTextHColor(), data.getPopMenu().getTextNColor()));
		/*
		 * viewHolder.image.setBackgroundDrawable(getImageviewDrawable(
		 * dataItem.norDrawable, dataItem.highLightDrawable));
		 */

        StateListDrawable drawable = TabBarUtils.createDrawableSelector(
                new BitmapDrawable(BUtility.getLocalImg(mContext, dataItem.getIconN())),
                new BitmapDrawable(BUtility.getLocalImg(mContext, dataItem.getIconH())),
                new BitmapDrawable(BUtility.getLocalImg(mContext, dataItem.getIconH())),
                new BitmapDrawable(BUtility.getLocalImg(mContext, dataItem.getIconN()))
        );

		if(Build.VERSION.SDK_INT<16){
			viewHolder.image.setBackgroundDrawable(drawable);
		}else{
			viewHolder.image.setBackground(drawable);
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

		sd.addState(new int[] { R.attr.state_enabled,
				R.attr.state_focused }, press);
		sd.addState(new int[] { R.attr.state_pressed,
				R.attr.state_enabled }, press);
		sd.addState(new int[] { R.attr.state_focused }, press);
		sd.addState(new int[] { R.attr.state_pressed }, press);
		sd.addState(new int[] { R.attr.state_enabled }, normal);
		sd.addState(new int[] {}, normal);

		return sd;
	}

	public void setData(List<PopItemBean> items) {
		// TODO Auto-generated method stub
		//data = items;
	}

}
