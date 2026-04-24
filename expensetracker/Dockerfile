FROM eclipse-temurin:21-jdk-jammy AS builder

WORKDIR /workspace

COPY gradlew gradlew
COPY gradlew.bat gradlew.bat
COPY build.gradle settings.gradle ./
COPY gradle gradle
COPY src src

RUN chmod +x gradlew && ./gradlew --no-daemon clean bootJar -x test

FROM eclipse-temurin:21-jre-jammy

ENV APP_HOME=/app
ENV SPRING_PROFILES_ACTIVE=prod

WORKDIR ${APP_HOME}

RUN groupadd --system spring \
    && useradd --system --gid spring --home-dir ${APP_HOME} --create-home spring \
    && mkdir -p ${APP_HOME}/data \
    && chown -R spring:spring ${APP_HOME}

COPY --from=builder /workspace/build/libs/*.jar /app/app.jar

USER spring:spring

EXPOSE 8080

ENTRYPOINT ["java","-XX:MaxRAMPercentage=75.0","-XX:+UseG1GC","-Djava.security.egd=file:/dev/./urandom","-jar","/app/app.jar"]
