# Book Store

## Description
This is a project for a Book Store application built with Spring Boot.

## Technologies Used
- Java 17
- Spring Boot 3.3.5
- Maven
- Docker
- Docker Compose
- Redis
- Thymeleaf
- jQuery
- Bootstrap

## Prerequisites
- Java 17
- Maven
- Docker
- Docker Compose

## Setup and Installation

1. **Clone the repository:**
   ```sh
   git clone https://github.com/nphuonha2101/book_store.git
   ```
   
2. **Navigate to the project directory:**
    ```sh
    cd book_store
    ```
3. **Copy the `application.properties` file:**
    ```sh
    cp src/main/resources/application.properties.example src/main/resources/application.properties
    ``` 
   
4. **Configure the application:**
   - Edit the `application.properties` file to set up your database connection and other configurations as needed.

5. **Build the project:**
    ```sh
    mvn clean install
    ```
6. **Run the application:**
    ```sh
    mvn spring-boot:run
    ```
7. **Access the application:**
   - Open your web browser and go to `http://localhost:8080`.

## Docker Setup (Alternative for running the application)
1. **Build the Docker image:**
   ```sh
   docker docker-compose build
   ```
2. **Run the Docker container:**
   ```sh
   docker-compose up -d
   ```
   
3. **Access the application:**
   - Open your web browser and go to `http://localhost:8080`.

# Authors
- Phuong Nha Nguyen
- Bich Phuong Lai Thi

# License
This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for details.