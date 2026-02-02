package com.kwedinger.blog.repository;

import com.kwedinger.blog.model.Conference;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ConferenceRepository extends JpaRepository<Conference, Long> {
    Optional<Conference> findByTitleAndYear(String title, Integer year);
}
