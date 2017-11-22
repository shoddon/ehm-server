package com.sheenline.ehm.server.dao;

import org.apache.ibatis.annotations.Param;

import com.sheenline.ehm.server.entity.HistoryStatusData;

public interface CurrentStatusDataMapper {
	int insertSelective(HistoryStatusData record);

    int updateByPrimaryKeySelective(HistoryStatusData record);

    HistoryStatusData selectByDeviceNumAndStatusCode(@Param(value = "devicenum")String devicenum, 
													@Param(value = "statuscode")String statuscode);
}