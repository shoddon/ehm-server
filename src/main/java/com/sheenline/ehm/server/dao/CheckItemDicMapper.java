package com.sheenline.ehm.server.dao;

import org.apache.ibatis.annotations.Param;

import com.sheenline.ehm.server.entity.CheckItemDic;

public interface CheckItemDicMapper {

	CheckItemDic selectByDevNoAndStatusCode(@Param(value = "devicenum")String devicenum,@Param(value = "itemcode") String itemcode);
	
}