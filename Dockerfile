FROM gradle:7.6-jdk17 AS builder

WORKDIR /build

COPY build.gradle settings.gradle /build/

RUN gradle build -x test --parallel --continue > /dev/null 2>&1 || true

COPY src /build/src

RUN gradle build -x test --parallel

FROM eclipse-temurin:17-jre

WORKDIR /app

COPY --from=builder /build/build/libs/*SNAPSHOT.jar app.jar

ENV TZ=Asia/Seoul

ENTRYPOINT ["java", "-jar", "app.jar"]