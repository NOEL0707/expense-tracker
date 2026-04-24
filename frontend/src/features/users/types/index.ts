export interface User {
  id: string;
  name: string;
  email: string;
  createdAt: string;
}

export interface UpsertUserDTO {
  name: string;
  email: string;
}
