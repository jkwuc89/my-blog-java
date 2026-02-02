package com.kwedinger.blog.repository;

import com.kwedinger.blog.model.ConferencePresentation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConferencePresentationRepository extends JpaRepository<ConferencePresentation, Long> {
}
