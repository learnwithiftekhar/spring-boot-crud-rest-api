ARG BASE_JRE_IMAGE=eclipse-temurin:21-jre-alpine
ARG BASE_JDK_IMAGE=eclipse-temurin:21-jdk-alpine
# --- Stage 1: Build the Application ---
FROM ${BASE_JDK_IMAGE} AS dependencies
RUN apk add --no-cache maven
WORKDIR /build
COPY pom.xml .
RUN mvn dependency:go-offline

# Stage:2
FROM dependencies AS builder
COPY src ./src
RUN mvn clean package -DskipTests

# --- Stage 3: Create the Runtime Image ---
# We use a smaller JRE image (Alpine) for production
FROM ${BASE_JRE_IMAGE} AS runtime
WORKDIR /app

ARG JAR_FILE=*.jar
COPY --from=builder /build/target/${JAR_FILE} app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]