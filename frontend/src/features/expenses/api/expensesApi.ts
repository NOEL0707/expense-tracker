import { baseApi } from '@/store/baseApi';
import type { Expense, CreateExpenseDTO } from '../types';

export const expensesApi = baseApi.injectEndpoints({
  endpoints: (builder) => ({
    getExpenses: builder.query<Expense[], { userId: string; category?: string; sort?: string }>({
      query: ({ userId, category, sort = 'date_desc' }) => {
        const params = new URLSearchParams();
        if (category) params.append('category', category);
        params.append('sort', sort);
        return {
          url: `/expenses?${params.toString()}`,
          headers: {
            'X-User-Id': userId,
          },
        };
      },
      providesTags: ['Expense'],
    }),
    createExpense: builder.mutation<Expense, { userId: string; expense: CreateExpenseDTO; idempotencyKey?: string }>({
      query: ({ userId, expense, idempotencyKey }) => {
        const headers: Record<string, string> = {
          'X-User-Id': userId,
        };
        if (idempotencyKey) {
          headers['Idempotency-Key'] = idempotencyKey;
        }
        return {
          url: '/expenses',
          method: 'POST',
          headers,
          body: expense,
        };
      },
      invalidatesTags: ['Expense'],
    }),
    updateExpense: builder.mutation<Expense, { userId: string; expenseId: string; expense: CreateExpenseDTO }>({
      query: ({ userId, expenseId, expense }) => ({
        url: `/expenses/${expenseId}`,
        method: 'PUT',
        headers: {
          'X-User-Id': userId,
        },
        body: expense,
      }),
      invalidatesTags: ['Expense'],
    }),
    deleteExpense: builder.mutation<void, { userId: string; expenseId: string }>({
      query: ({ userId, expenseId }) => ({
        url: `/expenses/${expenseId}`,
        method: 'DELETE',
        headers: {
          'X-User-Id': userId,
        },
      }),
      invalidatesTags: ['Expense'],
    }),
  }),
});

export const {
  useGetExpensesQuery,
  useCreateExpenseMutation,
  useUpdateExpenseMutation,
  useDeleteExpenseMutation,
} = expensesApi;
