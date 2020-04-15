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
		
		if(!input.containsKey("ID")) {
			Random r = new Random();
			String id = ""+r.nextLong();
			input.put("ID", id);
			
		}
        
		input.put("sort", ""+DateTime.now()); 
        
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
	
	public final String PHOTO_PARAM = "photo";
	
	public Map add(Item item, Map<String,Object> input) throws Exception{
		
		Table table = DUtil.getTable();
		
		String parentID = (String) input.get("parentID");
		   
		Get get = new Get();
		//Will throw error if not in table
		get.get(parentID);
		
		if(input.containsKey(PHOTO_PARAM) && input.get(PHOTO_PARAM) != null) {
			addPhoto((String) input.get(PHOTO_PARAM), (String) input.get("ID"));
		}

		Set<String> keyset = new HashSet<String>(input.keySet());
        keyset.remove(PHOTO_PARAM);
        
        
        for(String key : keyset) {
        	item.with(key, input.get(key));
        }
        

    	
    	table.putItem(item);
    	
    	return item.asMap();
        	
     
	}
	
	
	public void addPhoto(String photoString, String name) throws Exception{
		S3Bucket s3 = new S3Bucket();
		s3.add(photoString, "photos/"+name);
		
		
	}

	
}
