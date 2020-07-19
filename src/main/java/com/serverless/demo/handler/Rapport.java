package com.serverless.demo.handler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClient;
import com.amazonaws.services.sns.model.PublishRequest;
import com.amazonaws.services.sns.model.PublishResult;

import DyrkApp.element.Get;

public class Rapport implements RequestHandler<Map, Object>{
	String ARN = "arn:aws:sns:eu-west-2:962260041785:Rapport";
	String TOPIC = "Post rapportert fra Dyrk App";
	
	@Override
	public Object handleRequest(Map input, Context context) {
		String postID = (String) input.get("ID");
		
		AmazonSNS sns = AmazonSNSClient.builder().build();
		
		new PublishRequest();
		
		Get get = new Get();
		Map komment;
		try {
			komment = get.get(postID);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			return e;
		}
		
		String description = (String) input.get("description");
		
		String content = (String) komment.get("content");
		
		String name = (String) komment.get("name");
		
		String deviceID = (String) komment.get("deviceID");
		
		context.getLogger().log("content : "+content);
		context.getLogger().log("deviceID: "+deviceID);
		
		List<Map> posts = DUtil.getAllWithDeviceID(deviceID);
		
		Map response = new HashMap();
		response.put("content", content);
		response.put("posts", posts);
		response.put("description", description);
		
		String stringResp = "----------------------------"	+"/n/n"+
							"DeviceID: "+deviceID			+"\n"+
							"Description: "+description		+"\n"+
							"Content: "+content				+"\n"+
							"Name of repported: "+name		+"\n"+
							"All posts: "+posts.toString()	+"\n\n"+
							"----------------------------";
		PublishRequest pubReq = new PublishRequest(ARN, stringResp , TOPIC);
		
		sns.publish(pubReq);
		
		
		return response;
		
	}
	
}
