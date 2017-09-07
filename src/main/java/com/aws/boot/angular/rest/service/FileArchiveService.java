package com.aws.boot.angular.rest.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.time.Instant;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.aws.boot.angular.rest.exception.FileArchiveServiceException;
import com.aws.boot.angular.rest.model.CustomerImage;

/**
* @author Krishna Bhat
*
*/

@Service
public class FileArchiveService {
	
	//Set your AWS_ACCESS_KEYR and AWS_SECRET_KEY on the environment variable
	//private AmazonS3Client s3Client = new AmazonS3Client(new EnvironmentVariableCredentialsProvider().getCredentials());
	
	//private AmazonS3Client s3Client = new AmazonS3Client(new BasicAWSCredentials("Your Key", "Your Secret"));

	@Autowired
	private AmazonS3Client s3Client;
	
	private static final String S3_BUCKET_NAME = "krishna-456";//already created

	//Save image to S3 and return CustomerImage containing key and public URL
	public CustomerImage saveFile(MultipartFile multipartFile) {

		try{
			File fileToUpload = convertFromMultiPart(multipartFile);
			String key = Instant.now().getEpochSecond() + "_" + fileToUpload.getName();
			/* save file */
			s3Client.putObject(new PutObjectRequest(S3_BUCKET_NAME, key, fileToUpload));
			
			/* get signed URL (valid for one year) */
			GeneratePresignedUrlRequest generatePresignedUrlRequest = new GeneratePresignedUrlRequest(S3_BUCKET_NAME, key);
			generatePresignedUrlRequest.setMethod(HttpMethod.GET);
			generatePresignedUrlRequest.setExpiration(DateTime.now().plusYears(1).toDate());

			URL signedUrl = s3Client.generatePresignedUrl(generatePresignedUrlRequest);
			
			return new CustomerImage(key, signedUrl.toString());
		}
		catch(Exception ex){			
			throw new FileArchiveServiceException("An error occurred saving file to.", ex);
		}		
	}

	//Delete image from S3 using specified key
	public void deleteImage(CustomerImage customerImage){
		s3Client.deleteObject(new DeleteObjectRequest(S3_BUCKET_NAME, customerImage.getKey()));
	}

	/**
	 * Convert MultiPartFile to ordinary File
	 * 
	 * @param multipartFile
	 * @return
	 * @throws IOException
	 */
	private File convertFromMultiPart(MultipartFile multipartFile) throws IOException {

		File file = new File(multipartFile.getOriginalFilename());
		file.createNewFile(); 
		FileOutputStream fos = new FileOutputStream(file); 
		fos.write(multipartFile.getBytes());
		fos.close(); 

		return file;
	}
}