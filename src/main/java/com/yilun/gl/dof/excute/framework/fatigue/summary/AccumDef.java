package com.yilun.gl.dof.excute.framework.fatigue.summary;

import lombok.Data;

import java.io.Serializable;
import com.dyuproject.protostuff.Tag;

/**
 * 窗口的定义 可调整 当前都是默认值 时间戳单位都是秒
 * 窗口的定义 设定初始时间是 2022年8月1号 对应的时间戳是 1659283200
 */
@Data
public class AccumDef implements Serializable {

    /**
     * 表示对应的数组大小有90个，今天对应的数组大小是 (当前时间戳 - 1659283200)/90
     * eg (1660396711 - 1659283200)/(24*3600)%90 =
     * 说明，针对分钟级，最小单位是5分钟一次
     *
     */
    @Tag(1)
    private Integer dayWindowSize = 60;
    @Tag(2)
    private Integer hourWindowSize = 48;
    @Tag(3)
    private Integer minuteWindowSize = 24;
    @Tag(4)
    private Integer monthWindowSize = 24;
    @Tag(5)
    private Integer weekWindowSize = 10;
    @Tag(6)
    private Boolean keepTotalNum = Boolean.TRUE;
    @Tag(7)
    private Integer yearWindowSize = 6;
}
