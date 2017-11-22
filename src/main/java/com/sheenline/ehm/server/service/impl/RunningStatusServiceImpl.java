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
import com.sheenline.ehm.server.dao.CurrentStatusDataMapper;
import com.sheenline.ehm.server.dao.DeviceInfoMapper;
import com.sheenline.ehm.server.dao.HistoryStatusDataMapper;
import com.sheenline.ehm.server.dao.RunningStatusDicMapper;
import com.sheenline.ehm.server.entity.DeviceInfo;
import com.sheenline.ehm.server.entity.HistoryStatusData;
import com.sheenline.ehm.server.entity.RunningStatusDic;
import com.sheenline.ehm.server.exception.SheenlineException;
import com.sheenline.ehm.server.exception.SystemError;
import com.sheenline.ehm.server.service.IRunningStatusService;

/**
 * Description
 * @author Shoddon
 *
 * 2017年10月17日上午10:21:17
 */

@Service
public class RunningStatusServiceImpl implements IRunningStatusService {
	
	private static final Logger logger = LoggerFactory.getLogger(RunningStatusServiceImpl.class);
	
	@Autowired
	private HistoryStatusDataMapper historyStatusDataMapper;
	
	@Autowired
	private CurrentStatusDataMapper currentStatusDataMapper;
	
	@Autowired
	private DeviceInfoMapper deviceInfoMapper;
	
	@Autowired
	private RunningStatusDicMapper runningStatusDicMapper;

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
			String statuscode = js.getString("itemcode");
			if("".equals(devicenum) || devicenum == null){
				throw new SheenlineException(SystemError.BUSINESS_DEVICENO_ISNULL_ERROR);
			}
			if("".equals(statuscode) || statuscode == null){
				throw new SheenlineException(SystemError.BUSINESS_STATUSCODE_ISNULL_ERROR);
			}
			DeviceInfo deviceInfo = deviceInfoMapper.selectByDeviceNum(devicenum);
			if(deviceInfo == null){
				throw new SheenlineException(SystemError.BUSINESS_DEVICE_NOEXIST_ERROR);
			}
			RunningStatusDic runningStatusDic = runningStatusDicMapper.selectByDevNoAndStatusCode(devicenum,statuscode);
			if(runningStatusDic == null){
				throw new SheenlineException(SystemError.BUSINESS_DATA_NOEXIST_ERROR);
			}
			HistoryStatusData historyStatusData = new HistoryStatusData();
			historyStatusData.setId(UUID.randomUUID().toString().replace("-", ""));
			historyStatusData.setDevicenum(devicenum);
			historyStatusData.setStatuscode(statuscode);
			historyStatusData.setStatusvalue(js.getString("itemvalue"));
			historyStatusData.setDatetime(js.getDate("createtime"));
			int i = historyStatusDataMapper.insertSelective(historyStatusData);
			if(i == 0){
				logger.error(" data insertion failed, devicenum : " + js.getString("devicenum"));
			}
			/** 根据devicenum和statusvalue查询当前状态表是否存在,存在则更新数据,不存在则新增数据 */
			HistoryStatusData currentStatusData = currentStatusDataMapper.selectByDeviceNumAndStatusCode(devicenum,statuscode);
			if(currentStatusData != null){
				currentStatusData.setDevicenum(devicenum);
				currentStatusData.setStatuscode(statuscode);
				currentStatusData.setStatusvalue(historyStatusData.getStatusvalue());
				currentStatusData.setDatetime(historyStatusData.getDatetime());
				currentStatusDataMapper.updateByPrimaryKeySelective(currentStatusData);
			}else{
				currentStatusDataMapper.insertSelective(historyStatusData);
			}
		}
	}
}