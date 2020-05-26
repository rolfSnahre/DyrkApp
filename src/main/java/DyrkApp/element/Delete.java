package DyrkApp.element;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.PrimaryKey;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.TableWriteItems;
import com.amazonaws.services.dynamodbv2.document.spec.BatchWriteItemSpec;
import com.amazonaws.services.dynamodbv2.document.spec.DeleteItemSpec;
import com.amazonaws.services.dynamodbv2.document.utils.ValueMap;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.serverless.demo.handler.DUtil;
import com.serverless.demo.handler.S3Bucket;

public class Delete implements RequestHandler<Object, Object>{

	@Override
	public Object handleRequest(Object input, Context context) {
		
		String ID;
		
		if(input instanceof Map) {
			ID = (String) ((Map) input).get("ID");
		}else {
			ID = (String) input;
		}
		
		Table table = DUtil.getTable();
		
		
		Get get = new Get();
		
		if(!get.inTable(ID)) {
			return "item not in table";
		}
		
		

		Delete delete = new Delete();
		
		try {
			
			GetAll getAll = new GetAll();
			List<Map> children = (List<Map>) getAll.getAll(ID);
			
			for(Map child : children) {
				delete.handleRequest(child, context);
			}
			
			DeleteItemSpec spec = new DeleteItemSpec().withPrimaryKey("ID", ID);
			
			table.deleteItem(spec);
			S3Bucket s3 = new S3Bucket();
			
			try{
				s3.remove("photos/" + ID);
			}catch(Exception e) { }
			
			return input;
		}catch(Exception e) {
			return e.getMessage();
		}
	}


}
