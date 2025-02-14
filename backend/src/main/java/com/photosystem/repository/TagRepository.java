package com.photosystem.repository;

import com.photosystem.entity.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface TagRepository extends JpaRepository<Tag, Long> {
    Optional<Tag> findByName(String name);

    boolean existsByName(String name);

    @Query("SELECT t FROM Tag t WHERE t.name LIKE %:keyword%")
    Page<Tag> searchByKeyword(String keyword, Pageable pageable);

    @Query("SELECT t FROM Tag t JOIN t.photos p WHERE p.id = :photoId")
    Set<Tag> findByPhotoId(Long photoId);
}