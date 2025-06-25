# Build stage
FROM openjdk:11-jdk-slim AS build
WORKDIR /app
COPY . .
RUN ./gradlew build --no-daemon

# Package stage
FROM openjdk:11-jre-slim
WORKDIR /app
COPY --from=build /app/build/libs/ms-college-api-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8081
ENTRYPOINT ["java","-jar","app.jar"] 