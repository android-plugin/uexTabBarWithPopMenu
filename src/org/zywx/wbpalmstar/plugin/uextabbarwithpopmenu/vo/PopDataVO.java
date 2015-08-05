package org.zywx.wbpalmstar.plugin.uextabbarwithpopmenu.vo;

import java.io.Serializable;
import java.util.List;

public class PopDataVO extends DataBaseVO implements Serializable{
    private static final long serialVersionUID = 403040750423407262L;
    private List<DataItemVO> data;

    public List<DataItemVO> getData() {
        return data;
    }

    public void setData(List<DataItemVO> data) {
        this.data = data;
    }
}
