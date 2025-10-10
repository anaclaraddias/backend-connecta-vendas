package br.unibh.sdm.repository;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.PredefinedClientConfigurations;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;

/**
 * Configurações para acessar o AWS DynamoDB
 *  <br>
 * Para rodar, antes sete a seguinte variável de ambiente: -Dspring.config.location=C:/Users/jhcru/sdm/
 *  <br>
 * Neste diretório, criar um arquivo application.properties contendo as seguitnes variáveis:
 * <br>
 * amazon.aws.accesskey=<br>
 * amazon.aws.secretkey=<br>
 * @author jhcru
 *
 */
@Configuration
public class DynamoDBConfig {

	@Value("${amazon.aws.accesskey}")
	private String amazonAWSAccessKey;

	@Value("${amazon.aws.secretkey}")
	private String amazonAWSSecretKey;
	@Value("${amazon.dynamodb.requestTimeout:5000}")
	private int requestTimeout;

	@Value("${amazon.dynamodb.connectionTimeout:2000}")
	private int connectionTimeout;

	public AWSCredentialsProvider amazonAWSCredentialsProvider() {
		return new AWSStaticCredentialsProvider(amazonAWSCredentials());
	}

	@Bean
	public AWSCredentials amazonAWSCredentials() {
		return new BasicAWSCredentials(amazonAWSAccessKey, amazonAWSSecretKey);
	}

	@Bean
	public AmazonDynamoDB amazonDynamoDB() {
	ClientConfiguration clientConfig = PredefinedClientConfigurations.defaultConfig();
	clientConfig.setConnectionTimeout(connectionTimeout);
	clientConfig.setSocketTimeout(requestTimeout);

	AmazonDynamoDBClientBuilder builder = AmazonDynamoDBClientBuilder.standard()
		.withCredentials(amazonAWSCredentialsProvider())
		.withClientConfiguration(clientConfig)
		.withRegion(Regions.US_EAST_1);

	return builder.build();
	}

}