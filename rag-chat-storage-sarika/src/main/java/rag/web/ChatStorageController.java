package rag.web;

import rag.domain.ChatMessage;
import rag.domain.ChatSession;
import rag.ChatStorageService;
import rag.web.dto.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
public class ChatStorageController {
    private final ChatStorageService chatService;

    public ChatStorageController(ChatStorageService chatService) {
        this.chatService = chatService;
    }

    @PostMapping("/sessions")
    public ChatSession createSession(@RequestBody(required = false) CreateSessionRequest req) {
        String title = req == null ? null : req.title;
        String userId = req == null ? null : req.userId;
        return chatService.createSession(title, userId);
    }

    @GetMapping("/sessions")
    public Page<ChatSession> listSessions(@RequestParam(required = false) String userId,
                                          @RequestParam(required = false) Boolean favorite,
                                          @RequestParam(defaultValue = "0") int page,
                                          @RequestParam(defaultValue = "20") int size) {
        return chatService.listSessions(userId, favorite, PageRequest.of(page, size));
    }

    @PatchMapping("/sessions/{id}/rename")
    public ChatSession rename(@PathVariable UUID id, @RequestBody RenameSessionRequest req) {
        return chatService.renameSession(id, req.title);
    }

    @PatchMapping("/sessions/{id}/favorite")
    public ChatSession favorite(@PathVariable UUID id, @RequestBody FavoriteRequest req) {
        return chatService.markFavorite(id, req.favorite);
    }

    @DeleteMapping("/sessions/{id}")
    public Map<String,String> delete(@PathVariable UUID id) {
        chatService.deleteSession(id);
        return Map.of("status", "deleted");
    }

    @PostMapping("/sessions/{id}/messages")
    public ChatMessage addMessage(@PathVariable UUID id, @RequestBody AddMessageRequest req) throws Exception {
        String contextJson = req.context == null ? null : req.context.toString();
        return chatService.addMessage(id, req.sender, req.content, contextJson);
    }

    @GetMapping("/sessions/{id}/messages")
    public Page<ChatMessage> listMessages(@PathVariable UUID id,
                                          @RequestParam(defaultValue = "0") int page,
                                          @RequestParam(defaultValue = "50") int size) {
        return chatService.getMessages(id, PageRequest.of(page, size));
    }
}
