FROM openjdk:17

COPY ./build/libs /app

WORKDIR /app

CMD ["java", "-Duser.timezone=Asia/Seoul", "-jar", "interviewbank-0.0.1-SNAPSHOT.jar"]
