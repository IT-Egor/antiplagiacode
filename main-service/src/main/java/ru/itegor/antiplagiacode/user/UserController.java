package ru.itegor.antiplagiacode.user;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.web.bind.annotation.*;
import ru.itegor.antiplagiacode.user.dto.MergeUserRequestDto;
import ru.itegor.antiplagiacode.user.dto.UserResponseDto;
import ru.itegor.antiplagiacode.user.service.UserService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
public class UserController {
    private final UserService userService;

    @GetMapping
    public PagedModel<UserResponseDto> getAll(@ParameterObject Pageable pageable) {
        Page<UserResponseDto> userResponseDtos = userService.getAll(pageable);
        return new PagedModel<>(userResponseDtos);
    }

    @GetMapping("/{id}")
    public UserResponseDto getOne(@PathVariable Long id) {
        return userService.getOne(id);
    }

    @GetMapping("/by-ids")
    public List<UserResponseDto> getMany(@RequestParam List<Long> ids) {
        return userService.getMany(ids);
    }

    @PostMapping
    public UserResponseDto create(@RequestBody @Valid MergeUserRequestDto dto) {
        return userService.create(dto);
    }

    @PatchMapping("/{id}")
    public UserResponseDto patch(@PathVariable Long id, @RequestBody MergeUserRequestDto dto) {
        return userService.patch(id, dto);
    }

    @DeleteMapping("/{id}")
    public UserResponseDto delete(@PathVariable Long id) {
        return userService.delete(id);
    }

    @DeleteMapping
    public void deleteMany(@RequestParam List<Long> ids) {
        userService.deleteMany(ids);
    }
}
