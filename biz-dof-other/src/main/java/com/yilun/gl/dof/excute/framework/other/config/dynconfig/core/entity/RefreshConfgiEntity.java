package com.yilun.gl.dof.excute.framework.other.config.dynconfig.core.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

/**
 * @Auther: gl
 * @Date: 2020/10/27 14:46
 * @Description:
 */
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class RefreshConfgiEntity implements Serializable {

    private byte[] data;

    private String wholePath;

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public String getWholePath() {
        return wholePath;
    }

    public void setWholePath(String wholePath) {
        this.wholePath = wholePath;
    }
}
