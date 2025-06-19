package ru.itegor.antiplagiacode.comparison_result;

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
public class ComparisonResultController {
    private final ComparisonResultService comparisonResultService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public PagedModel<ComparisonResultResponseDto> getAll(@ParameterObject Pageable pageable) {
        Page<ComparisonResultResponseDto> comparisonResultResponseDtos = comparisonResultService.getAll(pageable);
        return new PagedModel<>(comparisonResultResponseDtos);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ComparisonResultResponseDto getOne(@PathVariable Long id) {
        return comparisonResultService.getOne(id);
    }

    @GetMapping("/by-file-id/{id}")
    @ResponseStatus(HttpStatus.OK)
    public List<ComparisonResultResponseDto> getAllByFileId(@PathVariable Long id) {
        return comparisonResultService.getAllByFileId(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public List<ComparisonResultResponseDto> createMany(@RequestBody List<CreateComparisonResultRequestDto> dtos) {
        return comparisonResultService.createMany(dtos);
    }

    @PatchMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ComparisonResultResponseDto> patchMany(@RequestBody @Valid List<UpdateComparisonResultRequestDto> dtos) {
        return comparisonResultService.patchMany(dtos);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.OK)
    public void deleteMany(@RequestParam List<Long> ids) {
        comparisonResultService.deleteMany(ids);
    }

    @DeleteMapping("by-file-id/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteManyByFileId(@PathVariable Long id) {
        comparisonResultService.deleteManyByOriginalFileId(id);
    }
}
