import React, { useState } from 'react';
import type { CreateExpenseDTO } from '../types';
import { Button } from '@/components/ui/button';
import { Input } from '@/components/ui/input';
import { Label } from '@/components/ui/label';
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from '@/components/ui/select';
import { Card, CardContent, CardHeader, CardTitle, CardDescription } from '@/components/ui/card';

interface ExpenseFormProps {
  onSubmit: (expense: CreateExpenseDTO, idempotencyKey: string) => Promise<void>;
  isLoading: boolean;
  disabled?: boolean;
}

const CATEGORIES = ['Food', 'Transport', 'Utilities', 'Entertainment', 'Shopping', 'Other'];

export const ExpenseForm: React.FC<ExpenseFormProps> = ({ onSubmit, isLoading, disabled }) => {
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

    const amountInCents = Math.round(parsedAmount * 100);
    const idempotencyKey = crypto.randomUUID();

    try {
      await onSubmit({
        amount: amountInCents,
        category,
        description,
        date,
      }, idempotencyKey);
      
      setAmount('');
      setDescription('');
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Failed to submit expense');
    }
  };

  return (
    <Card className="mb-8 border-primary/20">
      <CardHeader className="bg-muted/30">
        <CardTitle>Add New Expense</CardTitle>
        <CardDescription>Track a new transaction.</CardDescription>
      </CardHeader>
      <CardContent>
        <form onSubmit={handleSubmit} className="space-y-6">
          <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
            <div className="space-y-2">
              <Label htmlFor="amount">Amount (₹)</Label>
              <Input
                type="number"
                id="amount"
                step="0.01"
                min="0.01"
                value={amount}
                onChange={(e) => setAmount(e.target.value)}
                required
                placeholder="0.00"
                disabled={disabled || isLoading}
              />
            </div>
            
            <div className="space-y-2">
              <Label htmlFor="category">Category</Label>
              <Select 
                value={category} 
                onValueChange={setCategory} 
                disabled={disabled || isLoading}
              >
                <SelectTrigger className="w-full md:min-w-[240px]">
                  <SelectValue placeholder="Select a category" />
                </SelectTrigger>
                <SelectContent>
                  {CATEGORIES.map(cat => (
                    <SelectItem key={cat} value={cat}>{cat}</SelectItem>
                  ))}
                </SelectContent>
              </Select>
            </div>
          </div>

          <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
            <div className="space-y-2">
              <Label htmlFor="date">Date</Label>
              <Input
                type="date"
                id="date"
                value={date}
                onChange={(e) => setDate(e.target.value)}
                required
                disabled={disabled || isLoading}
              />
            </div>

            <div className="space-y-2">
              <Label htmlFor="description">Description (Optional)</Label>
              <Input
                type="text"
                id="description"
                value={description}
                onChange={(e) => setDescription(e.target.value)}
                placeholder="What was this expense for?"
                disabled={disabled || isLoading}
              />
            </div>
          </div>

          {error && <p className="text-sm font-medium text-destructive">{error}</p>}

          <Button type="submit" disabled={disabled || isLoading} className="w-full sm:w-auto">
            {isLoading ? 'Adding...' : 'Add Expense'}
          </Button>
        </form>
      </CardContent>
    </Card>
  );
};
