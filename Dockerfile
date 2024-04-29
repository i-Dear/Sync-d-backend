FROM ubuntu:latest as base
WORKDIR /app

RUN apt-get update && \
    apt-get install -y openjdk-17-jdk-headless gradle && \
    apt-get clean

FROM base as dependencies
COPY gradlew .
COPY gradle/ gradle/
COPY build.gradle .
COPY settings.gradle .

RUN chmod +x ./gradlew && ./gradlew --no-daemon dependencies

FROM dependencies as builder
COPY src src

RUN ./gradlew --no-daemon build

FROM openjdk:17
WORKDIR /app

ARG SPRING_DATA_MONGODB_URI
ARG SPRING_DATA_MONGODB_DATABASE
ARG SPRING_SECURITY_AUTH_LIVEBLOCKSECRETKEY
ARG GOOGLE_CLIENT_ID
ARG GOOGLE_CLIENT_SECRET

ENV SPRING_DATA_MONGODB_URI=$SPRING_DATA_MONGODB_URI
ENV SPRING_DATA_MONGODB_DATABASE=$SPRING_DATA_MONGODB_DATABASE
ENV SPRING_SECURITY_AUTH_LIVEBLOCKSECRETKEY=$SPRING_SECURITY_AUTH_LIVEBLOCKSECRETKEY
ENV GOOGLE_CLIENT_ID=$GOOGLE_CLIENT_ID
ENV GOOGLE_CLIENT_SECRET=$GOOGLE_CLIENT_SECRET

# Copy the built jar file
COPY --from=builder /app/build/libs/*.jar app.jar

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
