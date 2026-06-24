package ec.edu.ups.icc.fundamentos01.users.controllers;

import ec.edu.ups.icc.fundamentos01.users.dtos.*;
import ec.edu.ups.icc.fundamentos01.users.services.UserService;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class UsersController {

    private final UserService service;

    public UsersController(UserService service) {
        this.service = service;
    }

    @GetMapping
    public List<UserResponseDto> findAll() { return service.findAll(); }

    @GetMapping("/{id}")
    public UserResponseDto findOne(@PathVariable Long id) { return service.findOne(id); }

    @PostMapping
    public UserResponseDto create(@RequestBody CreateUserDto dto) { return service.create(dto); }

    @PutMapping("/{id}")
    public UserResponseDto update(@PathVariable Long id, @RequestBody UpdateUserDto dto) {
        return service.update(id, dto);
    }

    @PatchMapping("/{id}")
    public UserResponseDto partialUpdate(@PathVariable Long id, @RequestBody PartialUpdateUserDto dto) {
        return service.partialUpdate(id, dto);
    }

    @DeleteMapping("/{id}")
    public Map<String, String> delete(@PathVariable Long id) {
        service.delete(id);
        return Map.of("message", "Deleted successfully");
    }
}
/*package ec.edu.ups.icc.fundamentos01.users.controllers;

import ec.edu.ups.icc.fundamentos01.users.dtos.*;
import ec.edu.ups.icc.fundamentos01.users.services.UserService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UsersController {

    private final UserService service;

    public UsersController(UserService service) {
        this.service = service;
    }

    @GetMapping
    public List<UserResponseDto> findAll() { return service.findAll(); }

    @GetMapping("/{id}")
    public Object findOne(@PathVariable Long id) { return service.findOne(id); }

    @PostMapping
    public UserResponseDto create(@RequestBody CreateUserDto dto) { return service.create(dto); }

    @PutMapping("/{id}")
    public Object update(@PathVariable Long id, @RequestBody UpdateUserDto dto) { return service.update(id, dto); }

    @PatchMapping("/{id}")
    public Object partialUpdate(@PathVariable Long id, @RequestBody PartialUpdateUserDto dto) { return service.partialUpdate(id, dto); }

    @DeleteMapping("/{id}")
    public Object delete(@PathVariable Long id) { return service.delete(id); }
}

 */