package DyrkApp.element;

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
import com.serverless.demo.handler.DUtil;
import com.serverless.demo.handler.S3Bucket;

public class GetAll implements RequestHandler<Map, Object> {

    @Override
    public Object handleRequest(Map input, Context context) {
    	context.getLogger().log("Input: " + input);
    	
    	String parentID = (String) input.get("ID");
    	List<String> BlockedUsers = (List<String>) input.get("blocedUsers");
    	
        
        try {
        	List<Map> unfiltered = getAll(parentID);
        	return filterBlocked(unfiltered, BlockedUsers); 
        }catch(Exception e) {
        	return e;
        }
        
        
    }
    
    public List<Map> getAll(String parentID) throws Exception{
    	
        Table table = DUtil.getTable();
        Index index = table.getIndex("GSI1");
        
    	Get get = new Get();
        if(!get.inTable(parentID)) {
        	throw new Exception("Not in table");
        }
        
        
        Map<String, Object> valueMap = new HashMap<String, Object>();
        
        valueMap.put(":parentID", parentID);
        
        QuerySpec spec = new QuerySpec()
        		.withKeyConditionExpression("parentID = :parentID")
        			.withValueMap(valueMap)
				.withScanIndexForward(false);
        		
       
    
    	ItemCollection<QueryOutcome> queryRet = index.query(spec);        	
    	
    	
    	
    	List<Map> maps = new ArrayList<Map>();
    	
    	for(Item outcome : queryRet) {
    		Map map = outcome.asMap();
        	
    		map.put("date", map.get("sort"));
        	map.remove("sort");
        	
    		maps.add(map);
    	}
    	
    	System.out.println(maps.toString());
    	
    	S3Bucket s3 = new S3Bucket();
    	for(Map m : maps) {
    		String path = "photos/"+ m.get("ID");
    		try {
    			String photo = s3.get(path);
    			m.put("photo", photo);
    		}catch(Exception e) {
    		}
    	}
    	
    	return maps;

       
    }
    
    public List<Map> filterBlocked(List<Map> unfiltered, List<String> BlockedUsers) throws Exception {
    	List<Map> filtered_Maps = new ArrayList<Map>();
    	for(Map<String, Object> map : unfiltered) {
    		if(!map.containsKey("deviceID") || !BlockedUsers.contains( map.get("deviceID") ) ) {
    			filtered_Maps.add(map);
    		}
    	}
    	return filtered_Maps;
    }

}
