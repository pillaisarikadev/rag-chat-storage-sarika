package rag;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "rag")
public class RagChatStorageApplication {
    public static void main(String[] args) {
        SpringApplication.run(RagChatStorageApplication.class, args);
    }
}
