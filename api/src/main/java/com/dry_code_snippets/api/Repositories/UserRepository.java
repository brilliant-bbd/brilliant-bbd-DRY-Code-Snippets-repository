package com.dry_code_snippets.api.Repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dry_code_snippets.api.Models.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUserGuid(UUID userGuid);

}