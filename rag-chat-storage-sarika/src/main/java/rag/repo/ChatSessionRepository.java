package rag.repo;

import rag.domain.ChatSession;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ChatSessionRepository extends JpaRepository<ChatSession, UUID> {
    Page<ChatSession> findByUserId(String userId, Pageable pageable);
    Page<ChatSession> findByUserIdAndFavorite(String userId, boolean favorite, Pageable pageable);
}
