package com.photosystem.service;

import com.photosystem.entity.*;
import com.photosystem.repository.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class ShareService {

    private final ShareLinkRepository shareLinkRepository;
    private final PhotoRepository photoRepository;
    private final AlbumRepository albumRepository;
    private final PasswordEncoder passwordEncoder;

    public ShareService(ShareLinkRepository shareLinkRepository,
            PhotoRepository photoRepository,
            AlbumRepository albumRepository,
            PasswordEncoder passwordEncoder) {
        this.shareLinkRepository = shareLinkRepository;
        this.photoRepository = photoRepository;
        this.albumRepository = albumRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public ShareLink createShareLink(Long photoId, Long albumId, LocalDateTime expiresAt,
            String password, User user) {
        ShareLink shareLink = new ShareLink();
        shareLink.setUser(user);
        shareLink.setExpiresAt(expiresAt);

        if (photoId != null) {
            Photo photo = photoRepository.findByIdAndUser(photoId, user)
                    .orElseThrow(() -> new RuntimeException("Photo not found"));
            shareLink.setPhoto(photo);
        }

        if (albumId != null) {
            Album album = albumRepository.findByIdAndUser(albumId, user)
                    .orElseThrow(() -> new RuntimeException("Album not found"));
            shareLink.setAlbum(album);
        }

        if (password != null && !password.isEmpty()) {
            shareLink.setPasswordProtected(true);
            shareLink.setPassword(passwordEncoder.encode(password));
        }

        return shareLinkRepository.save(shareLink);
    }

    public ShareLink getShareLink(String token, String password) {
        ShareLink shareLink = shareLinkRepository.findValidShareLink(token, LocalDateTime.now())
                .orElseThrow(() -> new RuntimeException("Share link not found or expired"));

        if (shareLink.isPasswordProtected()) {
            if (password == null || !passwordEncoder.matches(password, shareLink.getPassword())) {
                throw new RuntimeException("Invalid password");
            }
        }

        return shareLink;
    }

    public Page<ShareLink> getUserShareLinks(User user, Pageable pageable) {
        return shareLinkRepository.findByUser(user, pageable);
    }

    public void deleteShareLink(String token, User user) {
        ShareLink shareLink = shareLinkRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Share link not found"));

        if (!shareLink.getUser().equals(user)) {
            throw new RuntimeException("Unauthorized");
        }

        shareLinkRepository.delete(shareLink);
    }
}