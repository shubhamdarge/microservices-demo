FROM eclipse-temurin:8-jre
WORKDIR /app
ARG JAR=target/user-service-0.0.1-SNAPSHOT.jar
COPY ${JAR} app.jar
EXPOSE 8081
ENTRYPOINT ["java","-jar","/app/app.jar"]