package com.aws.boot.angular;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AwsSpringBootAngularJsApplication {

	public static void main(String[] args) {
		SpringApplication.run(AwsSpringBootAngularJsApplication.class, args);
		
	}
}

//deploy jar to aws EC2
//scp -i MyEC2Key.pem /Users/krishna1bhat/Desktop/springboot-aws.jar ec2-user@<<remote IP address>>:~/.