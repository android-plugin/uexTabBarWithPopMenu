package org.zywx.wbpalmstar.plugin.uextabbarwithpopmenu;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.zywx.wbpalmstar.base.ACEImageLoader;
import org.zywx.wbpalmstar.engine.universalex.EUExUtil;
import org.zywx.wbpalmstar.plugin.uextabbarwithpopmenu.EUExTabBarWithPopMenu.TabPopCallbackListener;
import org.zywx.wbpalmstar.plugin.uextabbarwithpopmenu.vo.DataItemVO;
import org.zywx.wbpalmstar.plugin.uextabbarwithpopmenu.vo.OpenDataVO;

import java.util.ArrayList;
import java.util.List;

public class TableBarWithPopMenu implements OnClickListener ,OnItemClickListener{

    private RelativeLayout tabbarWithPopMenu;

    private List<LinearLayout> menuitems = new ArrayList<LinearLayout>();
    private ImageView mainItem;
    private LinearLayout popCover;

    private GridView popItemGrid;
    
    private int layoutId, mainItemId, popCoverId,popItemGridId;
    private int[] menuItemsId;
    private int[] menuItemsImgId;
    private int[] menuItemsTextId;


    private int menuOpenAnimId,menuCloseAnimId;

    private TabPopCallbackListener callback;
    private Context mContext;

    private boolean isOpenMenu = false;
    private PopAdapter popAdapter;
    private boolean isInit;
    private OpenDataVO mData;
    private int menuSize = 4; //底部menu默认是 4 个
    private float bottomDistance = EUExUtil.dipToPixels(300);


    public TableBarWithPopMenu(Context ctx, OpenDataVO data, TabPopCallbackListener callback) {
        menuSize = data.getTab().getData().size();
        if (menuSize != 2) {
            menuSize = 4;
        }
        bottomDistance = data.getPopMenu().getBottomDistance();
        menuItemsId = new int[menuSize];
        menuItemsImgId = new int[menuSize];
        menuItemsTextId = new int[menuSize];
        initId();
        this.callback = callback;
        this.mContext = ctx;
        this.mData = data;

    }
    
    public boolean isInitView(){
        return isInit ;
    }
    
    public void initView(){
        initId();
        LayoutInflater lif = LayoutInflater.from(mContext);
        tabbarWithPopMenu = (RelativeLayout) lif.inflate(layoutId, null);

        LinearLayout bgContent = (LinearLayout) tabbarWithPopMenu.findViewById(
            EUExUtil.getResIdID("plugin_tab_tabbar"));
        bgContent.getLayoutParams().width = mData.getWidth();
        bgContent.getLayoutParams().height = mData.getHeight();
        bgContent.setBackgroundColor(mData.getTab().getBgColor());

        mainItem = (ImageView) tabbarWithPopMenu.findViewById(mainItemId);
        mainItem.getLayoutParams().height = mData.getHeight();
        mainItem.getLayoutParams().width = mData.getHeight();
        ACEImageLoader.getInstance().displayImage(mainItem, mData.getTab().getCenterImg());
        mainItem.setOnClickListener(this);
        for(int i = 0; i < menuSize; i++){
            LinearLayout item = (LinearLayout) tabbarWithPopMenu.findViewById(menuItemsId[i]);
            List<DataItemVO> list = mData.getTab().getData();
            ImageView img = (ImageView) tabbarWithPopMenu.findViewById(menuItemsImgId[i]);
            img.getLayoutParams().height = 2 * mData.getHeight() / 5;
            img.getLayoutParams().width = 2 * mData.getHeight() / 5;
            TextView text = (TextView) tabbarWithPopMenu.findViewById(menuItemsTextId[i]);
            text.setTextSize(mData.getTab().getTextSize());
            text.setTextColor(mData.getTab().getTextNColor());
            if (i < list.size()) {
                text.setText(list.get(i).getTitle());
                ACEImageLoader.getInstance().displayImage(img, list.get(i).getIconN());
            }
            menuitems.add(item);
            item.setOnClickListener(this);
        }
        int gvLayoutId = EUExUtil.getResLayoutID("plugin_uextabbarwithpopmenu_gv");
        popItemGrid = (GridView) lif.inflate(gvLayoutId, null);
        popItemGrid.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_UP:
                        closePopMenu(-1);
                }
                return false;
            }
        });

        popAdapter = new PopAdapter(mContext, mData);
        popItemGrid.setAdapter(popAdapter);
        popItemGrid.setOnItemClickListener(this);

        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
        layoutParams.height = EUExUtil.dipToPixels((int)bottomDistance);
        layoutParams.leftMargin = 0;
        tabbarWithPopMenu.addView(popItemGrid, layoutParams);

        popCover = (LinearLayout) tabbarWithPopMenu.findViewById(popCoverId);
        popCover.setBackgroundColor(mData.getPopMenu().getBgColor());
        popCover.setOnClickListener(this);
        isInit=true;
        
    }

    private void initId() {
        if (menuSize == 2) {
            layoutId = EUExUtil.getResLayoutID("plugin_uextabbarwithpopmenu_2");
        } else {
            layoutId = EUExUtil.getResLayoutID("plugin_uextabbarwithpopmenu");
        }
        mainItemId = EUExUtil.getResIdID("plugin_tab_itemmenu");
        popCoverId = EUExUtil.getResIdID("plugin_tab_popcover");
        popItemGridId=EUExUtil.getResIdID("plugin_tab_popitems");

        menuItemsId[0] = EUExUtil.getResIdID("plugin_tab_item1");
        menuItemsImgId[0] = EUExUtil.getResIdID("plugin_tab_item1_image");
        menuItemsTextId[0] = EUExUtil.getResIdID("plugin_tab_item1_text");

        menuItemsId[1] = EUExUtil.getResIdID("plugin_tab_item2");
        menuItemsImgId[1] = EUExUtil.getResIdID("plugin_tab_item2_image");
        menuItemsTextId[1] = EUExUtil.getResIdID("plugin_tab_item2_text");

        if (menuSize == 4) {
            menuItemsId[2] = EUExUtil.getResIdID("plugin_tab_item3");
            menuItemsImgId[2] = EUExUtil.getResIdID("plugin_tab_item3_image");
            menuItemsTextId[2] = EUExUtil.getResIdID("plugin_tab_item3_text");
            menuItemsId[3] = EUExUtil.getResIdID("plugin_tab_item4");
            menuItemsImgId[3] = EUExUtil.getResIdID("plugin_tab_item4_image");
            menuItemsTextId[3] = EUExUtil.getResIdID("plugin_tab_item4_text");
        }
        menuOpenAnimId=EUExUtil.getResAnimID("plugin_tabpop_mainmenu_open");
        menuCloseAnimId=EUExUtil.getResAnimID("plugin_tabpop_mainmenu_close");
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
    public void onClick(View view) {
        if (view.equals(mainItem)) {
            if (isOpenMenu) {
                closePopMenu(0);
            } else {
                openPopMenu();
            }
        } else if (view.equals(menuitems.get(0))) {
            OnTabItemClick(0);
        } else if (view.equals(menuitems.get(1))) {
            OnTabItemClick(1);
        } else if (menuItemsId.length > 2 && view.equals(menuitems.get(2))) {
            OnTabItemClick(2);
        } else if (menuItemsId.length > 2 && view.equals(menuitems.get(3))) {
            OnTabItemClick(3);
        } else if (view.equals(popCover) || view.equals(popItemGrid)) {
            closePopMenu(-1);
        }
    }

    private void OnTabItemClick(int i) {
        callback.onTabItemClick(i);
    }

    private void closePopMenu(int i) {
        if (!isOpenMenu) {
            return;
        }
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
        callback.onPopMenuItemClick(arg2);
        closePopMenu(arg2 + 1);
        
    }

    public void clean() {
        tabbarWithPopMenu=null;
        isInit=false;
    }

    public View getAllView() {
        // TODO Auto-generated method stub
        return tabbarWithPopMenu;
    }

    public void setItemChecked(int index){
        if (isInit){
            for (int i = 0; i < menuSize; i++){
                if (i == index){
                    setCheckedState(i);
                }else {
                    setNormalState(i);
                }
            }
        }
    }

    private void setNormalState(int index){
        List<DataItemVO> list = mData.getTab().getData();
        ImageView img = (ImageView) tabbarWithPopMenu.findViewById(menuItemsImgId[index]);
        TextView text = (TextView) tabbarWithPopMenu.findViewById(menuItemsTextId[index]);
        text.setTextColor(mData.getTab().getTextNColor());
        if (index < list.size()) {
            ACEImageLoader.getInstance().displayImage(img, list.get(index).getIconN());
        }
    }

    private void setCheckedState(int index){
        List<DataItemVO> list = mData.getTab().getData();
        ImageView img = (ImageView) tabbarWithPopMenu.findViewById(menuItemsImgId[index]);
        TextView text = (TextView) tabbarWithPopMenu.findViewById(menuItemsTextId[index]);
        text.setTextColor(mData.getTab().getTextHColor());
        if (index < list.size()) {
            ACEImageLoader.getInstance().displayImage(img, list.get(index).getIconH());
        }
    }
}