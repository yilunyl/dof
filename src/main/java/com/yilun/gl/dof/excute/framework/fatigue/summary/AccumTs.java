package com.yilun.gl.dof.excute.framework.fatigue.summary;

//import com.dyuproject.protostuff.Tag;
import lombok.Data;

import java.io.Serializable;

@Data
public class AccumTs implements Serializable {
//    @Tag(1)
    private Long time;
//    @Tag(2)
    private Long day;
//    @Tag(3)
    private Long hour;
//    @Tag(4)
    private Long minute;
//    @Tag(5)
    private Long month;
//    @Tag(6)
    private Long week;
//    @Tag(7)
    private Long year;
}
