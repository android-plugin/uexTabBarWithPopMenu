package org.zywx.wbpalmstar.plugin.uextabbarwithpopmenu.vo;

import java.io.Serializable;

public class PopDataVO extends DataBaseVO implements Serializable{
    private static final long serialVersionUID = 403040750423407262L;
    private float bottomDistance;
    private DataItemVO [][] data;

    public DataItemVO[][] getData() {
        return data;
    }

    public void setData(DataItemVO[][] data) {
        this.data = data;
    }

    public float getBottomDistance() {
        return bottomDistance;
    }

    public void setBottomDistance(float bottomDistance) {
        this.bottomDistance = bottomDistance;
    }
}
