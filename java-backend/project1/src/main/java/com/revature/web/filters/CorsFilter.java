package com.revature.web.filters;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

public class CorsFilter implements Filter {

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		
		if (!(response instanceof HttpServletResponse)) {
			chain.doFilter(request, response);
			return;
		}
		
		// Cast the response as an HttpServletResponse
		// Which is important, because we are going to set
		// headers, which is specific to HTTP
		HttpServletResponse res = (HttpServletResponse) response;
		
		res.setHeader("Access-Control-Allow-Origin", "http://localhost:4200"); // Allow all origins
		// Allow specific HTTP Verbs
		res.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD");
		
		// Allow specific HTTP Headers (there's a fair few)
		res.setHeader("Access-Control-Allow-Headers", "Origin, Accept, X-Requested-With, "
				+ "Content-Type, Access-Control-Request-Method, Access-Control-Request-Headers");
		
		// Credentials are allowed
		res.setHeader("Access-Control-Allow-Credentials", "true");
		
		// Continue the filter chain
		chain.doFilter(request, response);
	}

}
