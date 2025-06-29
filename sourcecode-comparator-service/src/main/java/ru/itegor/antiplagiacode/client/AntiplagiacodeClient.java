package ru.itegor.antiplagiacode.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import ru.itegor.antiplagiacode.client.dto.FileResponseDto;

@Component
public class AntiplagiacodeClient {
    private final RestClient restClient;
    private final String baseURL;
    private final String fileDownloadPath;

    public AntiplagiacodeClient(@Value("${app.antiplagiacode.bootstrap-server}") String baseURL,
                                @Value("${app.antiplagiacode.path.download}") String fileDownloadPath) {
        this.baseURL = baseURL;
        this.fileDownloadPath = fileDownloadPath;
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
