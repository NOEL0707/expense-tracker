import { baseApi } from "@/store/baseApi";

import type { UpsertUserDTO, User } from "../types";

export const usersApi = baseApi.injectEndpoints({
  endpoints: (builder) => ({
    getUsers: builder.query<User[], void>({
      query: () => "/users",
      providesTags: (result) =>
        result
          ? [
              ...result.map((user) => ({ type: "User" as const, id: user.id })),
              { type: "User" as const, id: "LIST" },
            ]
          : [{ type: "User" as const, id: "LIST" }],
    }),
    getUser: builder.query<User, string>({
      query: (id) => `/users/${id}`,
      providesTags: (_result, _error, id) => [{ type: "User", id }],
    }),
    createUser: builder.mutation<User, UpsertUserDTO>({
      query: (user) => ({
        url: "/users",
        method: "POST",
        body: user,
      }),
      invalidatesTags: [{ type: "User", id: "LIST" }],
    }),
    updateUser: builder.mutation<User, { id: string; user: UpsertUserDTO }>({
      query: ({ id, user }) => ({
        url: `/users/${id}`,
        method: "PUT",
        body: user,
      }),
      invalidatesTags: (_result, _error, { id }) => [
        { type: "User", id },
        { type: "User", id: "LIST" },
      ],
    }),
    deleteUser: builder.mutation<void, string>({
      query: (id) => ({
        url: `/users/${id}`,
        method: "DELETE",
      }),
      invalidatesTags: [
        { type: "User", id: "LIST" },
        { type: "Expense", id: "LIST" },
      ],
    }),
  }),
});

export const {
  useGetUsersQuery,
  useGetUserQuery,
  useCreateUserMutation,
  useUpdateUserMutation,
  useDeleteUserMutation,
} = usersApi;
