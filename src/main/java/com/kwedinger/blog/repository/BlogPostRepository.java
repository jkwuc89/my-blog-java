package com.kwedinger.blog.repository;

import com.kwedinger.blog.model.BlogPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface BlogPostRepository extends JpaRepository<BlogPost, Long> {
    Optional<BlogPost> findByFilename(String filename);
    
    @Query("SELECT b FROM BlogPost b WHERE b.publishedAt IS NOT NULL AND b.publishedAt <= :date ORDER BY b.publishedAt DESC")
    List<BlogPost> findPublishedRecent(@Param("date") LocalDate date);
    
    List<BlogPost> findByPublishedAtIsNotNullAndPublishedAtLessThanEqualOrderByPublishedAtDesc(LocalDate date);
}
