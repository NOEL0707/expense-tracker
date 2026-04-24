package com.expensetracker.expensetracker.service;

import com.expensetracker.expensetracker.dto.UpsertUserDTO;
import com.expensetracker.expensetracker.exception.UserNotFoundException;
import com.expensetracker.expensetracker.model.AppUser;
import com.expensetracker.expensetracker.repository.ExpenseRepository;
import com.expensetracker.expensetracker.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ExpenseRepository expenseRepository;

    @Override
    @Transactional(readOnly = true)
    public AppUser getRequiredUser(UUID userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.warn("User not found for id: {}", userId);
                    return new UserNotFoundException(userId);
                });
    }

    @Override
    @Transactional(readOnly = true)
    public List<AppUser> getUsers() {
        return userRepository.findAll();
    }

    @Override
    @Transactional
    public AppUser createUser(UpsertUserDTO dto) {
        AppUser user = AppUser.builder()
                .name(dto.getName())
                .email(dto.getEmail())
                .build();
        return userRepository.save(user);
    }

    @Override
    @Transactional(readOnly = true)
    public AppUser getUser(UUID userId) {
        return getRequiredUser(userId);
    }

    @Override
    @Transactional
    public AppUser updateUser(UUID userId, UpsertUserDTO dto) {
        AppUser user = getRequiredUser(userId);
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        return userRepository.save(user);
    }

    @Override
    @Transactional
    public void deleteUser(UUID userId) {
        getRequiredUser(userId);
        expenseRepository.deleteByUserId(userId);
        userRepository.deleteById(userId);
    }
}
