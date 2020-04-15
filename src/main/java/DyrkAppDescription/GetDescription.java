package DyrkAppDescription;

import java.util.Map;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.serverless.demo.handler.S3Bucket;

public class GetDescription implements RequestHandler<String, Object>{

	@Override
	public Object handleRequest(String name, Context context) {
		
		
		S3Bucket s3 = new S3Bucket();
		
		try {
			return s3.get(name);
		}catch(Exception e) {
			e.printStackTrace();
			return e.getMessage();
		}
	}
	
}
