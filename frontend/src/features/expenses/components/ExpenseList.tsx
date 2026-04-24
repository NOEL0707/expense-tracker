import React from 'react';
import type { Expense } from '../types';
import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
} from '@/components/ui/table';

interface ExpenseListProps {
  expenses: Expense[];
  isLoading: boolean;
}

export const ExpenseList: React.FC<ExpenseListProps> = ({ expenses, isLoading }) => {
  if (isLoading) {
    return <div className="py-8 text-center text-muted-foreground">Loading expenses...</div>;
  }

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
    <div className="overflow-hidden rounded-xl">
      <Table>
        <TableHeader>
          <TableRow className="hover:bg-transparent">
            <TableHead className="w-[120px]">Date</TableHead>
            <TableHead>Category</TableHead>
            <TableHead>Description</TableHead>
            <TableHead className="text-right">Amount</TableHead>
          </TableRow>
        </TableHeader>
        <TableBody>
          {expenses.length === 0 ? (
            <TableRow>
              <TableCell colSpan={4} className="h-32 text-center text-muted-foreground">
                <div className="flex flex-col items-center justify-center gap-2">
                  <p>No expenses found for this user.</p>
                  <p className="text-xs">Try adding an expense above or switching users.</p>
                </div>
              </TableCell>
            </TableRow>
          ) : (
            expenses.map((expense) => (
              <TableRow key={expense.id}>
                <TableCell className="font-medium">{formatDate(expense.date)}</TableCell>
                <TableCell className="font-medium text-primary">{expense.category}</TableCell>
                <TableCell className="max-w-[200px] truncate">
                  {expense.description || <span className="text-muted-foreground">No description</span>}
                </TableCell>
                <TableCell className="text-right font-medium">
                  {formatCurrency(expense.amount)}
                </TableCell>
              </TableRow>
            ))
          )}
        </TableBody>
      </Table>
    </div>
  );
};
