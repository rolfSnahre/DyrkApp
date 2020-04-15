package com.serverless.demo.handler;

import java.util.List;
import java.util.Map;

import com.amazonaws.services.dynamodbv2.document.ItemCollection;
import com.amazonaws.services.dynamodbv2.document.ScanOutcome;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.spec.ScanSpec;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import DyrkApp.element.Delete;
import DyrkApp.element.Get;

public class Clean implements RequestHandler<Object, String>{

	@Override
	public String handleRequest(Object input, Context context) {
		Table table = DUtil.getTable();
		
		ItemCollection<ScanOutcome> outcome = table.scan(new ScanSpec());
		
		List<Map<String, Object>> elements = DUtil.itemCollectToMapList(outcome);
		
		Get get = new Get();
		Delete delete = new Delete();
		
		int deleted = 0;
		
		for(Map<String, Object> elem : elements) {
			String parentID = (String) elem.get("parantID");
			if(parentID != "Object" && !get.inTable(parentID)){
				delete.handleRequest(input, null);
				deleted++;
			}
			
		}
		
		return deleted + " elements deleted";
		
		
	}

}
