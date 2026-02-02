package com.kwedinger.blog.repository;

import com.kwedinger.blog.model.Bio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BioRepository extends JpaRepository<Bio, Long> {
    Optional<Bio> findFirstByOrderByIdAsc();
}
