package ec.edu.ups.icc.fundamentos01.users.repositories;

import ec.edu.ups.icc.fundamentos01.users.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    // Tu método original intacto
    Optional<UserEntity> findByEmail(String email);

    // Nuevo método necesario para validar relaciones en productos
    boolean existsByIdAndDeletedFalse(Long id);

    // ============== NUEVOS MÉTODOS PARA SEGURIDAD (Práctica 11) ==============

    // Usado en login: un usuario eliminado lógicamente no puede autenticarse
    Optional<UserEntity> findByEmailAndDeletedFalse(String email);

    // Usado en registro: evitar emails duplicados
    boolean existsByEmail(String email);
}