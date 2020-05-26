package DyrkAppDescription;

import java.util.HashMap;
import java.util.Map;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.serverless.demo.handler.S3Bucket;

public class GetDescription implements RequestHandler<Object, Object>{

	@Override
	public Object handleRequest(Object input, Context context) {
		
		Map reqMap = (Map) input;
		String name = (String) reqMap.get("name"); 
		
		S3Bucket s3 = new S3Bucket();
		
		try {
			String txt = s3.get(name);
			Map response = new HashMap<String, Object>();
			response.put("content", txt);
			return response;
		}catch(Exception e) {
			e.printStackTrace();
			return e.getMessage();
		}
	}
	
}
