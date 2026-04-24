package com.expensetracker.expensetracker.repository;

import com.expensetracker.expensetracker.model.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<AppUser, UUID> {
}
