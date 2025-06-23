package ru.itegor.antiplagiacode.file;

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
public class FileController {
    private final FileService fileService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public PagedModel<FileMetadataResponseDto> getAll(@ParameterObject Pageable pageable) {
        Page<FileMetadataResponseDto> fileResponseDtos = fileService.getAll(pageable);
        return new PagedModel<>(fileResponseDtos);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public FileMetadataResponseDto getOne(@PathVariable Long id) {
        return fileService.getOne(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public FileMetadataResponseDto create(@RequestParam Long studentId,
                                          @RequestParam Long taskId,
                                          @RequestParam("file") MultipartFile file) {
        return fileService.upload(studentId, taskId, file);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/download/{id}")
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
    public FileMetadataResponseDto delete(@PathVariable Long id) {
        return fileService.delete(id);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.OK)
    public void deleteMany(@RequestParam List<Long> ids) {
        fileService.deleteMany(ids);
    }
}
