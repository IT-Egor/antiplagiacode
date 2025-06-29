package ru.itegor.antiplagiacode.scanner;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import ru.itegor.antiplagiacode.kafka.message.ComparisonResultMessage;

@Slf4j
@Service
public class FileComparatorService {
    @KafkaListener(topics = "${app.kafka.file-scan-topic}", groupId = "${app.kafka.group-id}")
    public void consume(ComparisonResultMessage message) {
        log.info("{}", message);
    }
}
