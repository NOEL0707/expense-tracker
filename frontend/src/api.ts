export interface Expense {
  id: string;
  amount: number;
  category: string;
  description: string;
  date: string;
  createdAt: string;
}

export interface CreateExpenseRequest {
  amount: number;
  category: string;
  description?: string;
  date: string;
}

const API_BASE_URL = 'http://localhost:8080/api/expenses';

export const api = {
  getExpenses: async (category?: string, sort: string = 'date_desc'): Promise<Expense[]> => {
    const params = new URLSearchParams();
    if (category) params.append('category', category);
    params.append('sort', sort);

    const response = await fetch(`${API_BASE_URL}?${params.toString()}`);
    if (!response.ok) {
      throw new Error('Failed to fetch expenses');
    }
    return response.json();
  },

  createExpense: async (expense: CreateExpenseRequest, idempotencyKey: string): Promise<Expense> => {
    const response = await fetch(API_BASE_URL, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        'Idempotency-Key': idempotencyKey,
      },
      body: JSON.stringify(expense),
    });

    if (!response.ok) {
      throw new Error('Failed to create expense');
    }
    return response.json();
  }
};
