package com.serverless.demo.handler;

public class TestBucket {
	public static void main (String[] args) {
		S3Bucket s3 = new S3Bucket();
		
		try {
			System.out.println(s3.get("16"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
