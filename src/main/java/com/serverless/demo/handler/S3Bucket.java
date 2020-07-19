package com.serverless.demo.handler;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Random;
import java.util.Scanner;

import javax.imageio.ImageIO;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.util.IOUtils;

public class S3Bucket {
	
	final String BUCKET_NAME = "dyrk-app";
	
	public String get(String path) throws Exception {
	       
		AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard().withRegion(Regions.EU_WEST_2).build();
        DynamoDB dynamoDB = new DynamoDB(client);
        AmazonS3 s3 = AmazonS3ClientBuilder.standard().withRegion(Regions.EU_WEST_2).build();

        S3Object outcome = s3.getObject(new GetObjectRequest(BUCKET_NAME, path));
        
        Scanner scanner = new Scanner(outcome.getObjectContent(), StandardCharsets.UTF_8.toString());
    	String stringOut = scanner.useDelimiter("\\A").next();
    	//String stringOut = IOUtils.toString(outcome.getObjectContent());
		
    	return stringOut;

	}
	
	public String add(String photoString, String path) throws Exception {
	       
		AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard().withRegion(Regions.EU_WEST_2).build();
        DynamoDB dynamoDB = new DynamoDB(client);
        AmazonS3 s3 = AmazonS3ClientBuilder.standard().withRegion(Regions.EU_WEST_2).build();
        
	    
        byte[] inputBytes = photoString.getBytes(StandardCharsets.UTF_8);
	    InputStream inputStream = new ByteArrayInputStream(inputBytes);
	    
        /*
	    ObjectMetadata meta = new ObjectMetadata();
        meta.setContentLength(inputBytes.length);
        meta.setContentType("image/jpeg");
	    
	    s3.putObject(new PutObjectRequest(BUCKET_NAME, path, inputStream, meta)
	    		.withCannedAcl(CannedAccessControlList.PublicRead));
	    */
	    
	    String fileName = "image.jpg";
	    
	    BufferedImage image = null;
	    Base64.Decoder decoder = Base64.getDecoder();
	    
	    byte[] imageBytes = decoder.decode(photoString);
	    
	    ByteArrayInputStream bis = new ByteArrayInputStream(imageBytes);
	    image = ImageIO.read(bis);
	    bis.close();
	    
	    ByteArrayOutputStream os = new ByteArrayOutputStream();
	    
	    ImageIO.write(image, "jpg", os);
	    
	    InputStream is = new ByteArrayInputStream(os.toByteArray());
	    
	    
	    ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType("image/jpeg");
	    
	    PutObjectRequest putObjectRequest = new PutObjectRequest(BUCKET_NAME, path, is, metadata)
	    		.withCannedAcl(CannedAccessControlList.PublicRead);
        
        s3.putObject(putObjectRequest);
        
        return path;
	}
	
	public void remove(String path) throws Exception {
		AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard().withRegion(Regions.EU_WEST_2).build();
        DynamoDB dynamoDB = new DynamoDB(client);
        AmazonS3 s3 = AmazonS3ClientBuilder.standard().withRegion(Regions.EU_WEST_2).build();
        
        s3.deleteObject(BUCKET_NAME, path);
	}
}
