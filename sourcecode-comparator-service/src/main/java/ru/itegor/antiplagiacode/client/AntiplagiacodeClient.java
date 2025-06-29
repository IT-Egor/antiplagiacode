package ru.itegor.antiplagiacode.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import ru.itegor.antiplagiacode.client.dto.FileResponseDto;
import ru.itegor.antiplagiacode.client.dto.MergeComparisonResultRequestDto;
import ru.itegor.antiplagiacode.exception.ClientException;

@Component
public class AntiplagiacodeClient {
    private final RestClient restClient;
    private final String baseURL;
    private final String fileDownloadPath;
    private final String postComparisonResultsPath;

    public AntiplagiacodeClient(@Value("${app.antiplagiacode.bootstrap-server}") String baseURL,
                                @Value("${app.antiplagiacode.path.download}") String fileDownloadPath,
                                @Value("${app.antiplagiacode.path.post-comparison-results}") String postComparisonResultsPath) {
        this.baseURL = baseURL;
        this.fileDownloadPath = fileDownloadPath;
        this.postComparisonResultsPath = postComparisonResultsPath;
        this.restClient = RestClient.create(baseURL);
    }

    public FileResponseDto downloadFileWithName(Long fileId) {
        ResponseEntity<byte[]> response = restClient.get()
                .uri(fileDownloadPath, fileId)
                .retrieve()
                .toEntity(byte[].class);

        String filename = extractFilenameFromHeaders(response.getHeaders());

        return new FileResponseDto(filename, response.getBody());
    }

    public void postComparisonResults(MergeComparisonResultRequestDto request) {
        restClient.post()
                .uri(postComparisonResultsPath)
                .body(request)
                .retrieve()
                .onStatus(
                        status -> !status.is2xxSuccessful(),
                        (req, resp) -> {
                            throw new ClientException(
                                    "Error while posting comparison results: ",
                                    new String(resp.getBody().readAllBytes()),
                                    resp.getStatusCode().value());
                        }
                ).toBodilessEntity();
    }

    private String extractFilenameFromHeaders(HttpHeaders headers) {
        String contentDisposition = headers.getFirst(HttpHeaders.CONTENT_DISPOSITION);
        if (contentDisposition == null) {
            return "unnamed_file";
        }

        return contentDisposition.split("filename=")[1]
                .replace("\"", "")
                .trim();
    }
}
