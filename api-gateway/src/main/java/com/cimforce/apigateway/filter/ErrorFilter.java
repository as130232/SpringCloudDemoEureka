package com.cimforce.apigateway.filter;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.stereotype.Component;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;

/**
 * 處理發生異常的過濾器
 * @author charles.chen
 * @date 2018年3月22日 下午3:04:37
 */
@Component
public class ErrorFilter extends ZuulFilter{
	private static Logger log = LoggerFactory.getLogger(ErrorFilter.class);
	
	@Override
	public Object run() throws ZuulException {
		RequestContext cxt = RequestContext.getCurrentContext();
		Throwable throwable = cxt.getThrowable();
		log.error("This is ErrorFilter: {}", throwable.getCause().getMessage());
		cxt.set("error.status_code", HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		cxt.set("error.exception", throwable.getCause());
//		cxt.setSendZuulResponse(false);
//		cxt.setResponseStatusCode(HttpStatus.BAD_REQUEST.value());	//400
//		cxt.addZuulResponseHeader("content-type","text/html;charset=utf-8");
//		cxt.setResponseBody(throwable.getCause().getMessage());
		return null;
	}

	@Override
	public boolean shouldFilter() {
		return true;
	}

	@Override
	public int filterOrder() {
		return 10;
	}

	@Override
	public String filterType() {
		return FilterConstants.ERROR_TYPE;
	}

}
