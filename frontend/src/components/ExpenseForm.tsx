import React, { useState } from 'react';
import { CreateExpenseRequest } from '../api';

interface ExpenseFormProps {
  onSubmit: (expense: CreateExpenseRequest, idempotencyKey: string) => Promise<void>;
  isLoading: boolean;
}

const CATEGORIES = ['Food', 'Transport', 'Utilities', 'Entertainment', 'Shopping', 'Other'];

export const ExpenseForm: React.FC<ExpenseFormProps> = ({ onSubmit, isLoading }) => {
  const [amount, setAmount] = useState('');
  const [category, setCategory] = useState(CATEGORIES[0]);
  const [description, setDescription] = useState('');
  const [date, setDate] = useState(new Date().toISOString().split('T')[0]);
  const [error, setError] = useState('');

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setError('');

    const parsedAmount = parseFloat(amount);
    if (isNaN(parsedAmount) || parsedAmount <= 0) {
      setError('Amount must be greater than 0');
      return;
    }

    // Convert decimal amount to integer (e.g., paise/cents)
    const amountInCents = Math.round(parsedAmount * 100);

    const idempotencyKey = crypto.randomUUID();

    try {
      await onSubmit({
        amount: amountInCents,
        category,
        description,
        date,
      }, idempotencyKey);
      
      // Reset form
      setAmount('');
      setDescription('');
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Failed to submit');
    }
  };

  return (
    <div className="card">
      <form onSubmit={handleSubmit}>
        <div className="flex-row">
          <div className="form-group flex-1">
            <label htmlFor="amount">Amount (₹)</label>
            <input
              type="number"
              id="amount"
              step="0.01"
              min="0.01"
              className="form-control"
              value={amount}
              onChange={(e) => setAmount(e.target.value)}
              required
              placeholder="0.00"
            />
          </div>
          
          <div className="form-group flex-1">
            <label htmlFor="category">Category</label>
            <select
              id="category"
              className="form-control"
              value={category}
              onChange={(e) => setCategory(e.target.value)}
              required
            >
              {CATEGORIES.map(cat => (
                <option key={cat} value={cat}>{cat}</option>
              ))}
            </select>
          </div>
        </div>

        <div className="flex-row">
          <div className="form-group flex-1">
            <label htmlFor="date">Date</label>
            <input
              type="date"
              id="date"
              className="form-control"
              value={date}
              onChange={(e) => setDate(e.target.value)}
              required
            />
          </div>
        </div>

        <div className="form-group">
          <label htmlFor="description">Description (Optional)</label>
          <input
            type="text"
            id="description"
            className="form-control"
            value={description}
            onChange={(e) => setDescription(e.target.value)}
            placeholder="What was this expense for?"
          />
        </div>

        {error && <div className="error-message">{error}</div>}

        <button type="submit" className="btn btn-primary" disabled={isLoading}>
          {isLoading ? 'Adding...' : 'Add Expense'}
        </button>
      </form>
    </div>
  );
};
