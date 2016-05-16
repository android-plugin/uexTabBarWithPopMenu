package org.zywx.wbpalmstar.plugin.uextabbarwithpopmenu;

import android.content.Context;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.widget.RelativeLayout;

import org.json.JSONException;
import org.json.JSONObject;
import org.zywx.wbpalmstar.base.BUtility;
import org.zywx.wbpalmstar.engine.DataHelper;
import org.zywx.wbpalmstar.engine.EBrowserView;
import org.zywx.wbpalmstar.engine.universalex.EUExBase;
import org.zywx.wbpalmstar.engine.universalex.EUExUtil;
import org.zywx.wbpalmstar.plugin.uextabbarwithpopmenu.vo.DataItemVO;
import org.zywx.wbpalmstar.plugin.uextabbarwithpopmenu.vo.OpenDataVO;

import java.util.List;

public class EUExTabBarWithPopMenu extends EUExBase {

    private static final String BUNDLE_DATA = "data";
    private static final int MSG_SET_ITEM_CHECKED = 5;

    private TableBarWithPopMenu tabBarView;
    private static final int MSG_OPEN = 1;
    private static final int MSG_CLOSE = 2;
    private static final int DEFAULT_HEIGHT = 60;

    public EUExTabBarWithPopMenu(Context context, EBrowserView eBrowserView) {
        super(context, eBrowserView);
    }

    @Override
    protected boolean clean() {
        return false;
    }


    public void open(String[] params) {
        if (params == null || params.length < 1) {
            errorCallback(0, 0, "error params!");
            return;
        }
        Message msg = new Message();
        msg.obj = this;
        msg.what = MSG_OPEN;
        Bundle bd = new Bundle();
        bd.putStringArray(BUNDLE_DATA, params);
        msg.setData(bd);
        mHandler.sendMessage(msg);
    }

    private void openMsg(String[] params) {
        String json = params[0];
        OpenDataVO dataVO = DataHelper.gson.fromJson(json, OpenDataVO.class);
        if (dataVO == null || dataVO.getTab() == null || dataVO.getPopMenu() == null){
            errorCallback(0, 0, "error params!");
            return;
        }
        if (dataVO.getHeight() <= 0) {
            dataVO.setHeight(EUExUtil.dipToPixels(DEFAULT_HEIGHT));
        }
        List<DataItemVO> tabData = dataVO.getTab().getData();
        setRealPath(tabData);
        List<DataItemVO> popData = dataVO.getPopMenu().getData();
        setRealPath(popData);
        String centerImg = dataVO.getTab().getCenterImg();
        if (!TextUtils.isEmpty(centerImg)){
            dataVO.getTab().setCenterImg(getRealPath(centerImg));
        }
        final RelativeLayout.LayoutParams lparm = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        if (tabBarView == null) {
            tabBarView = new TableBarWithPopMenu(mContext, dataVO, mListener);
            tabBarView.initView();
        }
        if(tabBarView.getAllView().getParent()!=null){
            removeViewFromCurrentWindow(tabBarView.getAllView());
        }
        addViewToCurrentWindow(tabBarView.getAllView(), lparm);
    }

    private void setRealPath(List<DataItemVO> data) {
        if (data == null || data.size() < 1) return;
        for (int i = 0; i < data.size(); i++){
            DataItemVO itemVO = data.get(i);
            itemVO.setIconN(getRealPath(itemVO.getIconN()));
            itemVO.setIconH(getRealPath(itemVO.getIconH()));
        }
    }

    private String getRealPath(String srcPath){
        if (TextUtils.isEmpty(srcPath)) return null;
        String desPath = BUtility.makeRealPath(
                BUtility.makeUrl(mBrwView.getCurrentUrl(), srcPath),
                mBrwView.getCurrentWidget().m_widgetPath,
                mBrwView.getCurrentWidget().m_wgtType);
        return desPath;
    }

    public void close(String[] params) {
        Message msg = new Message();
        msg.obj = this;
        msg.what = MSG_CLOSE;
        Bundle bd = new Bundle();
        bd.putStringArray(BUNDLE_DATA, params);
        msg.setData(bd);
        mHandler.sendMessage(msg);
    }

    private void closeMsg() {
        if (tabBarView != null&&tabBarView.isInitView()) {
            removeViewFromCurrentWindow(tabBarView.getAllView());
        }
    }

    public void setItemChecked(String[] params) {
        if (params == null || params.length < 1) {
            errorCallback(0, 0, "error params!");
            return;
        }
        Message msg = new Message();
        msg.obj = this;
        msg.what = MSG_SET_ITEM_CHECKED;
        Bundle bd = new Bundle();
        bd.putStringArray(BUNDLE_DATA, params);
        msg.setData(bd);
        mHandler.sendMessage(msg);
    }

    private void setItemCheckedMsg(String[] params) {
        String json = params[0];
        int index = 0;
        try {
            JSONObject jsonObject = new JSONObject(json);
            index = Integer.valueOf(jsonObject.getString(JsConst.CALLBACK_INDEX));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (tabBarView == null){
            errorCallback(0,0, "no tab bar!");
            return;
        }
        tabBarView.setItemChecked(index);
    }

    @Override
    public void onHandleMessage(Message message) {
        if(message == null){
            return;
        }
        Bundle bundle=message.getData();
        switch (message.what) {

            case MSG_OPEN:
                openMsg(bundle.getStringArray(BUNDLE_DATA));
                break;
            case MSG_CLOSE:
                closeMsg();
                break;
            case MSG_SET_ITEM_CHECKED:
                setItemCheckedMsg(bundle.getStringArray(BUNDLE_DATA));
                break;
            default:
                super.onHandleMessage(message);
        }
    }

    private void callBackPluginJs(String methodName, String jsonData){
        String js = SCRIPT_HEADER + "if(" + methodName + "){"
                + methodName + "('" + jsonData + "');}";
        onCallback(js);
    }

    private TabPopCallbackListener mListener = new TabPopCallbackListener() {
        @Override
        public void onTabItemClick(int index) {
            tabBarView.setItemChecked(index);
            callbackToHtml(index, JsConst.ON_TAB_ITEM_CLICK);
        }

        @Override
        public void onPopMenuItemClick(int index) {
            callbackToHtml(index, JsConst.ON_POP_MENU_ITEM_CLICK);
        }
    };

    private void callbackToHtml(int index, String methodName) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(JsConst.CALLBACK_INDEX, index);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        callBackPluginJs(methodName, jsonObject.toString());
    }

    public interface TabPopCallbackListener{
        public void onTabItemClick(int index);
        public void onPopMenuItemClick(int index);
    }

}
