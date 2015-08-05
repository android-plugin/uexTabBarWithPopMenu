package org.zywx.wbpalmstar.plugin.uextabbarwithpopmenu.vo;

import java.io.Serializable;
import java.util.List;

public class TabDataVO extends DataBaseVO implements Serializable{
    private static final long serialVersionUID = 7693619950721985629L;
    private String centerImg;
    private List<DataItemVO> data;

    public List<DataItemVO> getData() {
        return data;
    }

    public void setData(List<DataItemVO> data) {
        this.data = data;
    }

    public String getCenterImg() {
        return centerImg;
    }

    public void setCenterImg(String centerImg) {
        this.centerImg = centerImg;
    }
}
