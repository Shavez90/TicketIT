# TicketIT - Minimal README
[![Build Status](https://github.com/Shavez90/TicketIT/actions/workflows/ci.yml/badge.svg?branch=main)](https://github.com/Shavez90/TicketIT/actions)
A lightweight ticketing/helpdesk REST API built with Spring Boot & JWT authentication.

---

## **Features**

- User, Agent, and Admin roles
- JWT-based auth (provide Bearer token in `Authorization` header)
- Ticket create, assign, view, and status transitions
- Role-based access (USER: create/view own tickets, AGENT/ADMIN: view, assign, change status)
- Custom error handling

---

## **API Overview**

| Action                     | Method & Path                        | Role      |
|----------------------------|--------------------------------------|-----------|
| Create Ticket              | POST `/api/tickets`                  | USER      |
| List My Tickets            | GET `/api/tickets/my`                | USER      |
| List All Tickets           | GET `/api/tickets`                   | AGENT/ADMIN |
| Assign Ticket To Self      | PUT `/api/tickets/{id}/assign`       | AGENT/ADMIN |
| Change Ticket Status       | PUT `/api/tickets/{id}/status`       | AGENT/ADMIN |

- **Status Transitions:**  
  `OPEN → IN_PROGRESS → RESOLVED → CLOSED`  
  _(Each step must follow this order)_

---

## **Quick Start**

1. **Clone & Run:**
    ```sh
    git clone <repo>
    cd TicketIT
    ./mvnw spring-boot:run
    ```
2. **Set up Postman/HTTP client:**  
   All requests need JWT in `Authorization: Bearer <token>` header.
3. **Use endpoints above as per your role.**

---

## **Notes**

- Only AGENT/ADMIN can assign tickets or change ticket status.
- Only ticket's assigned user can change its status.
- Error responses are JSON:  
  `{ "error": "Description here" }`

---

## **License**

MIT