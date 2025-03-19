package com.dry_code_snippets.api.Repositories;

import com.dry_code_snippets.api.Models.Tag;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class TagRepositoryTest {

    @Autowired
    private TagRepository tagRepository;

    private Tag tag;

    @BeforeEach
    void setUp() {
        tag = new Tag("Java");
        tagRepository.save(tag);
    }

    @AfterEach
    void tearDown() {
        tagRepository.deleteAll();
    }

    @Test
    void testSaveTag_ShouldPersistTag() {
        Tag newTag = new Tag("SpringBoot");
        Tag savedTag = tagRepository.save(newTag);

        assertNotNull(savedTag.getTagId());
        assertEquals("SpringBoot", savedTag.getTagName());
    }

    @Test
    void testFindTagById_ShouldReturnTag() {
        Optional<Tag> foundTag = tagRepository.findById(tag.getTagId());

        assertTrue(foundTag.isPresent());
        assertEquals("Java", foundTag.get().getTagName());
    }

    @Test
    void testFindTagById_WhenTagDoesNotExist_ShouldReturnEmpty() {
        Optional<Tag> foundTag = tagRepository.findById(999L); // Non-existing ID

        assertFalse(foundTag.isPresent());
    }

    @Test
    void testExistsById_ShouldReturnTrue_WhenTagExists() {
        boolean exists = tagRepository.existsById(tag.getTagId());

        assertTrue(exists);
    }

    @Test
    void testExistsById_ShouldReturnFalse_WhenTagDoesNotExist() {
        boolean exists = tagRepository.existsById(999L);

        assertFalse(exists);
    }

    @Test
    void testDeleteTag_ShouldRemoveTag() {
        tagRepository.delete(tag);
        Optional<Tag> deletedTag = tagRepository.findById(tag.getTagId());

        assertFalse(deletedTag.isPresent());
    }
}
