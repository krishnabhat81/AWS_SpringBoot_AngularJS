package com.aws.boot.angular.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.amazonaws.auth.EnvironmentVariableCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3Client;

/**
* @author Krishna Bhat
*
*/

@Configuration
public class RDSConfig {
	@Bean
	public AmazonS3Client getCredentials(){
		//Set your AWS_ACCESS_KEYR and AWS_SECRET_KEY on the environment variable
		return new AmazonS3Client(new EnvironmentVariableCredentialsProvider().getCredentials());
	}
}
