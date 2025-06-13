package ru.itegor.antiplagiacode.clazz;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.itegor.antiplagiacode.clazz.dto.ClassResponseDto;
import ru.itegor.antiplagiacode.clazz.dto.MergeClassRequestDto;
import ru.itegor.antiplagiacode.clazz.service.ClassService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/class")
public class ClassController {
    private final ClassService classService;

    @GetMapping
    public List<ClassResponseDto> getAll() {
        return classService.getAll();
    }

    @GetMapping("/{id}")
    public ClassResponseDto getOne(@PathVariable Long id) {
        return classService.getOne(id);
    }

    @GetMapping("/by-ids")
    public List<ClassResponseDto> getMany(@RequestParam List<Long> ids) {
        return classService.getMany(ids);
    }

    @PostMapping
    public ClassResponseDto create(@RequestBody @Valid MergeClassRequestDto dto) {
        return classService.create(dto);
    }

    @DeleteMapping("/{id}")
    public ClassResponseDto delete(@PathVariable Long id) {
        return classService.delete(id);
    }

    @DeleteMapping
    public void deleteMany(@RequestParam List<Long> ids) {
        classService.deleteMany(ids);
    }
}
