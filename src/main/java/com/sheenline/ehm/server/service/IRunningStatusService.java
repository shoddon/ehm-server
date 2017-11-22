package com.sheenline.ehm.server.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface IRunningStatusService {

	String insert(HttpServletRequest request, HttpServletResponse response);

}