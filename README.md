# ✈️ Flight Ticket Reservation System

A full-stack **Flight Ticket Reservation System** designed with a modern, scalable architecture.  
The system supports airline management, flight creation, airport/city data ingestion, ticket reservations, and secure user authentication.

---

## 🧩 System Architecture Overview

The system is composed of the following core components:


---

## 🧱 Components

### 1️⃣ Backend – **Quarkus**

The backend is built using **Quarkus** Java Framework.

#### Responsibilities:
- Exposes REST APIs consumed by the frontend
- Business logic for:
  - Aircrafts
  - Airlines
  - Flights
  - Users
  - Airports
  - Cities & Countries
  - Ticket reservations
- Integrates with:
  - **MySQL** for persistent storage
  - **Keycloak** for authentication & authorization
  - **FTP Server** for CSV data ingestion

#### Key Technologies:
- Java 21
- Quarkus(v3.20)
- Hibernate ORM with Panache
- RESTEasy Reactive (quarkus-rest, quarkus-rest-jackson, quarkus-rest-jsonb)
- Bean Validation (Hibernate Validator)
- JWT / OIDC Security
- WebSockets (Quarkus WebSockets) – real-time seat reservation updates
- MapStruct (DTO ↔ Entity mapping)
- Apache Camel (FTP Integration)

#### Database Integration:
- Uses **MySQL** in production
- Uses **H2 (in-memory)** for integration tests

### Testing & Tooling:
- Quarkus JUnit 5
- RestAssured (REST API testing)
- Quarkus Test Security (@TestSecurity)
- Mockito (inline + Quarkus integration)
- H2 in-memory database
- JaCoCo (code coverage)

---

### 2️⃣ Frontend – **Angular**

The frontend is developed using **Angular** and provides a responsive web interface for end users and administrators.

#### Responsibilities:
- User authentication via Keycloak
- Flight search and ticket reservation
- Airline and flight management (admin)
- Display airports, cities, and routes
- Communicates exclusively with backend via:
    - REST APIs
    - WebSocket connections for live updates

#### Key Technologies:
- Angular 19
- TypeScript 5
- Angular Router (client-side navigation)
- RxJS (reactive state & async data handling)
- Angular Forms (template-driven & reactive forms)
- Angular Material (UI components)
- Angular CDK (accessibility & UI utilities)
- Angular Animations
- HTTP Client (Angular HttpClient)
- Keycloak Integration (keycloak-js, keycloak-angular)

---

### 3️⃣ Authentication & Authorization – **Keycloak**

**Keycloak** is used as the centralized identity and access management solution.

#### Features:
- User authentication (login / logout)
- Role-based access control (RBAC)
- JWT token issuance
- Integration with Quarkus via OIDC

#### Roles:
- `System Administrator`
- `Airline Administrator`
- `Passenger`

#### Flow:
1. User logs in via Keycloak
2. Angular receives access token
3. Token is sent to backend
4. Quarkus validates token & roles

---

### 4️⃣ FTP Server (Dockerized)

A lightweight **FTP Server** runs inside a Docker container.

#### Purpose:
- Stores CSV files for:
  - Cities
  - Countries
- Backend fetches these files periodically
- Data is parsed and persisted into the database

## 🏃‍♂️ Instructions to Run the System

Follow these steps to run the full system locally.

---

### 1. Start the FTP Server

The system expects an SFTP server to provide CSV files for cities and countries.

1. Open a terminal and navigate to the `/ftp` folder.
2. Use the following Docker Compose configuration:

```yaml
services:
  sftp:
    image: atmoz/sftp
    container_name: sftp-server
    ports:
      - "2222:22"
    volumes:
      - /home/airport/uploads:/home/admin/uploads
    environment:
      - SFTP_USERS=admin:password:1001
```
3. Start the container:
```
docker compose up -d
```
### 2. Start Keycloak
1. Open a terminal and navigate to the `/ftp` folder.
2. Use the following Docker Compose configuration:

```yaml
services:
  keycloak-airport:
    image: quay.io/keycloak/keycloak:24.0.5
    container_name: keycloak-airport
    ports:
      - "8080:8080"
    environment:
      KEYCLOAK_ADMIN: admin
      KEYCLOAK_ADMIN_PASSWORD: admin
      KC_FEATURES: scripts
    volumes:
      - ./import:/opt/keycloak/data/import
    command: >
      start-dev
      --hostname=localhost
      --import-realm
```
3. Start the container:
```
docker compose up -d
```
4. Access the Keycloak admin console at http://localhost:8080 with credentials:
    - Username: admin
    - Password: admin

### 3. Start the Backend(Quarkus)

1. Navigate to the airport_project project folder.
2. Ensure the MySQL database is running and matches your application.properties configuration.
3. Run the Quarkus application in dev mode:
```
mvn quarkus:dev
```

### 4. Start the Frontend(Angular)

1. Navigate to the airport_frontend project folder.
2. Install dependencies:
```
npm install
```
3. Start the Angular development server:
```
ng serve
```

### 5. Notes & Recommendations
- Ensure the FTP server and Keycloak are running before starting the backend.
- Enusere a MySQL Database runs on the hosting machine before starting the backend.
- The backend will automatically fetch and persist CSV data from the FTP server on first startup.
- For testing purposes, the backend uses an H2 in-memory database.
- WebSocket-based ticket reservations require both frontend and backend running simultaneously.
