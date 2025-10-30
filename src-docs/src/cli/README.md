# Command-line interface
## Installation
The command-line interface comes as a pre-packaged JAR application. The package is compiled with Java 11, so make
sure you have Java 11+ installed.

*Note: Java 8 has been tested regularly and no issues have been encountered so far.*
 
Use the latest version of the client, you can download it from 
[here](https://github.com/docktape/swagger-brake/releases).

## Basics
The idea of the command-line interface is to provide a way to integrate swagger-brake into
any type of system, pipeline. There are several configuration parameters available that you 
could use to customize the behavior of swagger-brake.

The most basic usage is to compare 2 Swagger files. 

```bash
$ java -jar swagger-brake.jar --old-api=swagger.json --new-api=swagger.json
Loading old API from swagger.json
Loading new API from swagger.json
Successfully loaded APIs
Starting the check for breaking API changes
Check is finished
No breaking API changes detected
```

## Customizing reporting
The custom reporting functionality is described in the 
[Customizing reporting](../configuration/README.md#customizing-reporting) section.

Note that whatever custom reporting type you set for the command-line interface, console
reporting will always be enabled. 

To configure a custom reporting, the CLI accepts the `--output-formats` argument. The parameter
accepts a comma separated list of reporting formats, in case you want to use multiple reporters.

For file typed reporters like JSON or HTML, you must also pass the `--output-path` argument 
denoting the location where the reports should be saved.

An example configuration could look the following:
```bash
$ java -jar swagger-brake.jar --old-api=swagger.json --new-api=swagger2.json --output-formats=STDOUT,JSON,HTML --output-path=/home/user/swagger-brake
```

## Deprecating APIs
The API deprecation support is described in the 
[Deprecating APIs](../configuration/README.md#deprecating-apis) section.

The `--deprecated-api-deletion-allowed` parameter is responsible for setting this behavior. 
The default value is `true`, so **there's no additional configuration is required**, 
but it can be overridden to `false` anytime.

## Latest Maven artifact resolution
Latest Maven artifact resolution is described in detail within the 
[Latest Maven artifact resolution](../configuration/README.md#latest-maven-artifact-resolution) section.

The functionality requires the following parameters:
* `--maven-repo-url`
  * Specifies the release repository base URL. Might be optional in case `--maven-snapshot-repo-url` is provided.
    * Example: `https://oss.jfrog.org/oss-release-local/`
* `--maven-snapshot-repo-url`
  * Specifies the snapshot repository base URL. Might be optional in case `--maven-repo-url` is provided.
    * Example: `https://oss.jfrog.org/oss-snapshot-local/`
* `--groupId`
  * The groupId of the artifact 
* `--artifactId`
  * The artifactId of the artifact
* `--current-artifact-version`
  * The version of the artifact that contains the new API. This is used to determine if the snapshot, 
or the release repository needs to be used. 
    * Example values are: 1.0.0, 1.0.0-SNAPSHOT
* `--artifact-packaging`
  * Specifies the artifact packaging. Could be jar or war. Used when resolving the latest artifact version.
    
Example command:
```bash
$ java -jar swagger-brake.jar --new-api=swagger.yaml --maven-repo-url=https://oss.jfrog.org/oss-snapshot-local --maven-snapshot-repo-url=https://oss.jfrog.org/oss-snapshot-local --groupId=com.example --artifactId=petstore-api --current-artifact-version=1.0.0-SNAPSHOT
```

Note: if you provide both `--old-api` and Maven parameters, `--old-api` will take precedence.

The filename to search for can be customized by providing the `--api-filename` parameter.
Note that it's enough to provide the filename without the extension, in that case the JSON, YAML and YML
extension will be used for scanning.

Example command:
```bash
$ java -jar swagger-brake.jar --new-api=swagger.yaml --maven-repo-url=https://oss.jfrog.org/oss-snapshot-local --maven-snapshot-repo-url=https://oss.jfrog.org/oss-snapshot-local --groupId=com.example --artifactId=petstore-api --current-artifact-version=1.0.0-SNAPSHOT --api-filename=something.yaml
``` 

It's also possible that the repository is secured with username and password authentication. The following
2 parameters can be used to provide the credentials to access the repository:
* `--maven-repo-username`
* `--maven-repo-password`

## Beta API support
For further reference, check out [Beta API support](../configuration/README.md#beta-api-support) 
in the Configuration section.

By providing the `--beta-api-extension-name`, one can override the default `x-beta-api` attribute name,
and the given one will be used.

Example command:
```bash
$ java -jar swagger-brake.jar --old-api=swagger.yaml --new-api=swagger2.yaml --beta-api-extension-name=x-custom-beta-attributes
```

## Excluding paths from the scan
For detailed description on the feature, see [Excluding paths from the scan](../configuration/README.md#excluding-paths-from-the-scan).

There's a parameter `--excluded-paths` where you can provide a list of paths you want to be excluded
from the check. The paths can be separated by commas.

Example command:

```bash
$ java -jar swagger-brake.jar --old-api=swagger.yaml --new-api=swagger2.yaml --excluded-paths=/auth 
```

## Ignoring specific breaking changes
For detailed description on the feature, see [Ignoring specific breaking changes](../configuration/README.md#ignoring-specific-breaking-changes).

There's a parameter `--ignored-breaking-change-rules` where you can provide a list of rule codes you want to be ignored
during the check. The rules can be separated by commas. The rule codes can be found in the docs.

Example command:

```bash
$ java -jar swagger-brake.jar --old-api=swagger.yaml --new-api=swagger2.yaml --ignored-breaking-change-rules=R001,R002
```


## Full list of parameters
| <div style="width:250px">Parameter</div>   | Description                                                                                                                                               |
|:------------------------------------------:|:---------------------------------------------------------------------------------------------------------------------------------------------------------:|
| `--old-api`                                | Denotes the path of the baseline API. Can be a relative path and an absolute one.                                                                         |
| `--new-api`                                | Denotes the path of the new, changed API. Can be a relative path and an absolute one.                                                                     |
| `--output-formats`                         | Specifies which reports shall be generated. Possible values: `STDOUT`, `JSON`, `HTML`                                                                     |
| `--output-path`                            | Denotes the folder where the file reports shall be saved. Can be a relative path and an absolute one. In case the path doesn't exist, it will be created. |
| `--maven-repo-url`                         | Specifies the release repository base URL. Might be optional in case `--maven-snapshot-repo-url` is provided.                                             |
| `--maven-snapshot-repo-url`                | Specifies the snapshot repository base URL. Might be optional in case `--maven-repo-url` is provided.                                                     |
| `--maven-repo-username`                    | The username for the Maven repository.                                                                                                                    |
| `--maven-repo-password`                    | The password for the Maven repository.                                                                                                                    |
| `--groupId`                                | The groupId of the artifact.                                                                                                                              |
| `--artifactId`                             | The artifactId of the artifact.                                                                                                                           |
| `--current-artifact-version`               | The version of the artifact that contains the new API. This is used to determine if the snapshot, or the release repository needs to be used.             |
| `--artifact-packaging`                     | Specifies the artifact packaging. Could be jar or war. Used when resolving the latest artifact version. Defaults to jar if not specified.                 |
| `--api-filename`                           | The filename to search for within the artifact.                                                                                                           |
| `--beta-api-extension-name`                | The name of the custom vendor extension attribute that denotes beta APIs.                                                                                 |
| `--excluded-paths`                         | A comma separated list of path prefixes that shall be excluded from the scan.                
| `--ignored-breaking-change-rules`                         | Specifies which breaking changes shall be ignored. Rules have to be provided (find them in the doc). Multiple values can be provided by using comma. Example: R001,R002                