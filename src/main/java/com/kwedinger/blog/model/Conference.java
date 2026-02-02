package com.kwedinger.blog.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "conferences", uniqueConstraints = @UniqueConstraint(columnNames = {"title", "year"}))
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Conference {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "INTEGER")
    private Long id;
    
    @Column(nullable = false)
    private String title;
    
    @Column(nullable = false)
    private Integer year;
    
    private String link;
    
    @Column(name = "created_at", nullable = false, columnDefinition = "TEXT")
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at", nullable = false, columnDefinition = "TEXT")
    private LocalDateTime updatedAt;
    
    @OneToMany(mappedBy = "conference", cascade = {CascadeType.ALL}, orphanRemoval = true)
    private List<ConferencePresentation> conferencePresentations = new ArrayList<>();
    
    /**
     * Transient getter for backward compatibility with templates.
     * Returns the list of presentations associated with this conference.
     */
    @Transient
    public List<Presentation> getPresentations() {
        if (conferencePresentations == null) {
            return new ArrayList<>();
        }
        return conferencePresentations.stream()
            .map(ConferencePresentation::getPresentation)
            .collect(java.util.stream.Collectors.toList());
    }
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
