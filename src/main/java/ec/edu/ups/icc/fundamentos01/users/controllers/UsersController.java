package ec.edu.ups.icc.fundamentos01.users.controllers;

import ec.edu.ups.icc.fundamentos01.users.dtos.CreateUserDto;
import ec.edu.ups.icc.fundamentos01.users.dtos.UpdateUserDto;
import ec.edu.ups.icc.fundamentos01.users.dtos.PartialUpdateUserDto;
import ec.edu.ups.icc.fundamentos01.users.dtos.UserResponseDto;
import ec.edu.ups.icc.fundamentos01.users.mappers.UserMapper;
import ec.edu.ups.icc.fundamentos01.users.models.UserModel;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class UsersController {

    private List<UserModel> users = new ArrayList<>();
    private long currentId = 1; // Contador para autogenerar IDs únicos

    // 1. GET ALL
    @GetMapping
    public List<UserResponseDto> findAll() {
        return users.stream()
                .map(UserMapper::toResponse)
                .toList();
    }

    // 2. GET BY ID
    @GetMapping("/{id}")
    public Object findOne(@PathVariable long id) {
        return users.stream()
                .filter(u -> u.getId().equals(id))
                .findFirst()
                .map(user -> (Object) UserMapper.toResponse(user))
                .orElse(Map.of("error", "User not found"));
    }

    // 3. POST (Corrección: Aquí se asigna el ID secuencial antes de guardar)
    @PostMapping
    public UserResponseDto create(@RequestBody CreateUserDto dto) {
        UserModel user = UserMapper.toModel(dto);
        user.setId(currentId++); // Asigna el ID actual e incrementa para el siguiente
        users.add(user);
        return UserMapper.toResponse(user);
    }

    // 4. PUT
    @PutMapping("/{id}")
    public Object update(@PathVariable long id, @RequestBody UpdateUserDto dto) {
        UserModel user = users.stream()
                .filter(u -> u.getId().equals(id))
                .findFirst()
                .orElse(null);

        if (user == null) {
            return Map.of("error", "UserModel not found");
        }

        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        return UserMapper.toResponse(user);
    }

    // 5. PATCH
    @PatchMapping("/{id}")
    public Object partialUpdate(@PathVariable long id, @RequestBody PartialUpdateUserDto dto) {
        UserModel user = users.stream()
                .filter(u -> u.getId().equals(id))
                .findFirst()
                .orElse(null);

        if (user == null) {
            return Map.of("error", "UserModel not found");
        }

        if (dto.getName() != null) user.setName(dto.getName());
        if (dto.getEmail() != null) user.setEmail(dto.getEmail());

        return UserMapper.toResponse(user);
    }

    // 6. DELETE
    @DeleteMapping("/{id}")
    public Object delete(@PathVariable long id) {
        boolean exists = users.removeIf(u -> u.getId().equals(id));
        if (!exists) {
            return Map.of("error", "User not found");
        }
        return Map.of("message", "Deleted successfully");
    }
}
