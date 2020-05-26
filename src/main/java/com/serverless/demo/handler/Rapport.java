package com.serverless.demo.handler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

public class Rapport implements RequestHandler<Map, Object>{
	String PAS = "LystgårdenPlanterOgPersille";
	@Override
	public Object handleRequest(Map input, Context context) {
		
		String content = (String) input.get("content");
		
		String deviceID = (String) input.get("deviceID");
		
		List<Map> posts = DUtil.getAllWithDeviceID(deviceID);
		
		Map response = new HashMap();
		response.put("content", content);
		response.put("posts", posts);
		
		return response;
		
	}
	
}
