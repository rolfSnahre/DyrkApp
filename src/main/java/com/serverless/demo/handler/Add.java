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

public class Add implements RequestHandler<Map<String, Object>, Object>{

	@Override
	public Object handleRequest(Map<String, Object> input, Context context) {
		
		context.getLogger().log("Input: " + input +"\n");

		Item item = new Item();
        
        Random r = new Random();
        String id = ""+r.nextLong();
        item.withPrimaryKey("ID", id);
        
        try {
        	Map map = add(item, input);
        	
        	map.put("date", map.get("sort"));
        	map.remove("sort");
        	return map;
        }catch(Exception e) {
        	e.printStackTrace();
        	return e.toString();
        }
        
        
	}
	
	public Map add(Item item, Map<String,Object> input) throws Exception{
		
		Table table = DUtil.getTable();
		
		String parentID = (String) input.get("parentID");
		   

		
		Get get = new Get();
		//Will throw error if not in table
		get.get(parentID);
		
        Set<String> keyset = new HashSet<String>(input.keySet());
        keyset.remove("ID");
        keyset.remove("imageString");
        
        
        for(String key : keyset) {
        	item.with(key, input.get(key));
        }
        
        item.with("sort", ""+DateTime.now()); 

        if(input.containsKey("imageString")) {
        	item.with("imageID", addPhoto((String)input.get("imageString"), (String) input.get("ID")));
        }
    	
    	table.putItem(item);
    	
    	return item.asMap();
        	
     
	}
	
	
	public String addPhoto(String imageString, String ID) throws Exception{
		S3Bucket s3 = new S3Bucket();
		String photoID = s3.add(imageString, ID);
		
		return photoID;
		
	}

	
}
