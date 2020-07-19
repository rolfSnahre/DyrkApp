package com.serverless.demo.handler;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.ItemCollection;
import com.amazonaws.services.dynamodbv2.document.ScanOutcome;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.spec.ScanSpec;

import DyrkApp.element.Delete;
import DyrkApp.element.Get;

public class DUtil {

	public static Table getTable() {
		AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard()
				.withRegion(Regions.EU_WEST_2)
				.build();

        DynamoDB dynamoDB = new DynamoDB(client);

        return dynamoDB.getTable("Dyrk");
	}
	
	public static Table getTableBlockedUsers() {
		AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard()
				.withRegion(Regions.EU_WEST_2)
				.build();

        DynamoDB dynamoDB = new DynamoDB(client);

        return dynamoDB.getTable("BlockedUsers");
	}
	
	public static List<Map> getAllWithDeviceID(String deviceID){
		Table table = DUtil.getTable();
		
		ItemCollection<ScanOutcome> outcome = table.scan(new ScanSpec());
		
		List<Map<String, Object>> elements = DUtil.itemCollectToMapList(outcome);
		
		List<Map> elemsWithDevID = new ArrayList<Map>();
		for(Map<String, Object> elem : elements) {
			
			if(elem.containsKey("deviceID") && ((String) elem.get("deviceID")).equals(deviceID)){
				elemsWithDevID.add(elem);
			}
			
		}
		
		return elemsWithDevID;
	}
	
	public static DynamoDB getDynamoDB() {
		AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard()
				.withRegion(Regions.EU_WEST_2)
				.build();

        return new DynamoDB(client);
	}
	
	public static String inputToID(Object input) {
		if(input instanceof Map) {
			return (String) ((Map) input).get("ID");
		}else {
			return (String) input;
		}
	}
	
	public Map clientToTableNames(Map input) {
		input.put("sort", input.get("date"));
		input.remove("date");
		return input;
	}
	
	public Map tableToClientNames(Map input) {
		input.put("sort", input.get("date"));
		input.remove("date");
		return input;
	}
	
	public static List<Map<String, Object>> itemCollectToMapList(ItemCollection itemColection){
		List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
		for(Object i : itemColection) {
			mapList.add( ((Item) i).asMap());
		}
		return mapList;
	}
	
}
