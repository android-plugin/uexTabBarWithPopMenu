package org.zywx.wbpalmstar.plugin.uextabbarwithpopmenu;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.zywx.wbpalmstar.base.ACEImageLoader;
import org.zywx.wbpalmstar.engine.universalex.EUExUtil;
import org.zywx.wbpalmstar.plugin.uextabbarwithpopmenu.EUExTabBarWithPopMenu.TabPopCallbackListener;
import org.zywx.wbpalmstar.plugin.uextabbarwithpopmenu.vo.DataItemVO;
import org.zywx.wbpalmstar.plugin.uextabbarwithpopmenu.vo.OpenDataVO;

import java.util.ArrayList;
import java.util.List;

public class TableBarWithPopMenu implements OnClickListener{

    private RelativeLayout tabbarWithPopMenu;

    private List<LinearLayout> menuitems = new ArrayList<LinearLayout>();
    private ImageView mainItem;
    private LinearLayout popCover;

    private GridView popItemGrid;
    private PopMenuView popMenuView;
    private final String POPMENU_TAG = "popMenuView";
    private int layoutId, mainItemId, popCoverId;
    private int[] menuItemsId;
    private int[] menuItemsImgId;
    private int[] menuItemsTextId;
    private int [] indicatorId;


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
        indicatorId = new int[menuSize];
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
            View view =  tabbarWithPopMenu.findViewById(indicatorId[i]);
            if (!TextUtils.isEmpty(mData.getStatusColor())) {
                view.setBackgroundColor(Color.parseColor(mData.getStatusColor()));
            } else {
                view.setBackgroundColor(Color.parseColor("#EA7C24"));
            }


            if (i < list.size()) {
                text.setText(list.get(i).getTitle());
                ACEImageLoader.getInstance().displayImage(img, list.get(i).getIconN());
            }
            menuitems.add(item);
            item.setOnClickListener(this);
        }



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

        menuItemsId[0] = EUExUtil.getResIdID("plugin_tab_item1");
        menuItemsImgId[0] = EUExUtil.getResIdID("plugin_tab_item1_image");
        menuItemsTextId[0] = EUExUtil.getResIdID("plugin_tab_item1_text");
        indicatorId[0] = EUExUtil.getResIdID("plugin_tab_item1_indicator");


        menuItemsId[1] = EUExUtil.getResIdID("plugin_tab_item2");
        menuItemsImgId[1] = EUExUtil.getResIdID("plugin_tab_item2_image");
        menuItemsTextId[1] = EUExUtil.getResIdID("plugin_tab_item2_text");
        indicatorId[1] = EUExUtil.getResIdID("plugin_tab_item2_indicator");


        if (menuSize == 4) {
            menuItemsId[2] = EUExUtil.getResIdID("plugin_tab_item3");
            menuItemsImgId[2] = EUExUtil.getResIdID("plugin_tab_item3_image");
            menuItemsTextId[2] = EUExUtil.getResIdID("plugin_tab_item3_text");
            indicatorId[2] = EUExUtil.getResIdID("plugin_tab_item3_indicator");
            menuItemsId[3] = EUExUtil.getResIdID("plugin_tab_item4");
            menuItemsImgId[3] = EUExUtil.getResIdID("plugin_tab_item4_image");
            menuItemsTextId[3] = EUExUtil.getResIdID("plugin_tab_item4_text");
            indicatorId[3] = EUExUtil.getResIdID("plugin_tab_item4_indicator");
        }
        menuOpenAnimId=EUExUtil.getResAnimID("plugin_tabpop_mainmenu_open");
        menuCloseAnimId=EUExUtil.getResAnimID("plugin_tabpop_mainmenu_close");
    }

    private void startOpenAnimation() {
       Animation anim=AnimationUtils.loadAnimation(mContext, menuOpenAnimId);
       anim.setFillAfter(true);
       mainItem.startAnimation(anim);
    }

    private void startCloseAnimation() {
         Animation anim=AnimationUtils.loadAnimation(mContext, menuCloseAnimId);
         anim.setFillAfter(true);
         mainItem.startAnimation(anim);
    }



    @Override
    public void onClick(View view) {
        if (view.equals(mainItem)) {
            if (isOpenMenu) {
                closePopMenu();
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
        } else if (view.equals(popCover) || POPMENU_TAG.equals(view.getTag())) {
            closePopMenu();
        }
    }

    private void OnTabItemClick(int i) {
        callback.onTabItemClick(i);
    }

    private void closePopMenu() {
        if (!isOpenMenu) {
            return;
        }
        isOpenMenu = false;
        startCloseAnimation();
        popCover.setVisibility(View.GONE);
        popMenuView.setVisibility(View.GONE);
        popMenuView = null;

    }

    private void openPopMenu() {
        if(popMenuView == null) {
            popMenuView = new PopMenuView(mContext, eventInterface);
            popMenuView.setData(mData);
            popMenuView.setTag(POPMENU_TAG);

            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
            layoutParams.height = EUExUtil.dipToPixels((int)bottomDistance);
            layoutParams.leftMargin = 0;
            tabbarWithPopMenu.addView(popMenuView, layoutParams);
        }
        isOpenMenu = true;
        popCover.setVisibility(View.VISIBLE);
        popMenuView.setVisibility(View.VISIBLE);
        startOpenAnimation();

    }


    private PopMenuEventInterface eventInterface = new PopMenuEventInterface() {
        @Override
        public void onMenuItemClick(int page, int index) {
            callback.onPopMenuItemClick(page, index);
            closePopMenu();

        }
    };
    public void clean() {
        tabbarWithPopMenu=null;
        isInit=false;
    }

    public View getAllView() {
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
        View view =  tabbarWithPopMenu.findViewById(indicatorId[index]);
        view.setVisibility(View.GONE);
        if (index < list.size()) {
            ACEImageLoader.getInstance().displayImage(img, list.get(index).getIconN());
        }
    }

    private void setCheckedState(int index){
        List<DataItemVO> list = mData.getTab().getData();
        ImageView img = (ImageView) tabbarWithPopMenu.findViewById(menuItemsImgId[index]);
        TextView text = (TextView) tabbarWithPopMenu.findViewById(menuItemsTextId[index]);
        text.setTextColor(mData.getTab().getTextHColor());
        View view =  tabbarWithPopMenu.findViewById(indicatorId[index]);
        view.setVisibility(View.VISIBLE);
        if (index < list.size()) {
            ACEImageLoader.getInstance().displayImage(img, list.get(index).getIconH());
        }
    }

    public void setBadge(JSONArray indexes) {
        for(int i = 0; i < menuSize; i++) {
            for (int j = 0; j < indexes.length(); j ++) {
                if (i == indexes.optInt(j, -1)) {
                    boolean hasAdded = false;
                    ImageView img = (ImageView) tabbarWithPopMenu.findViewById(menuItemsImgId[i]);
                    //判断是否已添加进去了
                    ViewGroup group = (ViewGroup)img.getParent().getParent();
                    for (int k = 0; k < group.getChildCount(); k ++) {
                        if (group.getChildAt(k) instanceof BadgeView) {
                            hasAdded = true;
                            break;
                        }
                    }
                    if (!hasAdded) {
                        BadgeView badgeView = new BadgeView(mContext);
                        badgeView.setTargetView((ViewGroup) (img.getParent()));
                    }

                }
            }
        }
    }

    public void removeBadge(JSONArray indexes) {
        for(int i = 0; i < menuSize; i++) {
            //如果不传参数，删除所有
            if(indexes == null) {
                ImageView img = (ImageView) tabbarWithPopMenu.findViewById(menuItemsImgId[i]);
                ViewGroup group = (ViewGroup) (img.getParent().getParent());
                int count = group.getChildCount();
                for (int k = 0; k < count; k++) {
                    if (group.getChildAt(k) instanceof BadgeView) {
                        BadgeView badgeView = (BadgeView) group.getChildAt(k);
                        badgeView.remove();
                    }
                }
            } else {
                for (int j = 0; j < indexes.length(); j++) {
                    if (i == indexes.optInt(j, -1)) {
                        ImageView img = (ImageView) tabbarWithPopMenu.findViewById(menuItemsImgId[i]);
                        ViewGroup group = (ViewGroup) (img.getParent().getParent());
                        int count = group.getChildCount();
                        for (int k = 0; k < count; k++) {
                            if (group.getChildAt(k) instanceof BadgeView) {
                                BadgeView badgeView = (BadgeView) group.getChildAt(k);
                                badgeView.remove();
                            }
                        }
                    }
                }
            }
        }
    }
}