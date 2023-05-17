FROM openjdk:8-jdk
ADD target/*.jar Kezdesy.jar
EXPOSE 8089
ENTRYPOINT ["java", "-jar", "Kezdesy.jar"]