package com.yilun.gl.dof.excute.framework.util;

import org.apache.lucene.util.RamUsageEstimator;

public class ObjectSizeUtil {

    /**
     * 返回的单位是Byte
     * @param obj
     * @return
     */
    public static Long bSizeOf(Object obj){
        return RamUsageEstimator.sizeOf(obj);
    }


    /**
     * 返回的单位是MB
     * @param obj
     * @return
     */
    public static Long mBSizeOf(Object obj){

        long size = RamUsageEstimator.sizeOf(obj);
        //如果字节数少于1024，则直接以B为单位，否则先除于1024，后3位因太少无意义
        if (size < 1024) {
            // String.valueOf(size) + "B";
            return 0L;
        } else {
            size = size / 1024;
        }
        //如果原字节数除于1024之后，少于1024，则可以直接以KB作为单位
        //因为还没有到达要使用另一个单位的时候
        //接下去以此类推
        if (size < 1024) {
            //String.valueOf(size) + "KB";
            return 0L;
        } else {
            size = size / 1024;
        }
        return size;
    }
}
