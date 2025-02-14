package com.photosystem.repository;

import com.photosystem.entity.Photo;
import com.photosystem.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;

public interface PhotoRepository extends JpaRepository<Photo, Long> {
    Optional<Photo> findByIdAndUser(Long id, User user);

    Page<Photo> findByUser(User user, Pageable pageable);

    Page<Photo> findByUserAndTakenAtBetween(User user, LocalDateTime start, LocalDateTime end, Pageable pageable);

    @Query(value = """
            SELECT p.* FROM photos p
            WHERE p.user_id = :userId
            AND (6371 * acos(cos(radians(:latitude)) * cos(radians(p.latitude))
            * cos(radians(p.longitude) - radians(:longitude))
            + sin(radians(:latitude)) * sin(radians(p.latitude)))) <= :radius
            """, nativeQuery = true)
    Page<Photo> findNearbyPhotos(Long userId, Double latitude, Double longitude, Double radius, Pageable pageable);

    void deleteAllByIdInAndUser(Set<Long> ids, User user);
}