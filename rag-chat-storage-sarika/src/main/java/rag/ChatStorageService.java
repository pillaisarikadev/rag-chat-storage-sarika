package rag;

import rag.domain.ChatMessage;
import rag.domain.ChatSession;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface ChatStorageService {
    ChatSession createSession(String title, String userId);
    Page<ChatSession> listSessions(String userId, Boolean favorite, Pageable pageable);
    ChatSession renameSession(UUID id, String title);
    ChatSession markFavorite(UUID id, boolean fav);
    void deleteSession(UUID id);
    ChatMessage addMessage(UUID sessionId, String sender, String content, String contextJson);
    Page<ChatMessage> getMessages(UUID sessionId, Pageable pageable);
}
