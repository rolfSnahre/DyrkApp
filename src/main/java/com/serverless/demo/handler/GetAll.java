package com.serverless.demo.handler;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.amazonaws.services.dynamodbv2.document.Index;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.ItemCollection;
import com.amazonaws.services.dynamodbv2.document.QueryOutcome;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.spec.QuerySpec;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

public class GetAll implements RequestHandler<Object, Object> {

    @Override
    public Object handleRequest(Object input, Context context) {
    	context.getLogger().log("Input: " + input);
    	

        String parentID;
        
        if (input instanceof Map) {
        	parentID = (String) ((Map) input).get("ID");
        }else {
        	parentID = (String) input;
        } 
        
        
        return getAll(parentID);
        
    }
    
    public Object getAll(String parentID) {
    	
        Table table = DUtil.getTable();
        Index index = table.getIndex("GSI1");
        
    	Get get = new Get();
        if(!get.inTable(parentID)) {
        	return "Error";
        }
        
        Map<String, Object> valueMap = new HashMap<String, Object>();
        
        valueMap.put(":parentID", parentID);
        
        QuerySpec spec = new QuerySpec()
        		.withKeyConditionExpression("parentID = :parentID")
        			.withValueMap(valueMap)
				.withScanIndexForward(false);
        		
        
        try {
        	ItemCollection<QueryOutcome> queryRet = index.query(spec);        	
        	List<Map> maps = new ArrayList<Map>();
        	
        	for(Item outcome : queryRet) {
        		Map map = outcome.asMap();
            	
        		map.put("date", map.get("sort"));
            	map.remove("sort");
            	
        		maps.add(map);
        	}
        	
        	S3Bucket s3 = new S3Bucket();
        	for(Map m : maps) {
        		try {
        			String photo = s3.get("photos/"+ m.get("ID"));
        			m.put("photo", photo);
        		}catch(Exception e) {
        			System.out.println("No photo: " + m.get("ID"));
        		}
        	}
        	
        	return maps;

        }catch(Exception e) {
        	return e.getMessage();
        }
    }

}
