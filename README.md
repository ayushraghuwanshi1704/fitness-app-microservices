This is the Official repository of **Java Spring Boot AI Full Stack Microservices Course: Building Fitness Application**

# The Ultimate Java and Spring Boot Mastery Roadmap

Welcome to your one-stop-shop for mastering Java and Spring Boot! This repository offers a comprehensive learning experience with high-quality resources and community support. Dive into over 150+ hours of premium content, with everything you need to excel at Java and Spring Boot development.

## 🎓 Learning Roadmap

Most of the courses below are available in **Udemy For Business**, so if you have subscription - you can get FREE access.
Here’s a structured path to enhance your skills with detailed courses available:

1. **[Spring Boot By Building Complex Projects Step by Step](https://link.embarkx.com/spring-boot) (85+ Hours of Content)**
2. **[Master Spring Boot Microservices by Building eCommerce Project](https://link.embarkx.com/microservices) (55+ Hours of Content)**
3. **[Learn Java with 60+ Hours of Content](http://link.embarkx.com/java) (60+ Hours of Content)**
4. **[Master Spring Security with React JS + OAuth2](https://link.embarkx.com/spring-security) (34+ Hours of Content)**
5. **[Master IntelliJ IDEA](http://link.embarkx.com/intellij) (3+ Hours of Content)**


## 🌟 With All Our Courses You Gain Access To

- 📝 **Notes:** Detailed and downloadable notes to accompany each lesson.
- 💻 **Source Code:** Full access to the source code used in the tutorials.
- 🤔 **Doubt Solving:** Responsive instructor and community support.
- 🎥 **High-Quality HD Videos:** Easy to understand, high-definition video tutorials.
- 🔄 **Free Lifetime Updates:** Continuous updates to course content at no extra cost.

## 📚 Why Choose This Mastery Series?

With this series, you're not just learning; you're preparing to dominate the field of Java and Spring Boot development. Our structured learning path ensures that you build your skills progressively, with each course designed to build on the knowledge gained from the previous one.

### Join Us Now!

Start your journey today to become a master at Java and Spring Boot. Our community and expert instructors are here to support your learning every step of the way. **Enroll and start building your future, today!**





# Usage Policy for Course Materials

## Instructor Information

**Instructor:** Faisal Memon  
**Company:** [EmbarkX.com](http://www.embarkx.com)

## Policy Overview

This document outlines the guidelines and restrictions concerning the use of course materials provided by EmbarkX, including but not limited to PDF presentations, code samples, and video tutorials.

### 1. Personal Use Only

The materials provided in this course are intended for **your personal use only**. They are to be used solely for the purpose of learning and completing this course.

### 2. No Unauthorized Sharing or Distribution

You are **not permitted** to share, distribute, or publicly post any course materials on any websites, social media platforms, or other public forums without prior written consent from the instructor.

### 3. Intellectual Property

All course materials are protected by copyright laws and are the intellectual property of Faisal Memon and EmbarkX. Unauthorized use, reproduction, or distribution of these materials is **strictly prohibited**.

### 4. Reporting Violations

If you become aware of any unauthorized sharing or distribution of course materials, please report it immediately to [embarkxofficial@gmail.com](mailto:embarkxofficial@gmail.com).

### 5. Legal Action

We reserve the right to take legal action against individuals or entities found to be violating this usage policy.

## Thank You

Thank you for respecting these guidelines and helping us maintain the integrity of our course materials.

## Contact Information

- **Email:** [embarkxofficial@gmail.com](mailto:embarkxofficial@gmail.com)
- **Website:** [www.embarkx.com](http://www.embarkx.com)


# Local application setup (Windows / IntelliJ IDEA)

## Runtime versions

Use JDK 17 for all six Spring Boot services. The POMs target Java 17 and Spring Boot 3.4.3 with Spring Cloud 2024.0.0. Node.js 22 and npm 10 are suitable for the Vite frontend. Each service has a Maven Wrapper; from PowerShell, use `mvnw.cmd` or IntelliJ's Maven tool window.

## Services and ports

| Component | Port | Notes |
|---|---:|---|
| Config Server | 8888 | Start first; native configs are in `configserver/src/main/resources/config` |
| Eureka | 8761 | Dashboard: http://localhost:8761 |
| User Service | 8081 | MySQL database `fitness_user_db_new` only |
| Activity Service | 8082 | Mongo database `fitnessactivity`; publishes to RabbitMQ |
| AI Service | 8083 | Mongo database `fitnessrecommendation`; consumes activity queue; Groq-backed |
| API Gateway | 8080 | Frontend API base is http://localhost:8080/api |
| React/Vite | 5173 | Start with `npm.cmd run dev` |
| Keycloak | 8181 | Realm `fitness-oauth2`, client `oauth2-pkce-client` (existing app defaults) |
| MySQL | 3306 | Local MySQL Server |
| MongoDB | 27017 | Local MongoDB required |
| RabbitMQ | 5672 | Local broker required; management UI is commonly 15672 |

## MySQL setup

The user service config points only at `fitness_user_db_new`; it never connects to `fitness_user_db`. Before first startup, create the new database and application account in MySQL Workbench using an administrator connection. Choose a unique local password and substitute it below:

```sql
CREATE DATABASE IF NOT EXISTS fitness_user_db_new;
CREATE USER IF NOT EXISTS 'fitness_app'@'localhost' IDENTIFIED BY '<choose-a-strong-local-password>';
GRANT SELECT, INSERT, UPDATE, DELETE, CREATE, ALTER, INDEX, REFERENCES
  ON fitness_user_db_new.* TO 'fitness_app'@'localhost';
```

In IntelliJ Run Configuration for User Service, set `USER_DB_USERNAME=fitness_app` and `USER_DB_PASSWORD` to the password you chose. Optional `USER_DB_URL` defaults to `jdbc:mysql://localhost:3306/fitness_user_db_new`. Hibernate `ddl-auto: update` can create/update tables only inside this new database. Never change the URL to the existing `fitness_user_db`.

## Other local configuration

Set these in IntelliJ Run Configurations (or user-level environment variables), not in committed source files:

- AI Service: `GROQ_API_KEY` (required for recommendations); optional `GROQ_MODEL` defaults to `openai/gpt-oss-20b`, and `GROQ_API_URL` defaults to `https://api.groq.com/openai/v1/chat/completions`.
- Activity Service and AI Service: `RABBITMQ_USERNAME` / `RABBITMQ_PASSWORD` (defaults are local RabbitMQ `guest` / `guest`).
- Activity Service: optional `ACTIVITY_MONGODB_URI` (defaults to `mongodb://localhost:27017/fitnessactivity`).
- AI Service: optional `AI_MONGODB_URI` (defaults to `mongodb://localhost:27017/fitnessrecommendation`).
- Gateway: optional `KEYCLOAK_JWK_SET_URI` (defaults to the local realm certificate URL).
- Frontend: optional `VITE_API_URL`, `VITE_KEYCLOAK_URL`, `VITE_KEYCLOAK_REALM`, `VITE_KEYCLOAK_CLIENT_ID`, and `VITE_REDIRECT_URI`; defaults match the existing local API and Keycloak setup.

Groq offers a documented free plan with model-specific request/token limits; it is not unlimited. Create an API key from the Groq console and configure it only in the AI Service Run Configuration. The service reports missing keys, timeouts, API errors, and HTTP 429 rate limits; it does not save invented recommendations.

## IntelliJ startup order

Open the repository folder containing the six service directories. Set Project SDK and each module's language level to JDK 17. Create a Spring Boot Run Configuration for each `*Application` main class. Add the relevant environment variables above to each run configuration. Start in this order:

1. Config Server (`configserver`)
2. Eureka (`eureka`)
3. User Service (`userservice`)
4. Activity Service (`activityservice`)
5. AI Service (`aiservice`)
6. API Gateway (`gateway`)
7. Frontend from the `fitness-app-frontend` terminal with `npm.cmd ci`, then `npm.cmd run dev`

MongoDB and RabbitMQ must be running before Activity and AI Services. MySQL must be running and the approved new database/account must exist before User Service. Keycloak must have the named realm/client and allow the frontend redirect URI `http://localhost:5173`; authentication remains enabled at the gateway.

## Build commands

Run `mvnw.cmd clean verify` in each Java service directory (the service wrappers share Maven 3.9.9). In the frontend directory run `npm.cmd ci`, `npm.cmd run build`, and optionally `npm.cmd run lint`. On this Windows setup, PowerShell blocks the `npm` PowerShell shim, so use `npm.cmd`.

## Common startup issues

- `Access denied` from MySQL: check the `fitness_app` account host is `localhost`, its grant is scoped to `fitness_user_db_new.*`, and the Run Configuration password matches. Do not solve this by using `fitness_user_db` or root.
- Config Server unavailable: start port 8888 before client services; inspect http://localhost:8888/user-service/default.
- Mongo/Rabbit connection refused: start the respective local services and confirm ports 27017/5672; set environment credentials if `guest` is not enabled.
- Login or API returns 401: check Keycloak URL, realm, client ID, redirect URI, and gateway JWK URL; do not disable JWT validation.
- Groq 401: replace the local `GROQ_API_KEY` with a valid key. HTTP 429 indicates the provider's current rate limit; wait for its reset.
