FROM eclipse-temurin:8-jre
WORKDIR /app
ARG JAR=target/order-service-0.0.1-SNAPSHOT.jar
COPY ${JAR} app.jar
EXPOSE 8082
ENTRYPOINT ["java","-jar","/app/app.jar"]