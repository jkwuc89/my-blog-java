package com.kwedinger.blog.service;

import com.kwedinger.blog.model.Bio;
import com.kwedinger.blog.repository.BioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class BioService {
    private final BioRepository bioRepository;
    
    public BioService(BioRepository bioRepository) {
        this.bioRepository = bioRepository;
    }
    
    @Transactional
    public Bio getInstance() {
        Optional<Bio> existing = bioRepository.findFirstByOrderByIdAsc();
        if (existing.isPresent()) {
            return existing.get();
        }
        
        // Create new instance if none exists
        Bio bio = new Bio();
        bio.setName("");
        bio.setBriefBio("");
        bio.setContent("");
        return bioRepository.save(bio);
    }
    
    @Transactional
    public Bio save(Bio bio) {
        return bioRepository.save(bio);
    }
}
