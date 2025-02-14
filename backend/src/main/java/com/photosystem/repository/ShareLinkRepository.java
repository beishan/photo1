package com.photosystem.repository;

import com.photosystem.entity.ShareLink;
import com.photosystem.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.Optional;

public interface ShareLinkRepository extends JpaRepository<ShareLink, Long> {
    Optional<ShareLink> findByToken(String token);

    @Query("SELECT s FROM ShareLink s WHERE s.token = :token AND (s.expiresAt IS NULL OR s.expiresAt > :now)")
    Optional<ShareLink> findValidShareLink(String token, LocalDateTime now);

    Page<ShareLink> findByUser(User user, Pageable pageable);
}