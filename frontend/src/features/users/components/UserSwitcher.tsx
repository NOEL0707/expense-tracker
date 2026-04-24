import React, { useEffect } from 'react';
import { useGetUsersQuery } from '../api/usersApi';
import { Label } from '@/components/ui/label';
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from '@/components/ui/select';

interface UserSwitcherProps {
  selectedUserId: string;
  onSelectUser: (id: string) => void;
}

export const UserSwitcher: React.FC<UserSwitcherProps> = ({ selectedUserId, onSelectUser }) => {
  const { data: users = [], isLoading } = useGetUsersQuery();

  useEffect(() => {
    if (users.length > 0 && !selectedUserId) {
      onSelectUser(users[0].id);
    }
  }, [users, selectedUserId, onSelectUser]);

  return (
    <div className="flex w-full flex-col gap-2 md:w-auto">
      <Label htmlFor="user-select">Active User</Label>
      {isLoading ? (
        <span className="text-sm text-muted-foreground">Loading...</span>
      ) : (
        <Select value={selectedUserId} onValueChange={onSelectUser}>
          <SelectTrigger id="user-select" className="w-full md:w-[200px]" aria-label="Active User">
            <SelectValue placeholder="Select a user" />
          </SelectTrigger>
          <SelectContent>
            {users.map(u => (
              <SelectItem key={u.id} value={u.id}>{u.name}</SelectItem>
            ))}
          </SelectContent>
        </Select>
      )}
    </div>
  );
};
