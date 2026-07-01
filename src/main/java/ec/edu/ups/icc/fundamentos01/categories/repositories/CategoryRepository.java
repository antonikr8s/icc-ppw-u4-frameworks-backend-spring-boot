package ec.edu.ups.icc.fundamentos01.categories.repositories;

import ec.edu.ups.icc.fundamentos01.categories.entities.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<CategoryEntity, Long> {

    Optional<CategoryEntity> findByNameIgnoreCaseAndDeletedFalse(String name);

    boolean existsByNameIgnoreCaseAndDeletedFalse(String name);

    boolean existsByIdAndDeletedFalse(Long id); // Necesario para validar antes de crear productos [cite: 209]

    List<CategoryEntity> findByDeletedFalse();
}