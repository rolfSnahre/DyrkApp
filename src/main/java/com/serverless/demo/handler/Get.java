package com.serverless.demo.handler;

import java.util.HashMap;
import java.util.Map;

import javax.xml.crypto.dsig.spec.ExcC14NParameterSpec;

import com.amazonaws.services.dynamodbv2.document.GetItemOutcome;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.spec.GetItemSpec;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

public class Get implements RequestHandler<Object, Object> {

    @Override
    public Object handleRequest(Object input, Context context) {
        //context.getLogger().log("Input: " + input);
	
    	
		try {
			
			Map map = get(input);
	    	map.put("sort", map.get("date"));
	    	map.remove("date");

	    	if(map.containsKey("imageID")) {
	    		map.put("imageString", getImageString((String) map.get("imageID")));
	       	}
	    	return map;
	    	
		} catch (Exception e) {
			e.printStackTrace();
			return e.getMessage();
		}
    }
    
    public Map get(Object input) throws Exception{
    	
    	String ID = DUtil.inputToID(input);
        
    	Table table = DUtil.getTable();
        
    	GetItemSpec spec = new GetItemSpec().withPrimaryKey("ID" , ID);
    	
    	Item outcome = table.getItem(spec);
    	
    	return outcome.asMap();
        	
    }
    
    
    public String getImageString(String photoID) throws Exception {
    	
    	S3Bucket s3 = new S3Bucket();
    	String imageString = s3.get(photoID);
    	
    	return imageString;
    }
    
    public boolean inTable(Object input) {
    	
    	String ID = DUtil.inputToID(input);
    	
    	Table table = DUtil.getTable();
        
    	GetItemSpec spec = new GetItemSpec().withPrimaryKey("ID" , ID);
    	
    	try{
    		table.getItem(spec);
    		return true;
    	}catch(Exception e) {
    		return false;
    	}
    }
}
