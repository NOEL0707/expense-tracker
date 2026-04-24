import React, { useEffect, useState } from 'react';
import { api, Expense, CreateExpenseRequest } from './api';
import { ExpenseForm } from './components/ExpenseForm';
import { ExpenseList } from './components/ExpenseList';

function App() {
  const [expenses, setExpenses] = useState<Expense[]>([]);
  const [isLoading, setIsLoading] = useState(true);
  const [isSubmitting, setIsSubmitting] = useState(false);
  const [filterCategory, setFilterCategory] = useState('');
  const [sortOrder, setSortOrder] = useState('date_desc');
  const [error, setError] = useState('');

  const fetchExpenses = async () => {
    try {
      setIsLoading(true);
      setError('');
      const data = await api.getExpenses(filterCategory || undefined, sortOrder);
      setExpenses(data);
    } catch (err) {
      setError('Failed to load expenses. Please check your connection.');
    } finally {
      setIsLoading(false);
    }
  };

  useEffect(() => {
    fetchExpenses();
  }, [filterCategory, sortOrder]);

  const handleAddExpense = async (expenseData: CreateExpenseRequest, idempotencyKey: string) => {
    try {
      setIsSubmitting(true);
      await api.createExpense(expenseData, idempotencyKey);
      await fetchExpenses(); // Refresh the list
    } finally {
      setIsSubmitting(false);
    }
  };

  const totalAmount = expenses.reduce((sum, exp) => sum + exp.amount, 0);
  const formattedTotal = new Intl.NumberFormat('en-IN', {
    style: 'currency',
    currency: 'INR'
  }).format(totalAmount / 100);

  return (
    <div className="container">
      <header className="header">
        <h1>Expense Tracker</h1>
        <p>Keep track of your spending</p>
      </header>

      <main>
        <ExpenseForm onSubmit={handleAddExpense} isLoading={isSubmitting} />

        <div className="card">
          <div className="filters">
            <div className="filters-group">
              <select 
                className="form-control" 
                value={filterCategory} 
                onChange={(e) => setFilterCategory(e.target.value)}
                style={{ width: 'auto' }}
              >
                <option value="">All Categories</option>
                <option value="Food">Food</option>
                <option value="Transport">Transport</option>
                <option value="Utilities">Utilities</option>
                <option value="Entertainment">Entertainment</option>
                <option value="Shopping">Shopping</option>
                <option value="Other">Other</option>
              </select>

              <select 
                className="form-control" 
                value={sortOrder} 
                onChange={(e) => setSortOrder(e.target.value)}
                style={{ width: 'auto' }}
              >
                <option value="date_desc">Newest First</option>
                <option value="date_asc">Oldest First</option>
              </select>
            </div>
            <div className="total-summary">
              Total: {formattedTotal}
            </div>
          </div>

          {error && <div className="error-message" style={{ marginBottom: '1rem' }}>{error}</div>}

          <ExpenseList expenses={expenses} isLoading={isLoading} />
        </div>
      </main>
    </div>
  );
}

export default App;
