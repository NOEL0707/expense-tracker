import React, { useState } from 'react';
import { useCreateUserMutation } from '../api/usersApi';
import { Button } from '@/components/ui/button';
import { Input } from '@/components/ui/input';
import { Label } from '@/components/ui/label';
import { Card, CardContent, CardHeader, CardTitle, CardDescription } from '@/components/ui/card';

interface CreateUserFormProps {
  onSuccess?: (userId: string) => void;
}

export const CreateUserForm: React.FC<CreateUserFormProps> = ({ onSuccess }) => {
  const [name, setName] = useState('');
  const [email, setEmail] = useState('');
  const [createUser, { isLoading }] = useCreateUserMutation();

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!name || !email) return;
    try {
      const newUser = await createUser({ name, email }).unwrap();
      onSuccess?.(newUser.id);
      setName('');
      setEmail('');
    } catch (err) {
      console.error('Failed to create user:', err);
    }
  };

  return (
    <Card>
      <CardHeader>
        <CardTitle>Welcome! Create a User</CardTitle>
        <CardDescription>You need a user profile to start tracking expenses.</CardDescription>
      </CardHeader>
      <CardContent>
        <form onSubmit={handleSubmit} className="flex flex-col items-end gap-4 sm:flex-row">
          <div className="space-y-2 w-full">
            <Label htmlFor="userName">Name</Label>
            <Input id="userName" value={name} onChange={e => setName(e.target.value)} placeholder="John Doe" required />
          </div>
          <div className="space-y-2 w-full">
            <Label htmlFor="userEmail">Email</Label>
            <Input id="userEmail" type="email" value={email} onChange={e => setEmail(e.target.value)} placeholder="john@example.com" required />
          </div>
          <Button type="submit" disabled={isLoading} className="w-full sm:w-auto">
            {isLoading ? 'Creating...' : 'Create User'}
          </Button>
        </form>
      </CardContent>
    </Card>
  );
};
