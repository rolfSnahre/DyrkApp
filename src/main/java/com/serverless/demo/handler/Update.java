package com.serverless.demo.handler;

import java.util.Map;

import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

public class Update implements RequestHandler<Map<String, Object>, Object>{

	@Override
	public Object handleRequest(Map<String, Object> input, Context context) {
		context.getLogger().log(input.toString());
		
		Get get = new Get();
//		if(get.inTable(input)) {
//			return "Object not in table";
//		}
        
		try {
			get.get(input);
			Map map = update(input);
			
			map.put("date", map.get("sort"));
        	map.remove("sort");
        	
        	return map;
		}catch(Exception e) {
			return e.toString();
		}
		
	}
	
	public Map update(Map input) throws Exception {
		Item item = new Item().withKeyComponent("ID", input.get("ID"));
		
		Add add = new Add();
		
		Map m = add.add(item, input);
    	
    	return m;
		
	}

}
