package com.serverless.demo.handler;

import java.util.Map;

import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.spec.GetItemSpec;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

public class GetObject implements RequestHandler<Object, Object>{

	@Override
	public Object handleRequest(Object input, Context context) {
		context.getLogger().log("Input: " + input);

        Table table = DUtil.getTable();
        
        String parentID;
        
		if (input instanceof Map) {
			parentID = (String) ((Map) input).get("ID");
		}else {
			parentID = (String) input;
		}    	
    	
        
        try {
        	GetItemSpec spec = new GetItemSpec().withPrimaryKey("ID" , parentID);
        	
        	Item outcome = table.getItem(spec);
        	
        	Map map = outcome.asMap();
        	map.put("name", map.get("sort"));
        	map.remove("sort");
        	
        	return map;
        	
        }catch(Exception e) {
        	context.getLogger().log(e.getMessage());
        	return "Error";
        }
    }
}
