```

$ mvn clean package -Pnative -Dnative-image.docker-build=true -Dquarkus.native.enable-jni=true -DskipTests -Dquarkus.native.additional-build-args=--allow-incomplete-classpath 

...
Error: Image build request failed with exit status 137
[INFO] ------------------------------------------------------------------------
[INFO] BUILD FAILURE
[INFO] ------------------------------------------------------------------------
[INFO] Total time:  04:32 min
[INFO] Finished at: 2020-07-03T14:28:12+01:00
[INFO] ------------------------------------------------------------------------
[ERROR] Failed to execute goal io.quarkus:quarkus-maven-plugin:1.5.2.Final:native-image (default) on project s3json-to-dynamodb-lambda: Failed to generate native image: io.quarkus.builder.BuildException: Build failure: Build failed due to errors
[ERROR]         [error]: Build step io.quarkus.deployment.pkg.steps.NativeImageBuildStep#build threw an exception: java.lang.RuntimeException: Failed to build native image
[ERROR]         at io.quarkus.deployment.pkg.steps.NativeImageBuildStep.build(NativeImageBuildStep.java:358)
```