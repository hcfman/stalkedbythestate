package com.stalkedbythestate.sbts.streamer;

// Copyright (c) 2021 Kim Hendrikse

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

@WebFilter(urlPatterns = {"/*"}, description = "Cache Control Filter")
public class CacheControlFilter implements Filter {

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {
		HttpServletResponse resp = (HttpServletResponse) response;
		resp.setHeader("Expires", "Sat, 1 Jan 2000 12:00:00 GMT");
		resp.setHeader("Last-Modified", new Date().toString());
		resp.setHeader("Cache-Control",
				"no-store, no-cache, must-revalidate, max-age=0, post-check=0, pre-check=0");
		resp.setHeader("Pragma", "no-cache");

		chain.doFilter(request, response);
	}

	@Override
	public void destroy() {

	}

}
