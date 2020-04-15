package com.serverless.demo.handler;

import java.util.Map;

import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

public class UpdateObject implements RequestHandler<Map<String, Object>, Object>{

	@Override
	public Object handleRequest(Map<String, Object> input, Context context) {
		
		Get get = new Get();
		AddObject addObject = new AddObject();
		try {
			get.get(input);
			
			Map result = (Map) addObject.add(input);
			
			result.put("name", result.get("sort"));
			result.remove("sort");
			
			return result;
			
		}catch(Exception e) {
			e.printStackTrace();
			return e.toString();
		}
		
	}

}