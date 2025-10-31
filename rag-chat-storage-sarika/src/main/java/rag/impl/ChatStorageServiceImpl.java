package rag.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import rag.domain.ChatMessage;
import rag.domain.ChatSession;
import rag.repo.ChatMessageRepository;
import rag.repo.ChatSessionRepository;
import rag.ChatStorageService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@Transactional
public class ChatStorageServiceImpl implements ChatStorageService {
    private final ChatSessionRepository sessionRepo;
    private final ChatMessageRepository messageRepo;
    private final ObjectMapper mapper = new ObjectMapper();

    public ChatStorageServiceImpl(ChatSessionRepository sessionRepo, ChatMessageRepository messageRepo) {
        this.sessionRepo = sessionRepo;
        this.messageRepo = messageRepo;
    }

    @Override
    public ChatSession createSession(String title, String userId) {
        ChatSession s = new ChatSession();
        s.setTitle(title == null || title.isBlank() ? "New Chat" : title);
        s.setUserId(userId == null ? "default" : userId);
        return sessionRepo.save(s);
    }

    @Override
    public Page<ChatSession> listSessions(String userId, Boolean favorite, Pageable pageable) {
        String uid = userId == null ? "default" : userId;
        if (favorite == null) {
            return sessionRepo.findByUserId(uid, pageable);
        }
        return sessionRepo.findByUserIdAndFavorite(uid, favorite, pageable);
    }

    @Override
    public ChatSession renameSession(UUID id, String title) {
        ChatSession s = sessionRepo.findById(id).orElseThrow(() -> new NoSuchElementException("Session not found"));
        s.setTitle(title);
        return sessionRepo.save(s);
    }

    @Override
    public ChatSession markFavorite(UUID id, boolean fav) {
        ChatSession s = sessionRepo.findById(id).orElseThrow(() -> new NoSuchElementException("Session not found"));
        s.setFavorite(fav);
        return sessionRepo.save(s);
    }

    @Override
    public void deleteSession(UUID id) {
        ChatSession s = sessionRepo.findById(id).orElseThrow(() -> new NoSuchElementException("Session not found"));
        messageRepo.deleteBySession(s);
        sessionRepo.delete(s);
    }

    @Override
    public ChatMessage addMessage(UUID sessionId, String sender, String content, String contextJson) {
        ChatSession s = sessionRepo.findById(sessionId).orElseThrow(() -> new NoSuchElementException("Session not found"));
        ChatMessage m = new ChatMessage();
        m.setSession(s);
        m.setSender(sender);
        m.setContent(content);
        m.setContext(contextJson);
        return messageRepo.save(m);
    }

    @Override
    public Page<ChatMessage> getMessages(UUID sessionId, Pageable pageable) {
        ChatSession s = sessionRepo.findById(sessionId).orElseThrow(() -> new NoSuchElementException("Session not found"));
        return messageRepo.findBySession(s, pageable);
    }
}
