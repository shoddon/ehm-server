package com.sheenline.ehm.server.dao;

import com.sheenline.ehm.server.entity.DeviceInfo;

public interface DeviceInfoMapper {
	
	DeviceInfo selectByDeviceNum(String devicenum);

}