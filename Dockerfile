FROM amazoncorretto:17-alpine-jdk
COPY build/libs/event-app-0.0.1.jar app.jar
ENTRYPOINT ["java", "-jar","/app.jar"]