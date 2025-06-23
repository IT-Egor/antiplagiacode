package ru.itegor.antiplagiacode.user;

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
import ru.itegor.antiplagiacode.user.dto.MergeUserRequestDto;
import ru.itegor.antiplagiacode.user.dto.UserResponseDto;
import ru.itegor.antiplagiacode.user.service.UserService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
@Tag(name = "User", description = "User API")
public class UserController {
    private final UserService userService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get all users")
    public PagedModel<UserResponseDto> getAll(@ParameterObject Pageable pageable) {
        Page<UserResponseDto> userResponseDtos = userService.getAll(pageable);
        return new PagedModel<>(userResponseDtos);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get user by id")
    public UserResponseDto getOne(@PathVariable Long id) {
        return userService.getOne(id);
    }

    @GetMapping("/by-ids")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get users by ids")
    public List<UserResponseDto> getMany(@RequestParam List<Long> ids) {
        return userService.getMany(ids);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create user")
    public UserResponseDto create(@RequestBody @Valid MergeUserRequestDto dto) {
        return userService.create(dto);
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Update user by id")
    public UserResponseDto patch(@PathVariable Long id, @RequestBody MergeUserRequestDto dto) {
        return userService.patch(id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Delete user by id")
    public UserResponseDto delete(@PathVariable Long id) {
        return userService.delete(id);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Delete users by ids")
    public void deleteMany(@RequestParam List<Long> ids) {
        userService.deleteMany(ids);
    }
}
