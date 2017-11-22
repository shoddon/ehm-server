package com.sheenline.ehm.server.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sheenline.ehm.server.exception.SheenlineException;
import com.sheenline.ehm.server.exception.SystemError;
import com.sheenline.ehm.server.service.IRunningStatusService;

/**
 * Description:运行状态控制
 * @author Shoddon
 *
 * 2017年10月17日上午9:48:04
 */
@Controller
@RequestMapping(value = "runningstatus")
public class RunningStatusController {
	
	private static final Logger logger = LoggerFactory.getLogger(CheckItemValueController.class);
	
	@Autowired
	private IRunningStatusService runningStatusService;
	
	@RequestMapping(value = "runningStatusData")
	@ResponseBody
	public String checkItemData(HttpServletRequest request,HttpServletResponse response) throws IOException {
		String method = request.getMethod();
		try {
			if(!"POST".equals(method)){
				logger.error("{}",SystemError.SYSTEM_REQUEST_METHOD_ERROR.getDesc());
				response.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
				throw new SheenlineException(SystemError.SYSTEM_REQUEST_METHOD_ERROR);
			}
		} catch (SheenlineException e) {
			return String.valueOf(SystemError.SYSTEM_REQUEST_METHOD_ERROR.getDesc());
		}
		return runningStatusService.insert(request,response);
	}
}