import java.util.Random;

import com.serverless.demo.handler.S3Bucket;

public class TestS3 {
	public static void main(String[] args) {
		S3Bucket s3 = new S3Bucket();
		Random r = new Random();
		
		try {
			s3.add("imagestringdsjhg", "16");
			System.out.println("done");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
