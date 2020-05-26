package DyrkApp.element;

import java.util.HashMap;
import java.util.Map;

import javax.xml.crypto.dsig.spec.ExcC14NParameterSpec;

import com.amazonaws.services.dynamodbv2.document.GetItemOutcome;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.spec.GetItemSpec;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.serverless.demo.handler.DUtil;
import com.serverless.demo.handler.S3Bucket;

public class Get implements RequestHandler<Object, Object> {

    @Override
    public Object handleRequest(Object input, Context context) {
        //context.getLogger().log("Input: " + input);
	
    	
		try {
			
			Map map = get(input);
	    	map.put("date", map.get("sort"));
	    	map.remove("sort");
	    	
	    	try{
	    		map.put("photo", getPhoto((String) map.get("ID")));
	    		System.out.println("S3 success");
	    	}catch(Exception e) {
	    		System.out.println("S3 error");
	    	}
	    	
	    	return map;
	    	
		} catch (Exception e) {
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
    
    
    public String getPhoto(String ID) throws Exception {
    	
    	S3Bucket s3 = new S3Bucket();
    	String photoString = s3.get("photos/"+ID);
    	
    	return photoString;
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
