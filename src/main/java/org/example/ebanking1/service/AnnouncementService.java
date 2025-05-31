package org.example.ebanking1.service;

import org.example.ebanking1.entities.Announcement;
import org.example.ebanking1.repositories.AnnouncementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class AnnouncementService {

    private final AnnouncementRepository repository;

    @Autowired
    public AnnouncementService(AnnouncementRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public Announcement save(Announcement entity) {
        return repository.save(entity);
    }

    public Optional<Announcement> findById(String id) {
        return repository.findById(id);
    }

    public List<Announcement> findAll() {
        return repository.findAll();
    }

    @Transactional
    public void delete(String id) {
        repository.deleteById(id);
    }

    public List<Announcement> findImportantAnnouncements() {
        return repository.findByIsImportantTrue();
    }

    public List<Announcement> findByCategory(String category) {
        return repository.findByCategory(category);
    }

    public List<Announcement> findByDateBetween(LocalDateTime start, LocalDateTime end) {
        return repository.findByDateBetween(start, end);
    }

    public List<Announcement> findByAuthor(String author) {
        return repository.findByAuthor(author);
    }

    public List<Announcement> findCurrentAnnouncements() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime oneMonthAgo = now.minusMonths(1);
        return repository.findByDateBetween(oneMonthAgo, now);
    }
}
