package ru.itegor.antiplagiacode.scanner;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import ru.itegor.antiplagiacode.client.AntiplagiacodeClient;
import ru.itegor.antiplagiacode.client.dto.ComparisonResultDto;
import ru.itegor.antiplagiacode.client.dto.FileResponseDto;
import ru.itegor.antiplagiacode.client.dto.MergeComparisonResultRequestDto;
import ru.itegor.antiplagiacode.exception.NormalizerException;
import ru.itegor.antiplagiacode.kafka.message.ComparisonResultMessage;
import ru.itegor.antiplagiacode.normalizer.Normalizer;
import ru.itegor.antiplagiacode.normalizer.NormalizerFactory;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class FileComparatorService {
    private final AntiplagiacodeClient client;
    private final NormalizerFactory normalizerFactory;
    private final MatchCalculator matchCalculator;

    @KafkaListener(topics = "${app.kafka.file-scan-topic}", groupId = "${app.kafka.group-id}")
    public void consume(ComparisonResultMessage message) {
        log.info("Message: {}", message);

        MergeComparisonResultRequestDto results = getResults(message);
        postResults(results);

        log.info("Results: {}", results);
    }

    private Normalizer getNormalizerForFile(String fileName) {
        String fileExtension = fileName.substring(fileName.lastIndexOf(".") + 1);
        return normalizerFactory.getNormalizer(fileExtension);
    }

    private List<String> normalizeFile(FileResponseDto file) {
        List<String> lines = List.of(
                new String(file.getFile())
                        .split("\n")
        );
        try {
            Normalizer normalizer = getNormalizerForFile(file.getFileName());
            return normalizer.normalize(lines);
        } catch (NormalizerException e) {
            return lines;
        }
    }

    private ComparisonResultDto createComparisonResult(List<String> originalFileLines,
                                                       List<String> comparedFileLines,
                                                       Long comparedFileId) {
        String originalFile = String.join("\n", originalFileLines);
        String comparedFile = String.join("\n", comparedFileLines);
        double result = matchCalculator.getStringPercentage(originalFile, comparedFile);

        return new ComparisonResultDto(
                BigDecimal.valueOf(result),
                comparedFileId
        );
    }

    private MergeComparisonResultRequestDto getResults(ComparisonResultMessage message) {
        List<ComparisonResultDto> results = new ArrayList<>(message.getComparedFileIds().size());

        FileResponseDto originalFile = client.downloadFileWithName(message.getOriginalFileId());

        List<String> originalFileLines = normalizeFile(originalFile);

        for (Long fileId : message.getComparedFileIds()) {
            FileResponseDto comparedFile = client.downloadFileWithName(fileId);
            List<String> comparedFileLines = normalizeFile(comparedFile);
            results.add(createComparisonResult(originalFileLines, comparedFileLines, fileId));
        }

        return new MergeComparisonResultRequestDto(message.getOriginalFileId(), results);
    }

    private void postResults(MergeComparisonResultRequestDto results) {
        try {
            client.postComparisonResults(results);
        } catch (Exception e) {
            log.error("Error while posting results", e);
        }
    }
}
