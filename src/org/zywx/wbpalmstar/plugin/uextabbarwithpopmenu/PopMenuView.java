package org.zywx.wbpalmstar.plugin.uextabbarwithpopmenu;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.LinearLayout;

import org.zywx.wbpalmstar.engine.universalex.EUExUtil;
import org.zywx.wbpalmstar.plugin.uextabbarwithpopmenu.vo.OpenDataVO;
import org.zywx.wbpalmstar.plugin.uextabbarwithpopmenu.vo.PopDataVO;

/**
 * Created by fred on 16/7/14.
 */
public class PopMenuView extends LinearLayout  {
    private static final String TAG = "PopMenuView";
    private OpenDataVO openDataVo;
    private PopDataVO popMenuData;
    private DotGroup dotGroup;

    private PopMenuEventInterface callback;
    private int currentPageIndex;

    public PopMenuView(Context context, PopMenuEventInterface callback) {
        super(context);
        this.callback = callback;
    }

    public PopMenuView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setData(OpenDataVO data) {
        this.openDataVo = data;
        this.popMenuData = data.getPopMenu();
        render();
    }

    private void render() {
        setOrientation(VERTICAL);
        setGravity(Gravity.CENTER_HORIZONTAL);
        ViewPager viewPager = new ViewPager(getContext());
        viewPager.setAdapter(pageAdapter);
        viewPager.setOnPageChangeListener(pageChangeListner);
        int height = EUExUtil.dipToPixels((int)popMenuData.getBottomDistance());
        //90对应的是布局文件中的dp
        LayoutParams viewPagerParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height - EUExUtil.dipToPixels(90));
        this.addView(viewPager, viewPagerParams);

        if (popMenuData.getData().length > 0) {
            LayoutParams dotsParam = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            dotsParam.topMargin = EUExUtil.dipToPixels(5);

            dotGroup = new DotGroup(getContext(), popMenuData.getData().length, openDataVo.getIndicatorColor(), openDataVo.getIndicatorSelectColor());
            this.addView(dotGroup, dotsParam);
        }
    }

    private PagerAdapter pageAdapter = new PagerAdapter() {
        @Override
        public int getCount() {
            return popMenuData.getData().length;
        }

        @Override
        public boolean isViewFromObject(View view, Object o) {
            return view == o;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            LayoutInflater inflater = (LayoutInflater) container.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            int gvLayoutId = EUExUtil.getResLayoutID("plugin_uextabbarwithpopmenu_gv");
            GridView gv = (GridView) inflater.inflate(gvLayoutId, null);
            PopAdapter popAdapter = new PopAdapter(getContext(), openDataVo, position);
            gv.setAdapter(popAdapter);
            gv.setOnItemClickListener(onItemClickListener);
            container.addView(gv);
            return gv;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View)object);
        }

    };
    private ViewPager.OnPageChangeListener pageChangeListner = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageScrolled(int i, float v, int i1) {

        }

        @Override
        public void onPageSelected(int position) {
            currentPageIndex = position;
            if (dotGroup != null) {
                dotGroup.setChecked(position);
            }
        }

        @Override
        public void onPageScrollStateChanged(int i) {

        }
    };

    private OnItemClickListener onItemClickListener = new OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            callback.onMenuItemClick(currentPageIndex, position);
        }
    };

}
