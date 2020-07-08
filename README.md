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

## Confirming Input

Run the following in the local DynamoDB Shell:

```
var dynamodb = new AWS.DynamoDB({endpoint: 'http://localhost:8000' })

var params = {
    TableName: 'QuarkusFruits',
    Limit: 1, // optional (limit the number of items to evaluate)
    Select: 'ALL_ATTRIBUTES', // optional (ALL_ATTRIBUTES | ALL_PROJECTED_ATTRIBUTES | 
                              //           SPECIFIC_ATTRIBUTES | COUNT)
    ReturnConsumedCapacity: 'NONE', // optional (NONE | TOTAL | INDEXES)
};
dynamodb.scan(params, function(err, data) {
    if (err) ppJson(err); // an error occurred
    else ppJson(data); // successful response
});
```


As working:

JVM Mode:
```
REPORT RequestId: c817e315-621b-16bc-b0e5-169e0d87f06a	
Init Duration: 1939.45 ms	Duration: 410.97 ms
	Billed Duration: 500 ms	
    Memory Size: 512 MB	Max Memory Used: 133 MB
```

Native mode:

```
user@user-ubuntuvm:~/Documents/quarkus/s3json-to-dynamodb-lambda$ sam local invoke --template target/sam.native.yaml --event s3-event-payload.json
Invoking not.used.in.provided.runtime (provided)
Decompressing /home/user/Documents/quarkus/s3json-to-dynamodb-lambda/target/function.zip

Fetching lambci/lambda:provided Docker container image......
Mounting /tmp/tmpnm1dkdrl as /var/task:ro,delegated inside runtime container
__  ____  __  _____   ___  __ ____  ______ 
 --/ __ \/ / / / _ | / _ \/ //_/ / / / __/ 
 -/ /_/ / /_/ / __ |/ , _/ ,< / /_/ /\ \   
--\___\_\____/_/ |_/_/|_/_/|_|\____/___/   
2020-07-08 15:13:38,429 INFO  [io.quarkus] (main) s3json-to-dynamodb-lambda 1.0-SNAPSHOT native (powered by Quarkus 1.5.2.Final) started in 0.024s. Listening on: http://0.0.0.0:8080
2020-07-08 15:13:38,430 INFO  [io.quarkus] (main) Profile prod activated. 
2020-07-08 15:13:38,430 INFO  [io.quarkus] (main) Installed features: [amazon-dynamodb, amazon-lambda, amazon-s3, cdi, resteasy-jsonb]
START RequestId: 9205182b-4469-1e42-e761-bc85d58d7a79 Version: $LATEST
2020-07-08 15:13:38,438 INFO  [com.cod.S3toDynamoDB] (Lambda Thread) quarkus.s3.quickstart
2020-07-08 15:13:38,438 INFO  [com.cod.S3toDynamoDB] (Lambda Thread) durian.json
2020-07-08 15:13:38,468 INFO  [com.cod.S3toDynamoDB] (Lambda Thread) Output:{ "name" : "Durian", "description":"Smelly" }
2020-07-08 15:13:38,469 INFO  [com.cod.S3toDynamoDB] (Lambda Thread) Created Fruit com.codergists.Fruit@7f092ac83138
END RequestId: 9205182b-4469-1e42-e761-bc85d58d7a79
REPORT RequestId: 9205182b-4469-1e42-e761-bc85d58d7a79	Init Duration: 181.16 ms	Duration: 150.95 ms	Billed Duration: 200 ms	Memory Size: 128 MB	Max Memory Used: 59 MB	

{"name":"Durian","description":"Smelly"}
```