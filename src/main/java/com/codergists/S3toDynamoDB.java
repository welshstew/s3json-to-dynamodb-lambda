package com.codergists;

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

import software.amazon.awssdk.services.s3.model.GetObjectRequest;


@Named("S3toDynamoDB")
public class S3toDynamoDB implements RequestHandler<S3EventNotification, OutputObject> {

    @Inject
    S3Client s3;

    @Inject
    ProcessingService service;

    @Inject
    FruitSyncService fruitSyncer;

    private static final Logger LOGGER = Logger.getLogger(S3toDynamoDB.class);


    
    //Map<String,Object> input
    // public OutputObject handleRequest(final S3Event input, final Context context) {
        //final Map<String,Object>
    @Override
    public OutputObject handleRequest(final S3EventNotification input, final Context context) {

        // S3EventNotification.S3EventNotificationRecord record = input.getRecords().get(0);

        LOGGER.info(input.toString());
        LOGGER.info(input.getRecords().get(0).getS3().getBucket().getName());
        LOGGER.info(input.getRecords().get(0).getS3().getObject().getUrlDecodedKey());

        // S3EventNotificationRecord record = (S3EventNotificationRecord)input;
        // String thing = record.getS3().getBucket().getName();
        String bucketName = "quarkus.s3.quickstart"; // input.getRecords().get(0).getS3().getBucket().getName();
        String keyName = "test1.json"; //input.getRecords().get(0).getS3().getObject().getUrlDecodedKey();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        GetObjectResponse object = s3.getObject(buildGetRequest(bucketName, keyName), ResponseTransformer.toOutputStream(baos));

        //  LOGGER.info(baos.toString());


        final Fruit fruit = fruitSyncer.get("Banana");
        LOGGER.info(fruit.getDescription());


        final OutputObject outputObject = new OutputObject();
        outputObject.setRequestId("requestId");
        outputObject.setResult("hello Bill");

        return outputObject;

    }

    protected GetObjectRequest buildGetRequest(String bucketName, String objectKey) {
        return GetObjectRequest.builder()
                .bucket(bucketName)
                .key(objectKey)
                .build();
    }
}
