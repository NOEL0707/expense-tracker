const PROD_API_BASE_URL =
  "https://expense-tracker-production-4587.up.railway.app/api/v1";
const DEV_API_BASE_URL = "http://localhost:8080/api/v1";

export const API_BASE_URL =
  import.meta.env.VITE_API_BASE_URL?.replace(/\/$/, "") ??
  (import.meta.env.DEV ? DEV_API_BASE_URL : PROD_API_BASE_URL);
