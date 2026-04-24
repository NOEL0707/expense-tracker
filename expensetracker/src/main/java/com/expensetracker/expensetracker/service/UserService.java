package com.expensetracker.expensetracker.service;

import com.expensetracker.expensetracker.dto.UpsertUserDTO;
import com.expensetracker.expensetracker.model.AppUser;

import java.util.List;
import java.util.UUID;

public interface UserService {

    AppUser getRequiredUser(UUID userId);

    List<AppUser> getUsers();

    AppUser createUser(UpsertUserDTO dto);

    AppUser getUser(UUID userId);

    AppUser updateUser(UUID userId, UpsertUserDTO dto);

    void deleteUser(UUID userId);
}
