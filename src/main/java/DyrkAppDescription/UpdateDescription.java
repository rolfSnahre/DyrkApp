package DyrkAppDescription;

import java.util.Map;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.serverless.demo.handler.S3Bucket;

public class UpdateDescription implements RequestHandler<Map<String, String>, Object>{

	@Override
	public Object handleRequest(Map<String,String> input, Context context) {

		S3Bucket s3 = new S3Bucket();
		
		try {
			s3.add(input.get("content"), input.get("name"));
			return input;
		} catch (Exception e) {
			e.printStackTrace();
			return e.getMessage();
		}
	}

}
