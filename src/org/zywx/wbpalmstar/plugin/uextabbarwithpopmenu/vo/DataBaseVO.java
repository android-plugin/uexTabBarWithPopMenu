package org.zywx.wbpalmstar.plugin.uextabbarwithpopmenu.vo;

import org.zywx.wbpalmstar.base.BUtility;

import java.io.Serializable;

public class DataBaseVO implements Serializable{
    private static final long serialVersionUID = -2097869814829590450L;
    private int textSize;
    private String textNColor;
    private String textHColor;
    private String bgColor = "#ffffff";

    public int getTextSize() {
        return textSize;
    }

    public void setTextSize(int textSize) {
        this.textSize = textSize;
    }

    public int getTextNColor() {
        return BUtility.parseColor(textNColor);
    }

    public void setTextNColor(String textNColor) {
        this.textNColor = textNColor;
    }

    public int getTextHColor() {
        return BUtility.parseColor(textHColor);
    }

    public void setTextHColor(String textHColor) {
        this.textHColor = textHColor;
    }

    public int getBgColor() {
        return BUtility.parseColor(bgColor);
    }

    public void setBgColor(String bgColor) {
        this.bgColor = bgColor;
    }
}
