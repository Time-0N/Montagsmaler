# Keycloak Dev Environment Setup

In the development environment, the Keycloak client named `dev` requires its **service account** to have proper permissions in order to function correctly with the admin REST API.

## ✅ Required Role

Assign the following role to the `dev` client’s service account:

- Realm: `realm-management`
- Role: `realm-admin`

## 📍 Steps (Keycloak Admin Console)

1. Go to **Clients** → `dev`
2. Click on **Service Account Roles**
3. Select **Client Roles** → `realm-management`
4. Assign the **`realm-admin`** role

## ❗ Why it’s needed

Without this role, calls to the admin REST API (e.g., creating users) will return a `403 Forbidden` error, and the `Location` header will be missing, breaking the user registration flow.
