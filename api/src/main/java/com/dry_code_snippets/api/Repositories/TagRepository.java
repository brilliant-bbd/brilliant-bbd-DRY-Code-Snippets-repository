package com.dry_code_snippets.api.Repositories;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import com.dry_code_snippets.api.Models.Tag;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {
    
}
