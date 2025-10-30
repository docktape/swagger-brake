# Introduction
Nowadays when the most common communication protocol is HTTP and everybody is developing APIs, 
there's often a need to have these APIs backward compatible, so the existing functionality is
not going to be broken. 

**swagger-brake** tries to address this. It's a simple tool to verify whether the new version of 
your API will break the existing one. The idea behind is to compare 2 Swagger API descriptors 
with each other and report if the changes made between the 2 API states are not backward
compatible.

## Quickstart
swagger-brakes comes with different types of integration patterns. For generic use-cases, the
CLI interface is recommended and can be used by any ecosystem.

First of all, just use the latest version of the client, you can download it from 
[here](https://github.com/docktape/swagger-brake/releases).

Then as an example, [download the Petstore Swagger descriptor](https://petstore.swagger.io/v2/swagger.json).

And now you can execute swagger-brake passing the location of the Swagger descriptors as an argument. 
Note that now swagger-brake is going to check the same API against each other, 
so it's expected to not have any breaking changes reported.

```bash
$ java -jar swagger-brake.jar --old-api=swagger.json --new-api=swagger.json
Loading old API from swagger.json
Loading new API from swagger.json
Successfully loaded APIs
Starting the check for breaking API changes
Check is finished
No breaking API changes detected
```

As soon as you modify the Swagger descriptor (the new one) and let's say you remove an API
completely, this will be the result.

```bash
$ java -jar swagger-brake.jar --old-api=swagger.json --new-api=swagger2.json
Loading old API from swagger.json
Loading new API from swagger2.json
Successfully loaded APIs
Starting the check for breaking API changes
Check is finished
There were breaking API changes
Path /pet POST has been deleted
```

More details on the command-line interface can be found [here](../cli/README.md).