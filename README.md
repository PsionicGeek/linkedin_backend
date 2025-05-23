# LinkedIn Backend Microservices

This repository contains a **LinkedIn Backend** project implemented as a microservices architecture. It includes multiple services to handle core functionalities such as user management, connections, posts, and API gateway for routing and discovery.

## Project Structure


### 1. **API Gateway**
   The entry point for the microservices. Handles routing and acts as a gateway for various service requests. Built using **Spring Cloud Gateway**.

   **Key Dependencies:**
   - `spring-cloud-starter-gateway`
   - `spring-cloud-starter-netflix-eureka-client`

### 2. **Connection Service**
   Manages user connections and the social networking graph.

### 3. **Discovery Server**
   Implements a service registry using **Netflix Eureka** to allow dynamic discovery of the microservices.

### 4. **Post Service**
   Handles user posts, activities, and feed-related functionalities.

### 5. **User Service**
   Manages user registration, authentication, and profiles.

---

## Tech Stack

- **Java 21**
- **Spring Boot 3.4.5**
- **Spring Cloud 2024.0.1**
- **Netflix Eureka** (Service Registry and Discovery)
- **Spring Cloud Gateway** (API Gateway)
- **Lombok** (To reduce boilerplate code)
- **Spring Data JPA** (For database interactions)
- **H2 Database** (In-memory database for development and testing)
- **PostgreSQL** (For production)
- **Neo4j** (Graph database for connection service)

---

## Prerequisites

1. **Java Development Kit** - Version 21 or later.
2. **Maven** - Version 3.6 or later.
3. **Eureka Discovery Server** should be running for services to register and communicate.
4. All microservices should be configured to point to the same `discovery-server`.

---

## Getting Started

### 1. Clone the Repository
```bash
git clone https://github.com/your-username/linkedin-backend.git](https://github.com/PsionicGeek/linkedin-backend.git
cd linkedin-backend
````


### 2. Build the Project
Run the following Maven command to build all services:
```bash
mvn clean install
```
### 3. Start the Discovery Server
Navigate to the `discovery-server` directory and run:

### 4. Start the API Gateway
Navigate to the `api-gateway` directory and run:

### 5. Start Other Services
Navigate to each service directory (e.g., `user-service`, `post-service`, `connection-service`) and run:

### 6. Access the API Gateway
The API Gateway will be available at `http://localhost:8080`. You can access the various services through the gateway.

### 9. Swagger Documentation
Each service has Swagger documentation enabled. You can access the Swagger UI for each service at the following URLs:
- User Service: `http://localhost:9020/swagger-ui.html`
- Post Service: `http://localhost:9010/swagger-ui.html`
- Connection Service: `http://localhost:9030/swagger-ui.html`
- API Gateway: `http://localhost:8080/swagger-ui.html`
- Discovery Server: `http://localhost:8761/swagger-ui.html`
### 10. Testing
Each service has unit and integration tests. You can run the tests using:
```bash
mvn test
```

