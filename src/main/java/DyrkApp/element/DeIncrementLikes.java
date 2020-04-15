package DyrkApp.element;

import java.math.BigDecimal;
import java.util.Map;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

public class DeIncrementLikes implements RequestHandler<Object, Object>{

	@Override
	public Object handleRequest(Object input, Context context) {
		//context.getLogger().log(input.toString());
		
		String ID;
		if(input instanceof String) {
			ID = (String) input;	
		}else {
			Map<String, Object> inputMap = (Map<String, Object>) input;
			ID = (String) inputMap.get("ID");
		}
		
		
		Get get = new Get();
		Object oldObj;
		try {
			oldObj = get.get((String) ID);
		
		} catch (Exception e) {
			// TODO Auto-generated catch block
			return e.getMessage();
		}
		
		
		Map<String, Object> inputMap = (Map<String, Object>) input;
		
		BigDecimal likes = (BigDecimal) inputMap.get("likes");
		likes = likes.subtract(new BigDecimal(1));
		inputMap.put("likes", likes);
		
		Update update = new Update();
		
		try {
			Map map = update.update(inputMap);
			
			map.put("date", map.get("sort"));
	    	map.remove("sort");
	        	
	        	return map;
				
			} catch (Exception e) {
				e.printStackTrace();
				return e.toString();
			}
			
		}
	

}

