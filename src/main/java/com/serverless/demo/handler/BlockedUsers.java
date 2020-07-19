package com.serverless.demo.handler;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.amazonaws.services.dynamodbv2.document.ItemCollection;
import com.amazonaws.services.dynamodbv2.document.ScanOutcome;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.spec.ScanSpec;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

public class BlockedUsers implements RequestHandler<Map, Object>{

	@Override
	public Object handleRequest(Map input, Context context) {
		
		Table table = DUtil.getTableBlockedUsers();
		
		ItemCollection<ScanOutcome> outcome = table.scan(new ScanSpec());
		
		List<Map<String, Object>> elements = DUtil.itemCollectToMapList(outcome);
		
		List<String> blockedIDlist = new ArrayList<String>();
		for(Map elem : elements) {
			blockedIDlist.add((String) elem.get("deviceID")); 
		}
		
		return blockedIDlist;
		
	}

}
	