
# LogiMind AI – Logistics Platform

A Java Spring Boot backend project for managing shipment
operations through RESTful APIs, backed by Oracle AI
Database 26ai.

The project is under active development, with planned
AI-powered logistics features.

## Tech Stack

- Java 21+
- Spring Boot
- Spring MVC
- Spring Data JPA / Hibernate
- Oracle AI Database 26ai Free
- Maven
- JUnit 5
- Mockito
- Spring MockMvc
- JaCoCo

## Implemented Features

- Create and retrieve shipments
- Retrieve all shipments
- Update shipment status
- Delete shipments
- Validate shipment input
- Centralized API exception handling
- Automated service and controller tests
- JaCoCo code coverage reporting

## Architecture

Client
  |
  v
ShipmentController
  |
  v
ShipmentService
  |
  v
ShipmentRepository
  |
  v
Spring Data JPA / Hibernate
  |
  v
Oracle Database

## REST API Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | /api/shipments | Create shipment |
| GET | /api/shipments | List shipments |
| GET | /api/shipments/{id} | Find shipment |
| PUT | /api/shipments/{id}/status | Update status |
| DELETE | /api/shipments/{id} | Delete shipment |
| GET | /api/health | Health endpoint |

## Example: Create Shipment

POST /api/shipments

Request:

```json
{
  "trackingNumber": "LM1001",
  "origin": "Virginia",
  "destination": "New York",
  "status": "CREATED"
}
```

Supported shipment statuses:

- CREATED
- IN_TRANSIT
- DELIVERED
- CANCELLED

## Local Setup

### Requirements

- JDK 21 or newer (project currently uses Java 24)
- Oracle AI Database 26ai Free
- Git
- PowerShell on Windows

### Clone Repository

```bash
git clone https://github.com/manishshaik25-sudo/logimind-ai-logistics-platform.git
cd logimind-ai-logistics-platform/backend
```

### Configure Oracle

Create an Oracle user/schema named LOGIMIND
with appropriate development permissions.

Update the datasource settings in:

`src/main/resources/application.properties`

The local connection uses:

```properties
spring.datasource.url=jdbc:oracle:thin:@//localhost:1521/FREEPDB1
spring.datasource.username=logimind
spring.datasource.password=${DB_PASSWORD}
```

Set the database password in PowerShell:

```powershell
$env:DB_PASSWORD = Read-Host "Enter Oracle password"
```

### Run Application

```powershell
.\mvnw.cmd spring-boot:run
```

API base URL:

`http://localhost:8080/api/shipments`

## Testing

Run all tests:

```powershell
.\mvnw.cmd test
```

Generate the JaCoCo coverage report:

```powershell
.\mvnw.cmd clean verify
```

Open the report:

```powershell
Start-Process ".\target\site\jacoco\index.html"
```

### Latest Measured Coverage

- Overall instruction coverage: 77%
- Controller instruction coverage: 91%
- Service instruction coverage: 90%

These figures represent a development snapshot and may
change as new features and tests are added.

## Planned Enhancements

- Shipment status history and tracking events
- Improved shipment lifecycle management
- Request/response DTOs
- Additional integration tests
- AI-powered shipment delay prediction
- Intelligent logistics risk analysis

AI-powered features are planned and are not yet implemented.

## Project Status

Active development.
