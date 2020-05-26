package com.serverless.demo.handler;

import java.util.List;
import java.util.Map;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import DyrkApp.element.Delete;
import DyrkApp.element.Get;

import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.ItemCollection;
import com.amazonaws.services.dynamodbv2.document.PutItemOutcome;
import com.amazonaws.services.dynamodbv2.document.ScanOutcome;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.spec.ScanSpec;

public class BlockUser implements RequestHandler<Map, Object>{

	@Override
	public Object handleRequest(Map input, Context context) {
		
		String deviceID = (String) input.get("deviceID");
		
		putInBlocked(deviceID);
		
		deleteWithDeviceID(deviceID);
		
		return null;
		
	}
	
	private void putInBlocked(String deviceID) {
		Table table = DUtil.getTableBlockedUsers();
		
		Item item = new Item();
		item.with("deviceID", deviceID);
		
		table.putItem(item);
	}
	
	private void deleteWithDeviceID(String deviceID) {
		List<Map> elems = DUtil.getAllWithDeviceID(deviceID);
		
		Delete delete = new Delete();
		
		for(Map elem : elems) {
			delete.handleRequest(elem, null);
		}
	}

}
