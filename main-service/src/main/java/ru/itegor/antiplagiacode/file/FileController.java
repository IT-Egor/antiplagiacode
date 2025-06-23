package ru.itegor.antiplagiacode.file;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.itegor.antiplagiacode.file.dto.FileMetadataResponseDto;
import ru.itegor.antiplagiacode.file.dto.FileResponseDto;
import ru.itegor.antiplagiacode.file.service.FileService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/file")
@Tag(name = "File", description = "File API. For uploading and downloading files")
public class FileController {
    private final FileService fileService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get all files metadata")
    public PagedModel<FileMetadataResponseDto> getAll(@ParameterObject Pageable pageable) {
        Page<FileMetadataResponseDto> fileResponseDtos = fileService.getAll(pageable);
        return new PagedModel<>(fileResponseDtos);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get file metadata by id")
    public FileMetadataResponseDto getOne(@PathVariable Long id) {
        return fileService.getOne(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Upload file", description = "Upload file to the storage. Each student can only attach one file to a task. Therefore, when the file is uploaded again, it is overwritten")
    public FileMetadataResponseDto create(@RequestParam Long studentId,
                                          @RequestParam Long taskId,
                                          @RequestParam("file") MultipartFile file) {
        return fileService.upload(studentId, taskId, file);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/download/{id}")
    @Operation(summary = "Download file", description = "Download file from the storage")
    public ResponseEntity<ByteArrayResource> downloadFile(@PathVariable Long id) {
        FileResponseDto dto = fileService.download(id);
        byte[] data = dto.getFile();
        ByteArrayResource resource = new ByteArrayResource(data);
        return ResponseEntity.ok()
                .contentLength(data.length)
                .header("Content-type", "application/octet-stream")
                .header("Content-disposition", String.format("attachment; filename=\"%s\"", dto.getFileName()))
                .body(resource);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Delete file by id", description = "Delete one file by id from the storage")
    public FileMetadataResponseDto delete(@PathVariable Long id) {
        return fileService.delete(id);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Delete files by ids", description = "Delete many files by ids from the storage")
    public void deleteMany(@RequestParam List<Long> ids) {
        fileService.deleteMany(ids);
    }
}
