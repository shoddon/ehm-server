package com.sheenline.ehm.server.filter;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author shoddon
 *
 */
public class CheckLoginFilter implements Filter{

	public void destroy() {
		
	}

	public void doFilter(ServletRequest req, ServletResponse res,
			FilterChain chain) throws IOException, ServletException {
		
		HttpServletRequest request = (HttpServletRequest)req;
		HttpServletResponse response = (HttpServletResponse)res;
		HttpSession session = request.getSession();
		Object obj = session.getAttribute("user");
		if(obj == null){
			String uri = request.getRequestURI();
			String uri1 = request.getContextPath()+"/checkitemvalue/checkItemData";
			String uri2 = request.getContextPath()+"/runningstatus/runningStatusData";
			if(uri.equals(uri1) || uri.equals(uri2) || 
			   uri.endsWith(".js") || uri.endsWith(".css") || uri.endsWith(".png") || 
			   uri.endsWith(".jpg") ||  uri.endsWith(".gif") ){
				chain.doFilter(request, response);
				return;
			}
			
			response.setContentType("text/html");
			PrintWriter pw=response.getWriter();
			pw.print("<script>window.parent.location.href='"+request.getContextPath() + "/login/toEcharts'"+";</script>");
			
		} else {
			chain.doFilter(request, response);
		}
	}

	public void init(FilterConfig arg0) throws ServletException {
		
	}
}