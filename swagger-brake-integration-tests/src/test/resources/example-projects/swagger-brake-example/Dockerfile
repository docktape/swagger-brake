FROM amazoncorretto:21-alpine-jdk

RUN env
WORKDIR swagger-brake-example
COPY . .
ENTRYPOINT ["sh", "mvnw", "clean", "deploy"]

