FROM eclipse-temurin:17-jdk

LABEL authors="Phuong Nha Nguyen, Bich Phuong Lai Thi"

WORKDIR /app

# Copy Maven wrapper and configuration
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .

# Copy source code
COPY src ./src

RUN chmod +x ./mvnw

# Install dependencies
RUN ./mvnw dependency:resolve

# Build the application
CMD ["./mvnw", "spring-boot:run"]