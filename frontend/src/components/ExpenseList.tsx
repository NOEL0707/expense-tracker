import React from 'react';
import { Expense } from '../api';

interface ExpenseListProps {
  expenses: Expense[];
  isLoading: boolean;
}

export const ExpenseList: React.FC<ExpenseListProps> = ({ expenses, isLoading }) => {
  if (isLoading) {
    return <div className="loading">Loading expenses...</div>;
  }

  if (expenses.length === 0) {
    return <div className="card" style={{ textAlign: 'center', color: 'var(--text-secondary)' }}>No expenses found.</div>;
  }

  // Format integer cents to formatted currency string
  const formatCurrency = (amountInCents: number) => {
    return new Intl.NumberFormat('en-IN', {
      style: 'currency',
      currency: 'INR'
    }).format(amountInCents / 100);
  };

  const formatDate = (dateString: string) => {
    return new Intl.DateTimeFormat('en-IN', {
      year: 'numeric',
      month: 'short',
      day: 'numeric'
    }).format(new Date(dateString));
  };

  return (
    <div className="expense-list">
      {expenses.map((expense) => (
        <div key={expense.id} className="expense-item">
          <div className="expense-info">
            <h3>{expense.description || expense.category}</h3>
            <div className="expense-meta">
              <span className="expense-category">{expense.category}</span>
              <span>{formatDate(expense.date)}</span>
            </div>
          </div>
          <div className="expense-amount">
            {formatCurrency(expense.amount)}
          </div>
        </div>
      ))}
    </div>
  );
};
