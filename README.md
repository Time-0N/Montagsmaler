# 🛠️ Dev Environment Setup for Montagsmaler

This guide will help you set up the development environment for the **Montagsmaler** project, including **PostgreSQL**, **Keycloak**, and **Spring Boot** configuration.

---

## 🐘 Start the PostgreSQL and Keycloak Services

Use the two `.sh` scripts located in `Montagsmaler/dev/start`:

./start-postgres.sh  
./start-keycloak.sh

These scripts will automatically start and initialize the services.
> ⚠️ **Requirement:** Docker must be installed on your machine.

---

# 🧬 OpenAPI Code Generation

The Montagsmaler project uses [OpenAPI Generator](https://openapi-generator.tech/) to automatically generate API client and server code from a shared OpenAPI specification file.

## 🧪 Backend Code Generation (Spring Boot)

Generates Java interfaces and models for the REST API on the backend.

./gradlew backend:openApiGenerate

- 📄 Uses: `docs/openapi/openapi.yaml`
- 📁 Outputs to: `backend/src/main/java/com/example/rest/generated`

---

## 🧩 Frontend Code Generation (Angular)

Generates TypeScript services and models to communicate with the backend API.

./gradlew frontend:generateFrontendApi

- 📄 Uses: `docs/openapi/openapi.yaml`
- 📁 Outputs to: `frontend/src/app/api`

---

✅ Run both generation tasks at once with:

./gradlew backend:openApiGenerate frontend:generateFrontendApi

> This keeps backend and frontend code in sync with the OpenAPI spec.

---

## 🔐 Import the Keycloak Realm

1. Open your browser and go to: [http://localhost:8080](http://localhost:8080)
2. Log in with the following credentials:
   - **Username:** `admin`
   - **Password:** `admin`
3. In the Keycloak Admin Console:
   - Click **Create Realm**
   - Choose **Import Realm**
   - Select the file: `dev-realm.json` (located in `Montagsmaler/devRessources`)

---

### ⚠️ Important: Enable Username Editing in Keycloak

If you want the **username to be editable** by users, you must enable this feature in Keycloak:

- Go to your Realm in the Keycloak Admin Console
- Navigate to **Realm Settings** → **User Info** tab
- Enable the toggle for **Edit username**

If this option is **not enabled**, users will **not be able to change their usernames** via the Keycloak UI or API.

---

# 🧾 Keycloak Service Account Configuration

In the development environment, the Keycloak client named `dev` requires its **service account** to have proper permissions to interact with the admin REST API.

## ✅ Required Role

Assign the following role to the `dev` client’s service account:

- **Realm:** `realm-management`
- **Role:** `realm-admin`

## 📍 How to Assign the Role (Keycloak Admin Console)

1. Navigate to **Clients** → `dev`
2. Click on **Service Account Roles**
3. Select **Client Roles** → `realm-management`
4. Assign the **`realm-admin`** role

## ❗ Why This Is Important

Without this role:
- API calls such as user creation will return a **`403 Forbidden`** error
- The `Location` header will be missing in responses
- This breaks the user registration flow

---

# 🔐 Login Credentials

## Keycloak

- **Username:** `admin`
- **Password:** `admin`
- **URL:** [http://localhost:8080](http://localhost:8080)

## PostgreSQL

- **Username:** `devuser`
- **Password:** `devpass`
- **URL:** [http://localhost:5432](http://localhost:5432)

---

# 🚀 Starting the Spring Boot Application

To launch the backend using the development profile, run the following command from the root of the project:

./gradlew bootRun --args='--spring.profiles.active=dev'

This will start the application with the correct configuration for the dev environment.

---

# 📘 Backend API Documentation

## 🔎 Swagger UI

API documentation is available at:  
**[http://localhost:9090/swagger-ui/index.html#/](http://localhost:9090/swagger-ui/index.html#/)**
  
---

## 👤 User Login / Registration

### 🔧 Registration Endpoint

**URL:** `http://localhost:9090/api/auth/register`  
**Method:** `POST`  
**Expected JSON Body:**

{
"username": "string",
"email": "email@example.com",
"password": "string",
"firstName": "string",
"lastName": "string"
}

---

### 🔐 Login Endpoint

**URL:** `http://localhost:9090/api/auth/token`  
**Method:** `POST`  
**Expected JSON Body:**

{
"username": "string",
"password": "string"
}

Both endpoints return a **JWT Bearer Token** that must be included in authenticated requests using the `Authorization` header:

Authorization: Bearer <your_token_here>

---

## 🛡️ Authority

### Roles

- `ADMIN`
- `USER`

### Role-Protected Endpoints

- `GET /api/user/me` → requires **USER**
- `GET /api/get/all` → requires **ADMIN**

---

# 🗃️ Data Model

### 🧍 User Entity (Simplified Structure)

| Field                  | Type     | Constraints                    | Description                            |
|------------------------|----------|-------------------------------|--------------------------------------|
| `id`                   | UUID     | Primary Key (auto-generated)  | Unique identifier                    |
| `username`             | String   | Unique, Not Null              | Chosen username                      |
| `email`                | String   | Unique, Not Null              | User's email                        |
| `firstName`            | String   | —                             | First name                          |
| `lastName`             | String   | —                             | Last name                          |
| `gameWebSocketSessionId` | String | —                             | WebSocket session ID for in-game use |
| `aboutMe`              | String   | Large Object (Lob)            | User bio or description             |
| `keycloakId`           | String   | Not Null                      | Link to Keycloak user               |

> ℹ️ The entity is annotated with `@Entity` and mapped to the table `users`. UUIDs are used as the primary key and generated via `@UuidGenerator`.

---

### 🎮 GameSession (In-Memory DAO)

| Field               | Type                   | Description                                         |
|---------------------|------------------------|---------------------------------------------------|
| `roomId`            | String                 | Unique ID representing the game room              |
| `users`             | List\<User\>           | List of users in the session                       |
| `readyStatus`       | Map\<UUID, Boolean\>   | Mapping of user IDs to their ready state           |
| `submittedWords`    | Map\<UUID, String\>    | Words submitted by users (UUID → word)             |
| `drawingOrder`      | List\<User\>           | Order in which users will draw                      |
| `currentDrawerIndex`| int                    | Index of the current drawer in the drawing order   |
| `phase`             | GamePhase              | Current game phase (e.g. LOBBY, DRAWING, etc.)     |
| `currentWord`       | String                 | Word currently being drawn                          |
| `currentDrawer`     | User                   | The user currently drawing                          |
| `timerSeconds`      | int                    | Remaining time for the current round                |

> 🧠 `GameSession` is a plain in-memory data structure (not persisted in the database) used to manage game state per room. It's initialized with a `roomId` and maintains the entire round lifecycle.
