# Setup



```
docker run -it --publish 8008:4572 -e SERVICES=s3 -e START_WEB=0 localstack/localstack

aws s3 mb s3://quarkus.s3.quickstart --profile localstack --endpoint-url=http://localhost:8008
aws s3 sync ~/Downloads s3://quarkus.s3.quickstart --profile localstack --endpoint-url=http://localhost:8008



docker run --publish 8000:8000 amazon/dynamodb-local:1.11.477 -jar DynamoDBLocal.jar -inMemory -sharedDb

```
Open http://localhost:8000/shell in your browser.



Copy and paste the following code to the shell and run it:

```
var params = {
    TableName: 'QuarkusFruits',
    KeySchema: [{ AttributeName: 'fruitName', KeyType: 'HASH' }],
    AttributeDefinitions: [{  AttributeName: 'fruitName', AttributeType: 'S', }],
    ProvisionedThroughput: { ReadCapacityUnits: 1, WriteCapacityUnits: 1, }
};

dynamodb.createTable(params, function(err, data) {
    if (err) ppJson(err);
    else ppJson(data);


});
```

https://github.com/quarkusio/quarkus/issues/7670

https://quarkus.io/guides/cdi-reference


## Bits I got stuck on...

```
2020-07-02 17:14:56,894 ERROR [io.qua.ama.lam.run.AbstractLambdaPollLoop] (Lambda Thread) Failed to run lambda: com.fasterxml.jackson.databind.exc.InvalidDefinitionException: Cannot construct instance of `com.amazonaws.services.s3.event.S3EventNotification$S3EventNotificationRecord` (no Creators, like default construct, exist): cannot deserialize from Object value (no delegate- or property-based Creator)
 at [Source: (sun.net.www.protocol.http.HttpURLConnection$HttpInputStream); line: 4, column: 11] (through reference chain: com.amazonaws.services.lambda.runtime.events.S3Event["Records"]->java.util.ArrayList[0])
        at com.fasterxml.jackson.databind.DeserializationContext.reportBadDefinition(DeserializationContext.java:1592)
```