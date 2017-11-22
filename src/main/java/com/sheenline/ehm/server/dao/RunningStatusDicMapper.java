package com.sheenline.ehm.server.dao;

import org.apache.ibatis.annotations.Param;

import com.sheenline.ehm.server.entity.RunningStatusDic;

public interface RunningStatusDicMapper {

	RunningStatusDic selectByDevNoAndStatusCode(@Param(value = "devicenum")String devicenum,@Param(value = "statuscode") String statuscode);
	
}