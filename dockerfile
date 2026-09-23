FROM eclipse-temurin:21-jre
WORKDIR /app
COPY target/url-shortener.jar app.jar
EXPOSE 7000
ENTRYPOINT ["java", "-jar", "app.jar"]