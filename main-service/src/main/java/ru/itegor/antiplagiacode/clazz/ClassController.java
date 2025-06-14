package ru.itegor.antiplagiacode.clazz;

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
public class ClassController {
    private final ClassService classService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ClassResponseDto> getAll() {
        return classService.getAll();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ClassResponseDto getOne(@PathVariable Long id) {
        return classService.getOne(id);
    }

    @GetMapping("/by-ids")
    @ResponseStatus(HttpStatus.OK)
    public List<ClassResponseDto> getMany(@RequestParam List<Long> ids) {
        return classService.getMany(ids);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ClassResponseDto create(@RequestBody @Valid MergeClassRequestDto dto) {
        return classService.create(dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ClassResponseDto delete(@PathVariable Long id) {
        return classService.delete(id);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.OK)
    public void deleteMany(@RequestParam List<Long> ids) {
        classService.deleteMany(ids);
    }
}
