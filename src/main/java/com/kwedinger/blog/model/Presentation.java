package com.kwedinger.blog.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "presentations")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Presentation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "INTEGER")
    private Long id;
    
    private String title;
    
    @Column(name = "abstract", columnDefinition = "TEXT")
    private String abstractText;
    
    @Column(name = "slides_url")
    private String slidesUrl;
    
    @Column(name = "github_url")
    private String githubUrl;
    
    @Column(name = "created_at", nullable = false, columnDefinition = "TEXT")
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at", nullable = false, columnDefinition = "TEXT")
    private LocalDateTime updatedAt;
    
    @OneToMany(mappedBy = "presentation", cascade = {CascadeType.ALL}, orphanRemoval = true)
    private List<ConferencePresentation> conferencePresentations = new ArrayList<>();
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
    
    /**
     * Transient getter for backward compatibility with templates.
     * Returns the list of conferences associated with this presentation.
     */
    @Transient
    public List<Conference> getConferences() {
        if (conferencePresentations == null) {
            return new ArrayList<>();
        }
        return conferencePresentations.stream()
            .map(ConferencePresentation::getConference)
            .collect(Collectors.toList());
    }
    
    /**
     * Helper method to set conferences by creating ConferencePresentation entities.
     */
    public void setConferences(List<Conference> conferences) {
        if (conferencePresentations == null) {
            conferencePresentations = new ArrayList<>();
        }
        // Clear existing associations
        conferencePresentations.clear();
        // Create new associations
        if (conferences != null) {
            for (Conference conference : conferences) {
                ConferencePresentation cp = new ConferencePresentation();
                cp.setPresentation(this);
                cp.setConference(conference);
                conferencePresentations.add(cp);
            }
        }
    }
}
