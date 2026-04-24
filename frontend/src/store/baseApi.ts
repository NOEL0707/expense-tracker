import { createApi, fetchBaseQuery } from '@reduxjs/toolkit/query/react';

const API_BASE_URL = 'https://expense-tracker-production-4587.up.railway.app/api/v1';

export const baseApi = createApi({
  reducerPath: 'api',
  baseQuery: fetchBaseQuery({
    baseUrl: API_BASE_URL,
    prepareHeaders: (headers) => {
      return headers;
    },
  }),
  tagTypes: ['User', 'Expense'],
  endpoints: () => ({}),
});
