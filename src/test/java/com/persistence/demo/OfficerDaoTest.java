package com.persistence.demo;

import com.persistence.demo.dao.OfficerDAO;
import com.persistence.demo.entities.Officer;
import com.persistence.demo.entities.Rank;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.transaction.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@Transactional
public class OfficerDaoTest {
    @Autowired
    private OfficerDAO officerDAO;

    @Test
    public void save() {
        Officer officer = new Officer(Rank.ADMIRAL, "Olatunde", "Ogunboyejo");
        Officer savedOfficer = officerDAO.save(officer);
        assertNotNull(savedOfficer.getId());
    }

    @Test
    public void findAll() {
        List<String> officersLastNames = officerDAO.findAll().stream().map(Officer::getLastName).collect(Collectors.toList());
        assertThat(officersLastNames, containsInAnyOrder("Kirk", "Picard", "Sisko", "Janeway", "Archer"));
    }

    @Test
    public void findByIdThatExists() {
        Optional<Officer> officer = officerDAO.findById(1);
        assertTrue(officer.isPresent());
        assertEquals(1, officer.get().getId().intValue());
    }

    @Test
    public void findByIdThatDoesNotExists() {
        Optional<Officer> officer = officerDAO.findById(1000);
        assertTrue(officer.isEmpty());
    }

    @Test
    public void count() {
        assertEquals(5, officerDAO.count());
    }

    @Test
    public void delete() {
        IntStream.rangeClosed(1,5).forEach(id -> {
            assertNotNull(id);
            officerDAO.delete(officerDAO.findById(id).get());
        });
        assertEquals(0, officerDAO.count());
    }

    @Test
    public void existsById() {
        IntStream.rangeClosed(1, 5).forEach(id -> {
            assertTrue(officerDAO.existsById(id));
        });
    }
}
