package com.serverless.demo.handler;

import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.joda.time.DateTime;

import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.PutItemOutcome;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

public class AddObject implements RequestHandler<Map<String, Object>, Object>{

	@Override
	public Object handleRequest(Map<String, Object> input, Context context) {
		context.getLogger().log("Input: " + input);
        
        if(!input.containsKey("ID")) {
        	Random r = new Random();
        	String id = ""+r.nextLong();
        	input.put("ID", id);
        }
        

        
        try {
			Map map = (Map) add(input);
			
			map.put("name", input.get("sort"));
		    map.remove("sort");
		    
		    return map;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return e.toString();
		}
        
        
	}
	
	public Map add(Map<String, Object> input) throws Exception{
		Table table = DUtil.getTable();
        
		input.put("parentID", "Object");
		
		input.put("sort", input.get("name"));
	    input.remove("name");
		
        Item item = new Item();
        
        Set<String> keyset = input.keySet();
        
        for(String key : keyset) {
        	item.with(key, input.get(key));
        }
        
        
    	PutItemOutcome outcome = table.putItem(item);
    	return item.asMap();
        	
       
	}

}
