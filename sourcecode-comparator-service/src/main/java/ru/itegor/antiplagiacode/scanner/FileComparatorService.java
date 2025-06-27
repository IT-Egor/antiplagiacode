package ru.itegor.antiplagiacode.scanner;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FileComparatorService {
    @KafkaListener(topics = "my-topic", groupId = "my-group")
    public void consume(List<String> messages) {
        System.out.println("Received messages: " + messages);
    }
}
