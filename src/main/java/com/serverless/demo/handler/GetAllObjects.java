package com.serverless.demo.handler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.amazonaws.services.dynamodbv2.document.Index;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.ItemCollection;
import com.amazonaws.services.dynamodbv2.document.QueryOutcome;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.spec.QuerySpec;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

public class GetAllObjects implements RequestHandler<Object, Object>{

	@Override
	public Object handleRequest(Object input, Context context) {
		context.getLogger().log("Input: " + input);

        try {
        	return getAll();
        }catch(Exception e) {
        	return e.getMessage();
        }
	}
	
	public List<Map<String,Object>> getAll() throws Exception{
		
		 Map<String, Object> valueMap = new HashMap<String, Object>();
		
		 valueMap.put(":parentID", "Object");
	        
	        QuerySpec spec = new QuerySpec()
	        		.withKeyConditionExpression("parentID = :parentID")
	        		.withValueMap(valueMap)
	        		.withScanIndexForward(false);
		
        Table table = DUtil.getTable();
        Index index = table.getIndex("GSI1");
        
		ItemCollection<QueryOutcome> queryRet = index.query(spec);        	
    	List<Map<String,Object>> maps = new ArrayList<Map<String,Object>>();
    	
    	for(Item outcome : queryRet) {
    		Map map = outcome.asMap();
        	
    		map.put("name", map.get("sort"));
        	map.remove("sort");
        	
    		maps.add(map);
    	}
    	
    	return maps;
	}
	
}
