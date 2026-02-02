package com.kwedinger.blog.service;

import com.kwedinger.blog.model.ContactInfo;
import com.kwedinger.blog.repository.ContactInfoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class ContactInfoService {
    private final ContactInfoRepository contactInfoRepository;
    
    public ContactInfoService(ContactInfoRepository contactInfoRepository) {
        this.contactInfoRepository = contactInfoRepository;
    }
    
    @Transactional
    public ContactInfo getInstance() {
        Optional<ContactInfo> existing = contactInfoRepository.findFirstByOrderByIdAsc();
        if (existing.isPresent()) {
            return existing.get();
        }
        
        // Create new instance if none exists
        ContactInfo contactInfo = new ContactInfo();
        contactInfo.setEmail("");
        return contactInfoRepository.save(contactInfo);
    }
    
    @Transactional
    public ContactInfo save(ContactInfo contactInfo) {
        return contactInfoRepository.save(contactInfo);
    }
}
