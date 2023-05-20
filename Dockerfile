FROM openjdk:8-jdk
ADD target/Kezdesy-0.0.1-SNAPSHOT.jar Kezdesy.jar
EXPOSE 8089
ENTRYPOINT ["java", "-jar", "Kezdesy.jar"]