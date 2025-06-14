# Stage 1: Build JAR
FROM maven:3.9.6-eclipse-temurin-17 AS builder

LABEL authors="Phuong Nha Nguyen, Bich Phuong Lai Thi"

WORKDIR /app

COPY pom.xml .
COPY .mvn .mvn
COPY mvnw .
COPY src ./src

RUN chmod +x mvnw
RUN ./mvnw clean package -DskipTests

# Stage 2: Run JAR
FROM eclipse-temurin:17-jdk

WORKDIR /app

COPY --from=builder /app/target/*.jar app.jar

EXPOSE 8080

CMD ["java", "-jar", "app.jar"]
