FROM openjdk:8-jdk
ADD target/docker-container-matrix-1.0-SNAPSHOT.jar app.jar
CMD ["java", "-jar", "app.jar"]
