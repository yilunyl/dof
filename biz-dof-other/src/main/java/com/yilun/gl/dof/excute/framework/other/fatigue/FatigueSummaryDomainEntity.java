package com.yilun.gl.dof.excute.framework.other.fatigue;

import com.yilun.gl.dof.excute.framework.other.fatigue.summary.AccumSummaryEntity;
import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName: biz-dof FatigueSummaryDomainEntity
 * @Description: com.yilun.gl.dof.excute.framework.fatigue
 * @Author: 逸伦
 * @Date: 2023/2/21 22:37
 * @Version: 1.0
 */
@Data
public class FatigueSummaryDomainEntity implements Serializable {

	private String deviceId;

	private String event;

	private String position;

	private AccumSummaryEntity accumSummary;

	private Long updateTime;

	private Long createTime;
}
