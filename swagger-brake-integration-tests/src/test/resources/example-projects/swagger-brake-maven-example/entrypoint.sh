#!/bin/sh

if [ -d "local-swagger-brake" ]; then
    mkdir -p /root/.m2/repository/com/docktape/
    cp -r local-swagger-brake/* /root/.m2/repository/com/docktape/
    echo "Local Swagger Brake has been successfully copied from the host"
fi

if [ "$REMOVE_PACKAGING" == "true" ]
then
  sed -i "s/<packaging>\${custom.packaging}<\/packaging>/ /g" pom.xml
fi

sh mvnw clean deploy