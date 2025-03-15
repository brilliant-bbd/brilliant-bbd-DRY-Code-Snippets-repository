package com.dry_code_snippets.api.Repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dry_code_snippets.api.Models.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findByUserGuid(UUID userGuid);
    // Custom queries can be added here if needed

}