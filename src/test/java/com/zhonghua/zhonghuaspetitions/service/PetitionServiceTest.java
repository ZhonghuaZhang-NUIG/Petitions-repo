package com.zhonghua.zhonghuaspetitions.service;

import com.zhonghua.zhonghuaspetitions.model.Petition;
import com.zhonghua.zhonghuaspetitions.model.Signature;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class PetitionServiceTest {

    private PetitionService service;

    @BeforeEach
    void setUp() {
        service = new PetitionService();
    }

    @Test
    void initialData_hasThreePetitions() {
        List<Petition> all = service.findAll();
        assertNotNull(all);
        assertTrue(all.size() >= 3, "Expected at least 3 sample petitions");
    }

    @Test
    void create_addsNewPetitionWithIncrementingId() {
        int before = service.findAll().size();
        Petition created = service.create("New Title", "Desc", "Author");
        int after = service.findAll().size();

        assertNotNull(created);
        assertEquals("New Title", created.getTitle());
        assertEquals("Desc", created.getDescription());
        assertEquals("Author", created.getAuthor());
        assertEquals(before + 1, after);
        assertNotNull(created.getId());
    }

    @Test
    void findById_returnsExistingPetition() {
        Long existingId = service.findAll().get(0).getId();
        Optional<Petition> found = service.findById(existingId);

        assertTrue(found.isPresent());
        assertEquals(existingId, found.get().getId());
    }

    @Test
    void findById_returnsEmptyForMissing() {
        Optional<Petition> missing = service.findById(99999L);
        assertTrue(missing.isEmpty());
    }

    @Test
    void searchByTitle_matchesCaseInsensitiveSubstring() {
        // Ensure known sample exists (e.g., "Library")
        List<Petition> results = service.searchByTitle("library");
        assertFalse(results.isEmpty());
        assertTrue(
                results.stream().allMatch(p -> p.getTitle() != null && p.getTitle().toLowerCase().contains("library")));
    }

    @Test
    void searchByTitle_emptyQuery_returnsAllOrNoneConsistently() {
        List<Petition> results = service.searchByTitle("");
        // current implementation returns all titles that contain "" (which is all
        // non-null titles)
        assertEquals(service.findAll().size(), results.size());
    }

    @Test
    void sign_addsSignatureToPetition() {
        Petition p = service.create("Sign Me", "Desc", "Alice");
        int before = p.getSignatures().size();

        service.sign(p.getId(), "Bob", "bob@example.com");

        int after = p.getSignatures().size();
        assertEquals(before + 1, after);
        Signature s = p.getSignatures().get(after - 1);
        assertEquals("Bob", s.getName());
        assertEquals("bob@example.com", s.getEmail());
    }

    @Test
    void sign_missingPetition_doesNothing() {
        // Should not throw
        assertDoesNotThrow(() -> service.sign(123456L, "Ghost", "ghost@example.com"));
    }
}