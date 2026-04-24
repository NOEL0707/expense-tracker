import { useState } from 'react';
import { useGetUsersQuery } from '@/features/users/api/usersApi';
import { Header } from '@/components/layout/Header';
import { CreateUserForm } from '@/features/users/components/CreateUserForm';
import { ExpenseDashboard } from '@/features/expenses/components/ExpenseDashboard';

function App() {
  const [selectedUserId, setSelectedUserId] = useState<string>('');
  const { data: users = [], isLoading: isLoadingUsers } = useGetUsersQuery();

  return (
    <div className="min-h-screen bg-background px-4 py-6 md:px-8 md:py-8">
      <div className="mx-auto flex max-w-5xl flex-col gap-8">
        <Header
          selectedUserId={selectedUserId}
          onSelectUser={setSelectedUserId}
        />

        {!isLoadingUsers && users.length === 0 && (
          <CreateUserForm onSuccess={setSelectedUserId} />
        )}

        <main
          className={
            users.length === 0
              ? 'pointer-events-none opacity-50 transition-opacity'
              : 'transition-opacity'
          }
        >
          <ExpenseDashboard userId={selectedUserId} />
        </main>
      </div>
    </div>
  );
}

export default App;
