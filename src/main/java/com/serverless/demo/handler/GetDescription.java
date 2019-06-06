package com.serverless.demo.handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

public class GetDescription implements RequestHandler<Object, Object>{

	@Override
	public Object handleRequest(Object input, Context context) {
		
		S3Bucket s3 = new S3Bucket();
		
		try {
			return s3.get("description");
		}catch(Exception e) {
			e.printStackTrace();
			return e.getMessage();
		}
	}
	
}
