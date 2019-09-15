package com.cmm.jft.data.cloud.aws;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CompleteMultipartUploadRequest;
import com.amazonaws.services.s3.model.CopyObjectRequest;
import com.amazonaws.services.s3.model.CopyObjectResult;
import com.amazonaws.services.s3.model.InitiateMultipartUploadRequest;
import com.amazonaws.services.s3.model.InitiateMultipartUploadResult;
import com.amazonaws.services.s3.model.ListObjectsV2Request;
import com.amazonaws.services.s3.model.ListObjectsV2Result;
import com.amazonaws.services.s3.model.PartETag;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.amazonaws.services.s3.model.UploadPartRequest;
import com.amazonaws.services.s3.model.UploadPartResult;

import edu.emory.mathcs.backport.java.util.Collections;

import com.amazonaws.services.athena.*;


public class S3Client {

    private String accessKey;
    private String secretKey;
    private Regions region;
    private String bucketName;
    private static S3Client s3Client;

    private S3Client(Regions region, String bucket, String accessKey, String secretKey) {
        
        this.accessKey = accessKey;
        this.secretKey = secretKey;
        this.bucketName = bucket;
        this.region = region == null? Regions.US_EAST_2 : region;
    }

    public static void main(String[] args) throws IOException {
        
    	String s3Folder = "MarketData/Bovespa-Opcoes/";
    	
        S3Client client = S3Client.startClient();
        
        client.uploadBatch("/media/cristiano/Disco/Disco/Data/BMFBovespa/MarketData/Bovespa-Opcoes", s3Folder);
    }
    
    
    public static S3Client startClient() {

		Properties properties = new Properties();
		try {
			properties.load(S3Client.class.getClassLoader().getResourceAsStream("config.properties"));

			s3Client = new S3Client(
					Regions.fromName(properties.getProperty("awsMDRegion")), 
					properties.getProperty("awsMDBucketBkp"), 
					properties.getProperty("awsAccessKey"), 
					properties.getProperty("awsSecretKey"));

		} catch (IOException e) {
			e.printStackTrace();
		}

		return s3Client;
	}


	public void uploadBatch(String batchFolder, String s3Folder) {

		List<S3ObjectSummary> s3Objects = listObjects(s3Folder);

		HashMap<String, S3ObjectSummary> summaries = new HashMap<>();

		s3Objects.forEach(o -> {
			summaries.put(o.getKey(), o);
		});    	
		
		List<File> files = new ArrayList(); 
		
		Collections.addAll(files, new File(batchFolder).listFiles());
		
		files.stream().sorted(Comparator.comparing(File::getName));
		
		for(File file : files) {
			
			if(file.length() == 0) continue;

			String key = s3Folder + file.getName();
			System.out.println("Uploading file " + file.getName());
			
			if(!summaries.containsKey(key) || summaries.get(key).getSize() < file.length()) {
				
				if(!doesObjectExist(key)) {
					if(s3Client.uploadObject(file, key)) {
						System.out.println(" --> uploaded.");
					}else {
						System.out.println(" --> error");
					}
				}
				else {
					System.out.println("object already uploaded");
				}  
			}
			else {
				System.out.println("object already uploaded");
			}            
		}
	}
	
	public boolean doesObjectExist(String key) {
		
		AWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);

		AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
				.withCredentials(new AWSStaticCredentialsProvider(credentials))
				.withRegion(region)
				.build();
		
		return s3Client.doesObjectExist(bucketName, key);
	}

	public void copyObject(String sourceKey, String destinationKey) {
		try {

			AWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);

			AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
					.withCredentials(new AWSStaticCredentialsProvider(credentials))
					.withRegion(region)
					.build();

			// Copy the object into a new object in the same bucket.
			CopyObjectRequest copyObjRequest = new CopyObjectRequest(bucketName, sourceKey, bucketName, destinationKey);
			CopyObjectResult result = s3Client.copyObject(copyObjRequest);
		}
		catch(AmazonServiceException e) {
			// The call was transmitted successfully, but Amazon S3 couldn't process 
			// it, so it returned an error response.
			e.printStackTrace();
		}
		catch(SdkClientException e) {
			// Amazon S3 couldn't be contacted for a response, or the client  
			// couldn't parse the response from Amazon S3.
			e.printStackTrace();
		}   
	}

	public List<S3ObjectSummary> listObjects(String bucketFolder){
		List<S3ObjectSummary> objects = new ArrayList<>();

		AWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);

		AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
				.withCredentials(new AWSStaticCredentialsProvider(credentials))
				.withRegion(region)
				.build();

		ListObjectsV2Request request = new ListObjectsV2Request()
				.withBucketName(bucketName)
				.withPrefix(bucketFolder)
				.withMaxKeys(5000);

		ListObjectsV2Result result = s3Client.listObjectsV2(request);    	

		objects = result.getObjectSummaries();

		return objects;
	}

	public boolean uploadObject(File file, String objKeyName) {

		boolean uploaded = false;
		try {            
			AWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);

			AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
					.withCredentials(new AWSStaticCredentialsProvider(credentials))
					.withRegion(region)
					.build();

			if(file.exists()) {

				long contentLength = file.length();
				long partSize = 5 * 1024 * 1024; // Set part size to 5 MB. 

				// Create a list of ETag objects. You retrieve ETags for each object part uploaded,
				// then, after each individual part has been uploaded, pass the list of ETags to 
				// the request to complete the upload.
				List<PartETag> partETags = new ArrayList<PartETag>();

				// Initiate the multipart upload.
				InitiateMultipartUploadRequest initRequest = new InitiateMultipartUploadRequest(bucketName, objKeyName);
				InitiateMultipartUploadResult initResponse = s3Client.initiateMultipartUpload(initRequest);

				// Upload the file parts.
				long filePosition = 0;
				for (int i = 1; filePosition < contentLength; i++) {
					// Because the last part could be less than 5 MB, adjust the part size as needed.
					partSize = Math.min(partSize, (contentLength - filePosition));

					// Create the request to upload a part.
					UploadPartRequest uploadRequest = new UploadPartRequest()
							.withBucketName(bucketName)
							.withKey(objKeyName)
							.withUploadId(initResponse.getUploadId())
							.withPartNumber(i)
							.withFileOffset(filePosition)
							.withFile(file)
							.withPartSize(partSize);

					// Upload the part and add the response's ETag to our list.
					UploadPartResult uploadResult = s3Client.uploadPart(uploadRequest);
					partETags.add(uploadResult.getPartETag());

					filePosition += partSize;
				}

				// Complete the multipart upload.
				CompleteMultipartUploadRequest compRequest = new CompleteMultipartUploadRequest(
						bucketName, objKeyName, initResponse.getUploadId(), partETags);

				s3Client.completeMultipartUpload(compRequest);

				uploaded = file.length() == s3Client.getObjectMetadata(bucketName, objKeyName).getContentLength();
								
				if(!uploaded) {
					s3Client.deleteObject(bucketName, objKeyName);
				}               
			}

		} catch (AmazonClientException e) {
			// The call was transmitted successfully, but Amazon S3 couldn't process 
			// it, so it returned an error response.
			uploaded = false;
			e.printStackTrace();
		}

		return uploaded;
	}

	public void deleteObject() {

	}

	public void downloadObject() {

	}

}