# Expense Tracker

A simple, production-ready full-stack application to record and review personal expenses.

## Tech Stack
- **Backend:** Java, Spring Boot, Spring Data JPA, H2 Database (in-memory)
- **Frontend:** React (TypeScript), Vite, Vanilla CSS

## How to Run

### Backend
1. Navigate to the `expensetracker` directory:
   ```bash
   cd expensetracker
   ```
2. Build and run the Spring Boot application:
   ```bash
   ./gradlew bootRun
   ```
   The backend will run on `http://localhost:8080`.

### Frontend
1. Navigate to the `frontend` directory in a new terminal:
   ```bash
   cd frontend
   ```
2. Install dependencies and start the development server:
   ```bash
   npm install
   npm run dev
   ```
   The frontend will run on `http://localhost:5173`.

---

## Design Decisions & Trade-offs

### 1. Money Handling
- **Decision:** The `amount` is stored and transmitted as an `Integer` representing the smallest currency unit (e.g., paise for INR or cents for USD).
- **Reasoning:** This prevents floating-point rounding errors that occur when doing math with decimals in JavaScript and Java. The frontend handles the conversion back to decimals for display.

### 2. Idempotency (Handling Retries)
- **Decision:** The frontend generates a unique UUID (`Idempotency-Key`) for every form submission. The backend checks this key and enforces a unique constraint (`@Column(unique = true)`).
- **Reasoning:** If a user clicks submit multiple times or the network drops and the browser retries the `POST` request, the backend will gracefully handle the constraint violation, preventing duplicate expenses from being created.

### 3. Styling
- **Decision:** Used Premium Vanilla CSS with CSS Variables.
- **Reasoning:** Meets the requirement for a simple yet premium aesthetic without adding the weight or build complexity of a full component library or TailwindCSS. 

### 4. Database
- **Decision:** H2 in-memory database with JPA.
- **Reasoning:** Fastest for iteration and local testing, but the use of Spring Data JPA means switching to PostgreSQL or MySQL in production is simply a matter of changing the JDBC URL and driver in `application.properties`.

## What Was Intentionally Left Out (Timebox Constraints)
- **Authentication:** Left out to keep the focus on the core requirements.
- **Pagination:** The list returns all expenses. In a real-world scenario with thousands of records, we would implement cursor or offset-based pagination.
- **Advanced Summaries:** A "total per category" view or charts were left out in favor of ensuring the core idempotency, filtering, and sorting were robust.
- **Comprehensive Testing:** Added basic architecture and component tests, but omitted exhaustive E2E tests (like Cypress/Playwright) to save time.
