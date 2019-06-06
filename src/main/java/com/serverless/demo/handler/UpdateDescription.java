package com.serverless.demo.handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

public class UpdateDescription implements RequestHandler<String, Object>{

	@Override
	public Object handleRequest(String input, Context context) {

		S3Bucket s3 = new S3Bucket();
		
		try {
			s3.add(input, "description");
			return "success";
		} catch (Exception e) {
			e.printStackTrace();
			return e.getMessage();
		}
	}

}
