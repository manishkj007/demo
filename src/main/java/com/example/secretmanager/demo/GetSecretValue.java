package com.example.secretmanager.demo;

import java.nio.ByteBuffer;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.secretsmanager.AWSSecretsManager;
import com.amazonaws.services.secretsmanager.AWSSecretsManagerClientBuilder;
import com.amazonaws.services.secretsmanager.model.GetSecretValueRequest;
import com.amazonaws.services.secretsmanager.model.GetSecretValueResult;
import com.amazonaws.services.secretsmanager.model.InvalidParameterException;
import com.amazonaws.services.secretsmanager.model.InvalidRequestException;
import com.amazonaws.services.secretsmanager.model.ResourceNotFoundException;

public class GetSecretValue {
	public static void main(String[] args) {
		getSecret();
	}

	public static void getSecret() {

		String secretName = "aws-secret";
		String endpoint = "secretsmanager.us-east-1.amazonaws.com";
		String region = "us-east-1";

		AwsClientBuilder.EndpointConfiguration config = new AwsClientBuilder.EndpointConfiguration(endpoint, region);
		AWSSecretsManagerClientBuilder clientBuilder = AWSSecretsManagerClientBuilder.standard();
		clientBuilder.setCredentials(new AWSStaticCredentialsProvider(new
                BasicAWSCredentials("AKIAVSRJ2FA2ZYT5AHUU", "w7OU7dvHi9nVuKtLlf2wafOM7MPsUigpXrdbUTka")));
		clientBuilder.setEndpointConfiguration(config);
		AWSSecretsManager client = clientBuilder.build();

		String secret;
		ByteBuffer binarySecretData;
		GetSecretValueRequest getSecretValueRequest = new GetSecretValueRequest().withSecretId(secretName)
				.withVersionStage("AWSCURRENT");
		GetSecretValueResult getSecretValueResult = null;
		try {
			getSecretValueResult = client.getSecretValue(getSecretValueRequest);

		} catch (ResourceNotFoundException e) {
			System.out.println("The requested secret " + secretName + " was not found");
		} catch (InvalidRequestException e) {
			System.out.println("The request was invalid due to: " + e.getMessage());
		} catch (InvalidParameterException e) {
			System.out.println("The request had invalid params: " + e.getMessage());
		}

		if (getSecretValueResult == null) {
			return;
		}

		// Depending on whether the secret was a string or binary, one of these fields
		// will be populated
		if (getSecretValueResult.getSecretString() != null) {
			secret = getSecretValueResult.getSecretString();
			System.out.println(secret);
		} else {
			binarySecretData = getSecretValueResult.getSecretBinary();
			System.out.println(binarySecretData.toString());
		}

	}

}
