import { baseApi } from "@/store/baseApi";

import type { CreateExpenseDTO, Expense, ExpensePageResponse } from "../types";

type GetExpensesParams = {
  userId: string;
  category?: string;
  sort?: string;
  page?: number;
  size?: number;
};

export const expensesApi = baseApi.injectEndpoints({
  endpoints: (builder) => ({
    getExpenses: builder.query<ExpensePageResponse, GetExpensesParams>({
      query: ({ userId, category, sort = "date_desc", page = 0, size = 10 }) => {
        const params = new URLSearchParams();
        if (category) params.append('category', category);
        params.append("sort", sort);
        params.append("page", String(page));
        params.append("size", String(size));
        return {
          url: `/expenses?${params.toString()}`,
          headers: {
            "X-User-Id": userId,
          },
        };
      },
      providesTags: (result) =>
        result
          ? [
              ...result.content.map((expense) => ({
                type: "Expense" as const,
                id: expense.id,
              })),
              { type: "Expense" as const, id: "LIST" },
            ]
          : [{ type: "Expense" as const, id: "LIST" }],
    }),
    createExpense: builder.mutation<
      Expense,
      { userId: string; expense: CreateExpenseDTO; idempotencyKey?: string }
    >({
      query: ({ userId, expense, idempotencyKey }) => {
        const headers: Record<string, string> = {
          "X-User-Id": userId,
        };
        if (idempotencyKey) {
          headers["Idempotency-Key"] = idempotencyKey;
        }
        return {
          url: "/expenses",
          method: "POST",
          headers,
          body: expense,
        };
      },
      invalidatesTags: [{ type: "Expense", id: "LIST" }],
    }),
    updateExpense: builder.mutation<
      Expense,
      { userId: string; expenseId: string; expense: CreateExpenseDTO }
    >({
      query: ({ userId, expenseId, expense }) => ({
        url: `/expenses/${expenseId}`,
        method: "PUT",
        headers: {
          "X-User-Id": userId,
        },
        body: expense,
      }),
      invalidatesTags: (_result, _error, { expenseId }) => [
        { type: "Expense", id: expenseId },
        { type: "Expense", id: "LIST" },
      ],
    }),
    deleteExpense: builder.mutation<void, { userId: string; expenseId: string }>({
      query: ({ userId, expenseId }) => ({
        url: `/expenses/${expenseId}`,
        method: "DELETE",
        headers: {
          "X-User-Id": userId,
        },
      }),
      invalidatesTags: (_result, _error, { expenseId }) => [
        { type: "Expense", id: expenseId },
        { type: "Expense", id: "LIST" },
      ],
    }),
  }),
});

export const {
  useGetExpensesQuery,
  useCreateExpenseMutation,
  useUpdateExpenseMutation,
  useDeleteExpenseMutation,
} = expensesApi;
