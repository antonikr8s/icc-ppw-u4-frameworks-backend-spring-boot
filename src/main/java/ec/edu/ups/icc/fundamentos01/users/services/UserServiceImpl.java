package ec.edu.ups.icc.fundamentos01.users.services;

import ec.edu.ups.icc.fundamentos01.users.dtos.*;
import ec.edu.ups.icc.fundamentos01.users.entities.UserEntity;
import ec.edu.ups.icc.fundamentos01.users.mappers.UserMapper;
import ec.edu.ups.icc.fundamentos01.users.models.UserModel;
import ec.edu.ups.icc.fundamentos01.users.repositories.UserRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<UserResponseDto> findAll() {
        return userRepository.findAll()
                .stream()
                .map(UserMapper::toModelFromEntity)
                .map(UserMapper::toResponse)
                .toList();
    }

    @Override
    public UserResponseDto findOne(Long id) {
        return userRepository.findById(id)
                .map(UserMapper::toModelFromEntity)
                .map(UserMapper::toResponse)
                .orElseThrow(() -> new IllegalStateException("User not found"));
    }


    @Override
    public UserResponseDto create(CreateUserDto dto) {
        if (userRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new IllegalStateException("Email already registered");
        }
        UserModel model = UserMapper.toModelFromDTO(dto);
        UserEntity entity = UserMapper.toEntityFromModel(model);
        UserEntity saved = userRepository.save(entity);
        return UserMapper.toResponse(UserMapper.toModelFromEntity(saved));
    }

    @Override
    public UserResponseDto update(Long id, UpdateUserDto dto) {
        UserEntity entity = userRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("User not found"));
        entity.setName(dto.getName());
        entity.setEmail(dto.getEmail());
        UserEntity saved = userRepository.save(entity);
        return UserMapper.toResponse(UserMapper.toModelFromEntity(saved));
    }

    @Override
    public UserResponseDto partialUpdate(Long id, PartialUpdateUserDto dto) {
        UserEntity entity = userRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("User not found"));
        if (dto.getName() != null) entity.setName(dto.getName());
        if (dto.getEmail() != null) entity.setEmail(dto.getEmail());
        UserEntity saved = userRepository.save(entity);
        return UserMapper.toResponse(UserMapper.toModelFromEntity(saved));
    }

    @Override
    public void delete(Long id) {
        UserEntity entity = userRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("User not found"));
        entity.setDeleted(true);
        userRepository.save(entity);
    }
}
/*package ec.edu.ups.icc.fundamentos01.users.services;

import ec.edu.ups.icc.fundamentos01.core.dto.ErrorResponseDto;
import ec.edu.ups.icc.fundamentos01.users.dtos.*;
import ec.edu.ups.icc.fundamentos01.users.mappers.UserMapper;
import ec.edu.ups.icc.fundamentos01.users.models.UserModel;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private List<UserModel> users = new ArrayList<>();
    private Long currentId = 1L;

    @Override
    public List<UserResponseDto> findAll() {
        return users.stream().map(UserMapper::toResponse).toList();
    }

    @Override
    public Object findOne(Long id) {
        return users.stream()
                .filter(u -> u.getId().equals(id))
                .findFirst()
                .map(u -> (Object) UserMapper.toResponse(u))
                .orElseGet(() -> new ErrorResponseDto("User not found"));
    }

    @Override
    public UserResponseDto create(CreateUserDto dto) {
        UserModel user = UserMapper.toModel(dto);
        user.setId(currentId++);
        users.add(user);
        return UserMapper.toResponse(user);
    }

    @Override
    public Object update(Long id, UpdateUserDto dto) {
        UserModel user = users.stream()
                .filter(u -> u.getId().equals(id))
                .findFirst().orElse(null);
        if (user == null) return new ErrorResponseDto("User not found");
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        return UserMapper.toResponse(user);
    }

    @Override
    public Object partialUpdate(Long id, PartialUpdateUserDto dto) {
        UserModel user = users.stream()
                .filter(u -> u.getId().equals(id))
                .findFirst().orElse(null);
        if (user == null) return new ErrorResponseDto("User not found");
        if (dto.getName() != null) user.setName(dto.getName());
        if (dto.getEmail() != null) user.setEmail(dto.getEmail());
        return UserMapper.toResponse(user);
    }

    @Override
    public Object delete(Long id) {
        boolean removed = users.removeIf(u -> u.getId().equals(id));
        if (!removed) return new ErrorResponseDto("User not found");
        return new java.util.HashMap<String, String>() {{ put("message", "Deleted successfully"); }};
    }
}

 */