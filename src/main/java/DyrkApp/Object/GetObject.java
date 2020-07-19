package DyrkApp.Object;

import java.util.Map;

import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.spec.GetItemSpec;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.serverless.demo.handler.DUtil;

public class GetObject implements RequestHandler<Object, Object>{

	@Override
	public Object handleRequest(Object input, Context context) {
		context.getLogger().log("Input: " + input);

        Table table = DUtil.getTable();
        
        String ID;
        
		if (input instanceof Map) {
			ID = (String) ((Map) input).get("ID");
		}else {
			ID = (String) input;
		}    	
    	
        
        try {
        	GetItemSpec spec = new GetItemSpec().withPrimaryKey("ID" , ID);
        	
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
