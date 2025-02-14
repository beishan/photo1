package com.photosystem.repository;

import com.photosystem.entity.Album;
import com.photosystem.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AlbumRepository extends JpaRepository<Album, Long> {
    Page<Album> findByUser(User user, Pageable pageable);

    Optional<Album> findByIdAndUser(Long id, User user);

    boolean existsByNameAndUser(String name, User user);
}