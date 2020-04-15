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
import com.amazonaws.services.dynamodbv2.document.Table;

public class DUtil {

	public static Table getTable() {
		AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard()
				.withRegion(Regions.EU_WEST_2)
				.build();

        DynamoDB dynamoDB = new DynamoDB(client);

        return dynamoDB.getTable("Dyrk");
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
