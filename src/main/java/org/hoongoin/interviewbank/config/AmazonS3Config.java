package org.hoongoin.interviewbank.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

@Configuration
public class AmazonS3Config {

	@Value("${aws.credentials.accessKeyId}")
	private String accessKeyId;

	@Value("${aws.credentials.secretAccessKey}")
	private String secretAccessKey;

	@Value("${aws.region.static}")
	private String region;

	@Bean
	public AmazonS3 amazonS3() {
		BasicAWSCredentials credentials = new BasicAWSCredentials(accessKeyId, secretAccessKey);
		return AmazonS3ClientBuilder.standard()
			.withCredentials(new AWSStaticCredentialsProvider(credentials))
			.withRegion(region)
			.build();
	}
}
