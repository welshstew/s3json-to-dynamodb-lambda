package com.codergists;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import com.amazonaws.services.s3.event.S3EventNotification;
import org.jboss.logging.Logger;

import io.quarkus.amazon.lambda.test.LambdaClient;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.common.QuarkusTestResource;
import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import com.amazonaws.services.lambda.runtime.events.S3Event;

@QuarkusTest
@QuarkusTestResource(S3Resource.class)
@QuarkusTestResource(DynamodbResource.class)
public class LambdaHandlerTest {

    private static final Logger LOGGER = Logger.getLogger(LambdaHandlerTest.class);


    @Test
    public void testSimpleLambdaSuccess() throws Exception {

        LOGGER.info("1");
        String jsonString = new String(getClass().getClassLoader().getResourceAsStream("s3-event-payload.json").readAllBytes());
        LOGGER.info("2:" + jsonString);

        S3EventNotification notification = S3EventNotification.parseJson(jsonString);

        LOGGER.info(notification.getRecords().get(0).getS3().getBucket().getName());
        LOGGER.info(notification.getRecords().get(0).getS3().getObject().getUrlDecodedKey());
        Fruit out = LambdaClient.invoke(Fruit.class, notification);
        Assertions.assertEquals(out.getName(), "Guava");
        Assertions.assertEquals(out.getDescription(), "Juicy");

    }
}
