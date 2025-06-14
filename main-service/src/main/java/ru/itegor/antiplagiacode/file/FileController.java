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
    public PagedModel<FileResponseDto> getAll(@ParameterObject Pageable pageable) {
        Page<FileResponseDto> fileResponseDtos = fileService.getAll(pageable);
        return new PagedModel<>(fileResponseDtos);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public FileResponseDto getOne(@PathVariable Long id) {
        return fileService.getOne(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public FileResponseDto create(@RequestParam Long studentId, @RequestParam Long taskId) {
        return fileService.upload(studentId, taskId);
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public FileResponseDto patch(@PathVariable Long id) {
        return fileService.patch(id);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/download/{storageId}")
    public ResponseEntity<ByteArrayResource> downloadFile(@PathVariable Long storageId) {
        byte[] data = fileService.download(storageId);
        ByteArrayResource resource = new ByteArrayResource(data);
        return ResponseEntity.ok()
                .contentLength(data.length)
                .header("Content-type", "application/octet-stream")
                .header("Content-disposition", "attachment; filename=\"" + storageId + "\"")
                .body(resource);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/lines/{storageId}")
    public List<String> getFileLines(@PathVariable Long storageId) {
        return fileService.getFileLines(storageId);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public FileResponseDto delete(@PathVariable Long id) {
        return fileService.delete(id);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.OK)
    public void deleteMany(@RequestParam List<Long> ids) {
        fileService.deleteMany(ids);
    }
}
