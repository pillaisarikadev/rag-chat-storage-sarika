package rag.repo;

import rag.domain.ChatMessage;
import rag.domain.ChatSession;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, UUID> {
    Page<ChatMessage> findBySession(ChatSession session, Pageable pageable);
    void deleteBySession(ChatSession session);
}
