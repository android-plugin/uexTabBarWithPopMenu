package org.zywx.wbpalmstar.plugin.uextabbarwithpopmenu.vo;

import java.io.Serializable;

public class DataItemVO implements Serializable{
    private static final long serialVersionUID = -5999424197232565566L;
    private String title;
    private String iconN;
    private String iconH;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIconN() {
        return iconN;
    }

    public void setIconN(String iconN) {
        this.iconN = iconN;
    }

    public String getIconH() {
        return iconH;
    }

    public void setIconH(String iconH) {
        this.iconH = iconH;
    }
}
