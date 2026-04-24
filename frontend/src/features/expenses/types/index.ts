export interface Expense {
  id: string;
  userId: string;
  amount: number;
  category: string;
  description: string;
  date: string;
  createdAt: string;
}

export interface CreateExpenseDTO {
  amount: number;
  category: string;
  description?: string;
  date: string;
}
