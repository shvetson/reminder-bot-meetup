FROM openjdk:17-slim
MAINTAINER shvetson@gmail.com
ADD build/libs/reminder-bot-meetup-0.0.1.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "./app.jar"]