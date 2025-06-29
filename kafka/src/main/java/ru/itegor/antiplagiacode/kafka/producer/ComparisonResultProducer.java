package ru.itegor.antiplagiacode.kafka.producer;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ru.itegor.antiplagiacode.kafka.message.ComparisonResultMessage;

import java.time.Instant;

@Service
public class ComparisonResultProducer {
    private final String comparisonResultTopic;
    private final KafkaTemplate<Long, ComparisonResultMessage> kafkaTemplate;

    public ComparisonResultProducer(KafkaTemplate<Long, ComparisonResultMessage> kafkaTemplate,
                                    @Value("${app.kafka.file-scan-topic}") String comparisonResultTopic) {
        this.kafkaTemplate = kafkaTemplate;
        this.comparisonResultTopic = comparisonResultTopic;
    }

    public void sendComparisonResult(ComparisonResultMessage message) {
        kafkaTemplate.send(comparisonResultTopic,
                null,
                Instant.now().toEpochMilli(),
                message.getOriginalFileId(),
                message);
    }
}
