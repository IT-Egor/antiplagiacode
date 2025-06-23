package ru.itegor.antiplagiacode.clazz;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.itegor.antiplagiacode.clazz.dto.ClassResponseDto;
import ru.itegor.antiplagiacode.clazz.dto.MergeClassRequestDto;
import ru.itegor.antiplagiacode.clazz.service.ClassService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/class")
@Tag(name = "Class", description = "Class API")
public class ClassController {
    private final ClassService classService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get all classes")
    public List<ClassResponseDto> getAll() {
        return classService.getAll();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get class by id")
    public ClassResponseDto getOne(@PathVariable Long id) {
        return classService.getOne(id);
    }

    @GetMapping("/by-ids")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get classes by ids")
    public List<ClassResponseDto> getMany(@RequestParam List<Long> ids) {
        return classService.getMany(ids);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create class")
    public ClassResponseDto create(@RequestBody @Valid MergeClassRequestDto dto) {
        return classService.create(dto);
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Update class by id")
    public ClassResponseDto patch(@PathVariable Long id, @RequestBody @Valid MergeClassRequestDto dto) {
        return classService.patch(id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Delete class by id")
    public ClassResponseDto delete(@PathVariable Long id) {
        return classService.delete(id);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Delete classes by ids")
    public void deleteMany(@RequestParam List<Long> ids) {
        classService.deleteMany(ids);
    }
}
