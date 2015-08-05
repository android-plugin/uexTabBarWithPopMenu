package org.zywx.wbpalmstar.plugin.uextabbarwithpopmenu;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
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
    private int[] menuItemsId = new int[] { 0, 0, 0, 0 };
    private int[] menuItemsImgId = new int[] { 0, 0, 0, 0 };
    private int[] menuItemsTextId = new int[] { 0, 0, 0, 0 };
    private String[] popMenuItemText=new String[]{"联系人","保存","拍照","打印文件","定位"};
    
    
    private int menuOpenAnimId,menuCloseAnimId;

    private TabPopCallbackListener callback;
    private Context mContext;

    private boolean isOpenMenu = false;
    private PopAdapter popAdapter;
    private boolean isInit;
    private OpenDataVO mData;



    public TableBarWithPopMenu(Context ctx, OpenDataVO data, TabPopCallbackListener callback) {
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
        for(int i = 0; i < 4;i++){
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
        popItemGrid=(GridView) tabbarWithPopMenu.findViewById(popItemGridId);

        popAdapter=new PopAdapter(mContext, mData);
        
        popItemGrid.setAdapter(popAdapter);
        popItemGrid.setOnItemClickListener(this);
        
        popCover = (LinearLayout) tabbarWithPopMenu.findViewById(popCoverId);
        popCover.setBackgroundColor(mData.getPopMenu().getBgColor());
        popCover.setOnClickListener(this);
        isInit=true;
        
    }

    private void initId() {
        // TODO Auto-generated method stub
        layoutId = EUExUtil.getResLayoutID("plugin_uextabbarwithpopmenu");
        
        mainItemId = EUExUtil.getResIdID("plugin_tab_itemmenu");
        popCoverId = EUExUtil.getResIdID("plugin_tab_popcover");
        popItemGridId=EUExUtil.getResIdID("plugin_tab_popitems");

        menuItemsId[0] = EUExUtil.getResIdID("plugin_tab_item1");
        menuItemsId[1] = EUExUtil.getResIdID("plugin_tab_item2");
        menuItemsId[2] = EUExUtil.getResIdID("plugin_tab_item3");
        menuItemsId[3] = EUExUtil.getResIdID("plugin_tab_item4");

        menuItemsImgId[0] = EUExUtil.getResIdID("plugin_tab_item1_image");
        menuItemsImgId[1] = EUExUtil.getResIdID("plugin_tab_item2_image");
        menuItemsImgId[2] = EUExUtil.getResIdID("plugin_tab_item3_image");
        menuItemsImgId[3] = EUExUtil.getResIdID("plugin_tab_item4_image");

        menuItemsTextId[0] = EUExUtil.getResIdID("plugin_tab_item1_text");
        menuItemsTextId[1] = EUExUtil.getResIdID("plugin_tab_item2_text");
        menuItemsTextId[2] = EUExUtil.getResIdID("plugin_tab_item3_text");
        menuItemsTextId[3] = EUExUtil.getResIdID("plugin_tab_item4_text");

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
//       popAdapter=new PopAdapter(mContext,items);
//       popItemGrid.setAdapter(popAdapter);
//       tabbarWithPopMenu.postInvalidate();
      
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

    public void setItemChecked(int index){
        if (isInit){
            for (int i = 0; i < 4; i++){
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