# Developer Guide
## Embedding swagger-brake
There could be cases where you want to integrate swagger-brake directly into your application and build up another
interface, just for your use-case. 

swagger-brake is built in a way to make this an achievable objective. The swagger-brake repo contains two modules:
* swagger-brake
* swagger-brake-cli

The `swagger-brake` module is the core module which holds all the logic specific to the breaking change detection
mechanism. `swagger-brake-cli` is only an interface in front of the core module, just like the Maven and Gradle plugins.

When swagger-brake is released into the Maven central, the core module is pushed separately, so you can simply use
it as a dependency without bringing in any unnecessary dependency from the interface modules.

For Maven:
```xml
<dependency>
    <groupId>com.docktape</groupId>
    <artifactId>swagger-brake</artifactId>
    <version>2.6.0</version>
</dependency>
```

For Gradle:
```groovy
compile group: 'com.docktape', name: 'swagger-brake', version: '2.6.0'
```

The entrypoint for the execution is the `com.docktape.swagger.brake.runner.Starter` class. There are several methods available:
* `start(Options)`
    * Used for kicking-off the CLI, full-fledged check with the additional features like downloading from repositories, etc.
* `check(OpenAPI, OpenAPI)`
    * Only for library users where the main idea is to just execute a check between 2 APIs.
* `check(OpenAPI, OpenAPI, CheckerOptions)`
    * Only for library users where the main idea is to just execute a check between 2 APIs. In addition to the other
      `check` method, this accepts some customization parameter which affects the result.