package DyrkApp.element;

import java.util.Map;

import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

public class Update implements RequestHandler<Map<String, Object>, Object>{

	@Override
	public Object handleRequest(Map<String, Object> input, Context context) {
		context.getLogger().log(input.toString());
		
//		if(get.inTable(input)) {
//			return "Object not in table";
//		}
        
		try {
			
			Get get = new Get();
			get.get(input);
			
			input.put("sort",input.get("date"));
			input.remove("date");
			
			Map map = update(input);
			
			map.put("date", map.get("sort"));
        	map.remove("sort");
        	
        	return map;
		}catch(Exception e) {
			e.printStackTrace();
			return "Internal error";
		}
		
	}
	
	public Map update(Map input) throws Exception {
		
		Add add = new Add();
		
		Item item = new Item();
		
		Map m = add.add(item, input);
    	
    	return m;
		
	}

}
