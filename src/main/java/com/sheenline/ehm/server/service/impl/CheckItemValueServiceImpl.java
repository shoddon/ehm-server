package com.sheenline.ehm.server.service.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sheenline.ehm.server.dao.CheckItemDicMapper;
import com.sheenline.ehm.server.dao.CheckItemValueMapper;
import com.sheenline.ehm.server.dao.DeviceInfoMapper;
import com.sheenline.ehm.server.entity.CheckItemDic;
import com.sheenline.ehm.server.entity.CheckItemValue;
import com.sheenline.ehm.server.entity.DeviceInfo;
import com.sheenline.ehm.server.exception.SheenlineException;
import com.sheenline.ehm.server.exception.SystemError;
import com.sheenline.ehm.server.service.ICheckItemValueService;

/**
 * Description
 * @author Shoddon
 *
 * 2017年10月13日下午2:20:26
 */
@Service
public class CheckItemValueServiceImpl implements ICheckItemValueService {
	
	private static final Logger logger = LoggerFactory.getLogger(CheckItemValueServiceImpl.class); 
	
	@Autowired
	private CheckItemValueMapper checkItemValueMapper;
	
	@Autowired
	private DeviceInfoMapper deviceInfoMapper;
	
	@Autowired
	private CheckItemDicMapper checkItemDicMapper;
	
	@Override
	public String insert(HttpServletRequest request, HttpServletResponse response) {
		try {
			BufferedReader in=new BufferedReader(new InputStreamReader(request.getInputStream()));
			StringBuilder sb = new StringBuilder();   
	        String line = null;
	        while ((line = in.readLine()) != null) {
	        	sb.append(line);   
	        } 
			addData(sb.toString());
			logger.info("{}","处理完成");
		}catch (IOException e) {
			logger.error("{}",e.getMessage());
			return String.valueOf(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}catch (SheenlineException e) {
			logger.error("{}",e.getError().getDesc());
			return String.valueOf(e.getError().getDesc());
		}
		return String.valueOf(HttpServletResponse.SC_OK);
	}
	
	private void addData(String string) {
		JSONArray jsonarray = JSON.parseArray(string);
		if("".equals(jsonarray) || jsonarray == null){
			throw new SheenlineException(SystemError.BUSINESS_PARAM_ERROR);
		}
		for(Object obj : jsonarray){
			JSONObject js = (JSONObject) obj;
			//通过设备编号确定所属局、配属段、所属库区（地点是否需要？）
			String devicenum = js.getString("devicenum");
			String itemcode = js.getString("itemcode");
			if("".equals(devicenum) || devicenum == null){
				throw new SheenlineException(SystemError.BUSINESS_DEVICENO_ISNULL_ERROR);
			}
			if("".equals(itemcode) || itemcode == null){
				throw new SheenlineException(SystemError.BUSINESS_STATUSCODE_ISNULL_ERROR);
			}
			DeviceInfo deviceInfo = deviceInfoMapper.selectByDeviceNum(devicenum);
			if(deviceInfo == null){
				throw new SheenlineException(SystemError.BUSINESS_DEVICE_NOEXIST_ERROR);
			}
			CheckItemDic checkItemDic = checkItemDicMapper.selectByDevNoAndStatusCode(devicenum,itemcode);
			if(checkItemDic == null){
				throw new SheenlineException(SystemError.BUSINESS_DATA_NOEXIST_ERROR);
			}
			CheckItemValue checkItemValue = new CheckItemValue();
			checkItemValue.setId(UUID.randomUUID().toString().replace("-", ""));
			checkItemValue.setDevicenum(devicenum);
			checkItemValue.setDevicename(deviceInfo.getDevicename());
			checkItemValue.setItemcode(itemcode);
			checkItemValue.setDatavalue(js.getString("itemvalue"));
			checkItemValue.setCreatetime(js.getDate("createtime"));
			int i = checkItemValueMapper.insertSelective(checkItemValue);
			if(i == 0){
				logger.error("{}",SystemError.SYSTEM_DATA_INSERT_ERROR.getDesc());
			}
		}
	}
}