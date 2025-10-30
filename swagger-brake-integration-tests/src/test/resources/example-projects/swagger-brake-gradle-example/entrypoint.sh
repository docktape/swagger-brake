#!/bin/sh

if [ -d "local-swagger-brake" ]; then
    mkdir -p /root/.m2/repository/com/docktape/
    cp -r local-swagger-brake/* /root/.m2/repository/com/docktape/
    echo "Local Swagger Brake has been successfully copied from the host"
fi

sed -i "s/<VERSION_PLACEHOLDER>/${CUSTOM_VERSION}/g" build.gradle
sed -i "s/<ARTIFACT_ID_PLACEHOLDER>/${ARTIFACT_ID}/g" settings.gradle

if [ "$PACKAGING" == "war" ]
then
  sh gradlew clean build artifactoryPublish --info \
                    -PswaggerBrakeVersion=${SWAGGER_BRAKE_VERSION} \
                    -PcustomVersion=${CUSTOM_VERSION} \
                    -PartifactoryContextUrl=${ARTIFACTORY_CONTEXT_URL} \
                    -PartifactoryRepoKey=${ARTIFACTORY_REPO_KEY} \
                    -PartifactoryRepoUrl=${ARTIFACTORY_REPO_URL} \
                    -PartifactorySnapshotRepoKey=${ARTIFACTORY_SNAPSHOT_REPO_KEY} \
                    -PartifactorySnapshotRepoUrl=${ARTIFACTORY_SNAPSHOT_REPO_URL} \
                    -PartifactoryUsername=${ARTIFACTORY_USERNAME} \
                    -PartifactoryPassword=${ARTIFACTORY_PASSWORD} \
                    -Ppackaging=${PACKAGING}
else
  sh gradlew clean build artifactoryPublish --info \
                  -PswaggerBrakeVersion=${SWAGGER_BRAKE_VERSION} \
                  -PcustomVersion=${CUSTOM_VERSION} \
                  -PartifactoryContextUrl=${ARTIFACTORY_CONTEXT_URL} \
                  -PartifactoryRepoKey=${ARTIFACTORY_REPO_KEY} \
                  -PartifactoryRepoUrl=${ARTIFACTORY_REPO_URL} \
                  -PartifactorySnapshotRepoKey=${ARTIFACTORY_SNAPSHOT_REPO_KEY} \
                  -PartifactorySnapshotRepoUrl=${ARTIFACTORY_SNAPSHOT_REPO_URL} \
                  -PartifactoryUsername=${ARTIFACTORY_USERNAME} \
                  -PartifactoryPassword=${ARTIFACTORY_PASSWORD} \
                  -Ppackaging=${PACKAGING}
fi
