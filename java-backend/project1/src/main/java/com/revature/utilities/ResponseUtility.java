package com.revature.utilities;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ResponseUtility {
	
	private static ObjectMapper om = new ObjectMapper();
	
	public static void writeJSON(HttpServletResponse resp, Object body) throws IOException {
		PrintWriter writer = resp.getWriter();
		
		writer.println(om.writeValueAsString(body));
	}
}
