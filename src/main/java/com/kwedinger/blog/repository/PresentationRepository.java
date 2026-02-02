package com.kwedinger.blog.repository;

import com.kwedinger.blog.model.Presentation;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PresentationRepository extends JpaRepository<Presentation, Long> {
    
    @EntityGraph(attributePaths = {"conferencePresentations", "conferencePresentations.conference"})
    @Override
    List<Presentation> findAll();
    
    @EntityGraph(attributePaths = {"conferencePresentations", "conferencePresentations.conference"})
    @Override
    Optional<Presentation> findById(Long id);
}
