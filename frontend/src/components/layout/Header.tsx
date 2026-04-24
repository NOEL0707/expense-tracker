import React from 'react';
import { UserSwitcher } from '@/features/users/components/UserSwitcher';

interface HeaderProps {
  selectedUserId: string;
  onSelectUser: (id: string) => void;
}

export const Header: React.FC<HeaderProps> = ({ selectedUserId, onSelectUser }) => {
  return (
    <header className="flex flex-col gap-4 md:flex-row md:items-center md:justify-between">
      <div>
        <h1 className="text-3xl font-bold tracking-tight">Expense Tracker</h1>
        <p className="mt-1 text-muted-foreground">
          Keep track of your spending effortlessly
        </p>
      </div>

      <UserSwitcher
        selectedUserId={selectedUserId}
        onSelectUser={onSelectUser}
      />
    </header>
  );
};
