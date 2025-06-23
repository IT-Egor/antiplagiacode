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
import ru.itegor.antiplagiacode.comparison_result.dto.CreateComparisonResultRequestDto;
import ru.itegor.antiplagiacode.comparison_result.dto.UpdateComparisonResultRequestDto;
import ru.itegor.antiplagiacode.comparison_result.service.ComparisonResultService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/comparisonResult")
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

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create many comparison results", description = "Create many comparison results for files. When creating, mirror copies of the result are added for the corresponding files")
    public List<ComparisonResultResponseDto> createMany(@RequestBody List<CreateComparisonResultRequestDto> dtos) {
        return comparisonResultService.createMany(dtos);
    }

    @PatchMapping
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Patch many comparison results", description = "Patch many comparison results for files. When patching, mirror copies of the result are patched for the corresponding files")
    public List<ComparisonResultResponseDto> patchMany(@RequestBody @Valid List<UpdateComparisonResultRequestDto> dtos) {
        return comparisonResultService.patchMany(dtos);
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
