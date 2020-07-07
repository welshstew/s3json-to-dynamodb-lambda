package com.codergists;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;
import javax.inject.Named;
import org.jboss.logging.Logger;

import software.amazon.awssdk.core.sync.ResponseTransformer;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.s3.event.S3EventNotification;
import java.io.ByteArrayOutputStream;

import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;


import software.amazon.awssdk.services.s3.model.GetObjectRequest;


@Named("S3toDynamoDB")
public class S3toDynamoDB implements RequestHandler<S3EventNotification, Fruit> {

    @Inject
    S3Client s3;

    @Inject
    FruitSyncService fruitSyncer;

    private static final Logger LOGGER = Logger.getLogger(S3toDynamoDB.class);
    private Jsonb jsonb;

    @PostConstruct
    public void createJsonBuilder(){
        jsonb = JsonbBuilder.create();
    }

    @PreDestroy
    public void destroyJsonBuilder() throws Exception {
        jsonb.close();
    }
    
    //Map<String,Object> input
    // public OutputObject handleRequest(final S3Event input, final Context context) {
        //final Map<String,Object>
    @Override
    public Fruit handleRequest(final S3EventNotification input, final Context context) {

        // S3EventNotification.S3EventNotificationRecord record = input.getRecords().get(0);
        LOGGER.info(input.getRecords().get(0).getS3().getBucket().getName());
        LOGGER.info(input.getRecords().get(0).getS3().getObject().getUrlDecodedKey());

        // S3EventNotificationRecord record = (S3EventNotificationRecord)input;
        // String thing = record.getS3().getBucket().getName();
        String bucketName = input.getRecords().get(0).getS3().getBucket().getName(); // "quarkus.s3.quickstart"; // 
        String keyName = input.getRecords().get(0).getS3().getObject().getUrlDecodedKey(); //"test1.json"; //

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        GetObjectResponse getObjectResponse = s3.getObject(buildGetRequest(bucketName, keyName), ResponseTransformer.toOutputStream(baos));
        
        String json = baos.toString();
        LOGGER.info("Output:" + json);
 
        Fruit f = jsonb.fromJson(json, Fruit.class);
        LOGGER.info("Created Fruit " + f);
        fruitSyncer.add(f);

        return f;
    }

    protected GetObjectRequest buildGetRequest(String bucketName, String objectKey) {
        return GetObjectRequest.builder()
                .bucket(bucketName)
                .key(objectKey)
                .build();
    }
}
