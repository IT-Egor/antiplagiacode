package ru.itegor.antiplagiacode.comparison_result;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.itegor.antiplagiacode.comparison_result.dto.ComparisonResultResponseDto;
import ru.itegor.antiplagiacode.comparison_result.dto.MergeComparisonResultRequestDto;
import ru.itegor.antiplagiacode.comparison_result.service.ComparisonResultService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/comparison-result")
@Tag(name = "Comparison result", description = "Comparison result API. For files comparison results")
public class ComparisonResultController {
    private final ComparisonResultService comparisonResultService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get all comparison results")
    public PagedModel<ComparisonResultResponseDto> getAll(@ParameterObject Pageable pageable) {
        Page<ComparisonResultResponseDto> comparisonResultResponseDtos = comparisonResultService.getAll(pageable);
        return new PagedModel<>(comparisonResultResponseDtos);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get comparison result by id")
    public ComparisonResultResponseDto getOne(@PathVariable Long id) {
        return comparisonResultService.getOne(id);
    }

    @GetMapping("/by-file-id/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get comparison results by file id", description = "Get comparison results for all uploaded files in task")
    public List<ComparisonResultResponseDto> getAllByFileId(@PathVariable Long id) {
        return comparisonResultService.getAllByFileId(id);
    }

    @GetMapping("/by-task-id/{taskId}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get comparison results by task id")
    public List<ComparisonResultResponseDto> getAllByTaskId(@PathVariable Long taskId) {
        return comparisonResultService.getAllByTaskId(taskId);
    }

    @GetMapping("/warning/by-file-id/{fileId}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get suspicious comparison results by file id", description = "Get comparison results by file id with result greater than 50.00")
    public List<ComparisonResultResponseDto> getAllWarningsByFileId(@PathVariable Long fileId) {
        return comparisonResultService.getWarningsByFileId(fileId);
    }

    @GetMapping("warning/by-task-id/{taskId}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get suspicious comparison results by task id", description = "Get comparison results by task id with result greater than 50.00")
    public List<ComparisonResultResponseDto> getAllWarningsByTaskId(@PathVariable Long taskId) {
        return comparisonResultService.getWarningsByTaskId(taskId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Merge many comparison results", description = "Merge many comparison results for files. If existing comparison results for files, they will be updated. When add a new result, a mirror copy of it will be created")
    public List<ComparisonResultResponseDto> createMany(@RequestBody @Valid MergeComparisonResultRequestDto dto) {
        return comparisonResultService.mergeMany(dto);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Delete many comparison results")
    public void deleteMany(@RequestParam List<Long> ids) {
        comparisonResultService.deleteMany(ids);
    }

    @DeleteMapping("by-file-id/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Delete many comparison results by file id", description = "Delete all comparison results of all uploaded files in task")
    public void deleteManyByFileId(@PathVariable Long id) {
        comparisonResultService.deleteManyByOriginalFileId(id);
    }
}
