package com.yilun.gl.dof.excute.framework.other.fatigue.summary;


//import com.dyuproject.protostuff.Tag;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class AccumWithTime implements Serializable {

//    @Tag(1)
    private List<Long> dayAccum;
//    @Tag(2)
    private List<Long> hourAccum;
//    @Tag(3)
    private List<Long> minuteAccum;
//    @Tag(4)
    private List<Long> monthAccum;
//    @Tag(5)
    private List<Long> weekAccum;
//    @Tag(6)
    private Long totalNum;
//    @Tag(7)
    private List<Long> yearAccum;
}
