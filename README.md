# Incident Tracker

An internal service catalog and incident management tool, built as a 3-tier
application for hands-on DevOps/SRE practice.

## Architecture

```
React (frontend)  --->  Spring Boot (backend, REST API + JWT auth)  --->  PostgreSQL
```

- **Frontend**: React app - login, service catalog view, incident dashboard
- **Backend**: Spring Boot (Java 17, Maven) - REST API secured with JWT,
  exposes Actuator health/metrics endpoints for Kubernetes probes and Prometheus
- **Database**: PostgreSQL - stores users, services, incidents

## Domain Model

- **User**: username, password (BCrypt hashed), role
- **Service**: name, owner team, criticality tier (TIER-0/1/2), status (HEALTHY/DEGRADED/DOWN)
- **Incident**: linked to a service and a reporting user; severity (P1-P4),
  status (OPEN/INVESTIGATING/RESOLVED), description, timestamps

## API Endpoints

| Method | Endpoint                     | Auth required | Description                  |
|--------|------------------------------|----------------|------------------------------|
| POST   | /api/auth/register           | No             | Create a new user            |
| POST   | /api/auth/login               | No             | Login, returns JWT            |
| GET    | /api/services                 | Yes            | List services (filter by tier/status) |
| POST   | /api/services                 | Yes            | Create a service             |
| PATCH  | /api/services/{id}/status      | Yes            | Update service status        |
| GET    | /api/incidents                | Yes            | List incidents (filter by severity/status) |
| POST   | /api/incidents                | Yes            | Create an incident            |
| PATCH  | /api/incidents/{id}/resolve     | Yes            | Mark incident resolved       |
| GET    | /actuator/health               | No             | Health check (for K8s probes) |
| GET    | /actuator/prometheus           | No             | Prometheus metrics            |

## Configuration

All runtime config is environment-variable driven (12-factor app style):

| Variable          | Default                    | Description                  |
|--------------------|-----------------------------|-------------------------------|
| DB_HOST            | localhost                   | Postgres host                |
| DB_PORT            | 5432                        | Postgres port                |
| DB_NAME            | incident_tracker            | Database name                |
| DB_USERNAME        | postgres                    | Database user                |
| DB_PASSWORD        | postgres                    | Database password            |
| JWT_SECRET         | (dev default - override!)  | JWT signing secret           |
| JWT_EXPIRATION_MS  | 3600000 (1 hour)            | Token expiration              |
| SERVER_PORT        | 8080                        | Backend port                  |
| LOG_LEVEL          | INFO                        | App log level                 |

## Demo Credentials

Seeded automatically on first run (empty database):
- `admin` / `admin123` (ROLE_ADMIN)
- `rohit` / `rohit123` (ROLE_SRE)

## Running Locally (without Docker)

### Prerequisites
- Java 17+
- Maven 3.8+
- Node.js 18+
- PostgreSQL 14+ running locally

### Database setup
```bash
sudo -u postgres psql -c "CREATE DATABASE incident_tracker;"
```

### Backend
```bash
cd backend
mvn spring-boot:run
```
Runs on http://localhost:8080

### Frontend
```bash
cd frontend
npm install
npm start
```
Runs on http://localhost:3000 (proxies /api calls to :8080)

## Roadmap (DevOps phases to be applied to this app)

- [ ] Dockerize backend (multi-stage Maven build)
- [ ] Dockerize frontend (multi-stage Node build -> Nginx)
- [ ] docker-compose for local 3-tier orchestration
- [ ] Kubernetes manifests (Deployments, Services, ConfigMaps, Secrets, Ingress)
- [ ] CI/CD pipeline (GitHub Actions)
- [ ] Terraform for AWS infrastructure (EKS, RDS)
- [ ] Prometheus + Grafana observability
- [ ] SLO definition and incident simulation
