FROM openjdk:21-jdk

RUN env
WORKDIR swagger-brake-example
COPY . .
ENTRYPOINT ["sh", "mvnw", "clean", "deploy"]

