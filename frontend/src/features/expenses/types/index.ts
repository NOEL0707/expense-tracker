export interface Expense {
  id: string;
  userId: string;
  amount: number;
  category: string;
  description?: string;
  date: string;
  createdAt: string;
}

export interface CreateExpenseDTO {
  amount: number;
  category: string;
  description?: string;
  date: string;
}

export interface ExpensePageResponse {
  content: Expense[];
  page: number;
  size: number;
  totalElements: number;
  totalPages: number;
  first: boolean;
  last: boolean;
}
