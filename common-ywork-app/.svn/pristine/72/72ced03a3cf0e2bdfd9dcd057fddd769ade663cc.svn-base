package me.ywork.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.context.request.NativeWebRequest;


/**
 * 控制器基类
 * @author TangGang  2015年8月11日
 *
 */
public abstract class EventPublisherController {

	@Autowired
	protected ApplicationEventPublisher eventPublisher;
	
	public EventPublisherController() {
		super();
	}

	public void setEventPublisher(ApplicationEventPublisher eventPublisher) {
		this.eventPublisher = eventPublisher;
	}
	
	protected HttpServletRequest getHttpServletRequest(NativeWebRequest request) {
		if (request == null) {
			throw new NullPointerException(
					"getHttpServletRequest - parameter request is null.");
		}
		
		return request.getNativeRequest(HttpServletRequest.class);
	}
	
	/**
	 * 获取当前绑定的域名
	 * @param request   当前的请求对象
	 * @return 域名名称
	 */
	protected String getBindingDomainName(NativeWebRequest request) {
		if (request == null) {
			throw new NullPointerException(
					"getBindingDomainName - parameter request is null.");
		}
		
		HttpServletRequest httpServletRequest = this.getHttpServletRequest(request);
		
		String ctxPath = httpServletRequest.getContextPath();
		String serverAddr = httpServletRequest.getServerName();
		int port = httpServletRequest.getServerPort();
		String reporturl = httpServletRequest.getScheme() + "://" + serverAddr;
		if (port != 80) {
			reporturl += ":" + port;
		}
		reporturl += ctxPath;
		
		return reporturl;
	}
	
	protected static boolean isAjax(HttpServletRequest request) {
		if (request != null
				&& "XMLHttpRequest".equalsIgnoreCase(request
						.getHeader("X-Requested-With")))
			return true;
		return false;
	}
}
