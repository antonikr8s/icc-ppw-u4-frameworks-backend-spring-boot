package ec.edu.ups.icc.fundamentos01.products.repositories;

import ec.edu.ups.icc.fundamentos01.products.entities.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, Long> {

    // --- Métodos de la Práctica 08 ---
    Optional<ProductEntity> findByNameIgnoreCaseAndDeletedFalse(String name);

    List<ProductEntity> findByDeletedFalse();

    Optional<ProductEntity> findByIdAndDeletedFalse(Long id);

    List<ProductEntity> findByOwner_IdAndDeletedFalse(Long ownerId);

    List<ProductEntity> findByCategory_IdAndDeletedFalse(Long categoryId);

    // --- MÉTODOS DE LA PRÁCTICA 09 CORREGIDOS PARA EVITAR EL ERROR LOWER(BYTEA) ---

    /*
     * Busca productos activos de un usuario aplicando filtros opcionales. [cite: 106, 127]
     * Se aplica CAST(:name AS string) para asegurar que PostgreSQL no interprete el parámetro como bytea.
     */
    @Query("""
            SELECT p
            FROM ProductEntity p
            JOIN FETCH p.owner u
            JOIN FETCH p.category c
            WHERE p.deleted = false
              AND u.id = :userId
              AND u.deleted = false
              AND (:name IS NULL OR LOWER(CAST(p.name AS string)) LIKE LOWER(CONCAT('%', CAST(:name AS string), '%')))
              AND (:minPrice IS NULL OR p.price >= :minPrice)
              AND (:maxPrice IS NULL OR p.price <= :maxPrice)
              AND (:categoryId IS NULL OR c.id = :categoryId)
              AND (:categoryId IS NULL OR c.deleted = false)
           """)
    List<ProductEntity> findByOwnerIdWithFilters(
            @Param("userId") Long userId,
            @Param("name") String name,
            @Param("minPrice") Double minPrice,
            @Param("maxPrice") Double maxPrice,
            @Param("categoryId") Long categoryId
    );

    /*
     * Busca productos activos de una categoría aplicando filtros opcionales. [cite: 111, 131]
     * Se aplica CAST(:name AS string) para asegurar la compatibilidad de tipos en PostgreSQL.
     */
    @Query("""
            SELECT p
            FROM ProductEntity p
            JOIN FETCH p.owner u
            JOIN FETCH p.category c
            WHERE p.deleted = false
              AND c.id = :categoryId
              AND c.deleted = false
              AND (:name IS NULL OR LOWER(CAST(p.name AS string)) LIKE LOWER(CONCAT('%', CAST(:name AS string), '%')))
              AND (:minPrice IS NULL OR p.price >= :minPrice)
              AND (:maxPrice IS NULL OR p.price <= :maxPrice)
              AND (:userId IS NULL OR u.id = :userId)
              AND (:userId IS NULL OR u.deleted = false)
           """)
    List<ProductEntity> findByCategoryIdWithFilters(
            @Param("categoryId") Long categoryId,
            @Param("name") String name,
            @Param("minPrice") Double minPrice,
            @Param("maxPrice") Double maxPrice,
            @Param("userId") Long userId
    );
}