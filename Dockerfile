FROM openjdk:17-jdk-slim as base
WORKDIR /app
RUN apt-get update && apt-get install -y gradle && rm -rf /var/lib/apt/lists/*

COPY gradlew .
COPY gradle/ gradle/
COPY build.gradle .
COPY settings.gradle .
RUN chmod +x ./gradlew
RUN ./gradlew --no-daemon dependencies

COPY src src
RUN ./gradlew --no-daemon build

FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=base /app/build/libs/*.jar app.jar
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
