package org.zywx.wbpalmstar.plugin.uextabbarwithpopmenu.vo;

import java.io.Serializable;

public class OpenDataVO implements Serializable{
    private static final long serialVersionUID = 8270321726298949867L;
    private double left = 0;
    private double top = 0;
    private double width = -1;
    private double height = -1;
    private TabDataVO tab;
    private PopDataVO popMenu;
    public int getLeft() {
        return (int)left;
    }

    public void setLeft(double left) {
        this.left = left;
    }

    public int getTop() {
        return (int)top;
    }

    public void setTop(double top) {
        this.top = top;
    }

    public int getWidth() {
        return (int)width;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public int getHeight() {
        return (int)height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public TabDataVO getTab() {
        return tab;
    }

    public void setTab(TabDataVO tab) {
        this.tab = tab;
    }

    public PopDataVO getPopMenu() {
        return popMenu;
    }

    public void setPopMenu(PopDataVO popMenu) {
        this.popMenu = popMenu;
    }
}
