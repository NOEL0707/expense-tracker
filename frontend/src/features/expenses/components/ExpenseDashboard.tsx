import React, { useState } from 'react';
import { useGetExpensesQuery, useCreateExpenseMutation } from '../api/expensesApi';
import type { CreateExpenseDTO } from '../types';
import { ExpenseForm } from './ExpenseForm';
import { ExpenseList } from './ExpenseList';
import { Button } from '@/components/ui/button';
import { Card, CardContent, CardFooter } from '@/components/ui/card';
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from '@/components/ui/select';

interface ExpenseDashboardProps {
  userId: string;
}

export const ExpenseDashboard: React.FC<ExpenseDashboardProps> = ({ userId }) => {
  const [filterCategory, setFilterCategory] = useState<string>('all');
  const [sortOrder, setSortOrder] = useState<string>('date_desc');
  const [page, setPage] = useState(0);
  const pageSize = 10;

  const { data: expensesPage, isLoading: isLoadingExpenses } = useGetExpensesQuery(
    { 
      userId, 
      category: filterCategory === 'all' ? undefined : filterCategory,
      sort: sortOrder,
      page,
      size: pageSize,
    },
    { skip: !userId }
  );

  const [createExpense, { isLoading: isCreatingExpense }] = useCreateExpenseMutation();

  const expenses = expensesPage?.content ?? [];
  const totalPages = expensesPage?.totalPages ?? 0;
  const totalElements = expensesPage?.totalElements ?? 0;
  const currentPage = expensesPage?.page ?? page;
  const startItem = totalElements === 0 ? 0 : currentPage * pageSize + 1;
  const endItem = totalElements === 0 ? 0 : startItem + expenses.length - 1;

  const handleAddExpense = async (expenseData: CreateExpenseDTO, idempotencyKey: string) => {
    if (!userId) return;
    try {
      await createExpense({ 
        userId, 
        expense: expenseData, 
        idempotencyKey 
      }).unwrap();
    } catch (err) {
      console.error('Failed to create expense:', err);
      throw err;
    }
  };

  const totalAmount = expenses.reduce((sum, exp) => sum + exp.amount, 0);
  const formattedTotal = new Intl.NumberFormat('en-IN', {
    style: 'currency',
    currency: 'INR'
  }).format(totalAmount / 100);

  return (
    <>
      <ExpenseForm 
        onSubmit={handleAddExpense} 
        isLoading={isCreatingExpense} 
        disabled={!userId}
      />

      <Card className="border-primary/20">
        <div className="flex flex-col gap-4 border-b bg-muted/20 px-6 py-6 md:flex-row md:items-center md:justify-between">
          <div className="flex w-full flex-col gap-3 sm:flex-row md:w-auto">
            <Select
              value={filterCategory}
              onValueChange={(value) => {
                setFilterCategory(value);
                setPage(0);
              }}
            >
              <SelectTrigger className="w-full sm:w-[220px]" aria-label="Filter Category">
                <SelectValue placeholder="Filter Category" />
              </SelectTrigger>
              <SelectContent>
                <SelectItem value="all">All Categories</SelectItem>
                <SelectItem value="Food">Food</SelectItem>
                <SelectItem value="Transport">Transport</SelectItem>
                <SelectItem value="Utilities">Utilities</SelectItem>
                <SelectItem value="Entertainment">Entertainment</SelectItem>
                <SelectItem value="Shopping">Shopping</SelectItem>
                <SelectItem value="Other">Other</SelectItem>
              </SelectContent>
            </Select>

            <Select
              value={sortOrder}
              onValueChange={(value) => {
                setSortOrder(value);
                setPage(0);
              }}
            >
              <SelectTrigger className="w-[180px]" aria-label="Sort Order">
                <SelectValue placeholder="Sort Order" />
              </SelectTrigger>
              <SelectContent>
                <SelectItem value="date_desc">Newest First</SelectItem>
                <SelectItem value="date_asc">Oldest First</SelectItem>
              </SelectContent>
            </Select>
          </div>

          <div className="text-xl font-semibold tracking-tight text-primary">
            Total: {formattedTotal}
          </div>
        </div>

        <CardContent className="p-0">
          <ExpenseList 
            expenses={expenses} 
            isLoading={isLoadingExpenses && !!userId} 
          />
        </CardContent>

        <CardFooter className="flex flex-col gap-3 border-t pt-6 sm:flex-row sm:items-center sm:justify-between">
          <div className="text-sm text-muted-foreground">
            {totalElements === 0
              ? 'No expenses to display'
              : `Showing ${startItem}-${endItem} of ${totalElements} expenses`}
          </div>

          <div className="flex items-center gap-2">
            <span className="text-sm text-muted-foreground">
              Page {totalPages === 0 ? 0 : currentPage + 1} of {totalPages}
            </span>
            <Button
              type="button"
              variant="outline"
              size="sm"
              onClick={() => setPage((prev) => Math.max(prev - 1, 0))}
              disabled={isLoadingExpenses || currentPage <= 0}
            >
              Previous
            </Button>
            <Button
              type="button"
              variant="outline"
              size="sm"
              onClick={() => setPage((prev) => prev + 1)}
              disabled={isLoadingExpenses || totalPages === 0 || currentPage >= totalPages - 1}
            >
              Next
            </Button>
          </div>
        </CardFooter>
      </Card>
    </>
  );
};
