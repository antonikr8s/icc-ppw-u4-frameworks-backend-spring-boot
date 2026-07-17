package ec.edu.ups.icc.fundamentos01.products.controllers;

import ec.edu.ups.icc.fundamentos01.core.dtos.PaginationDto;
import ec.edu.ups.icc.fundamentos01.products.dtos.*;
import ec.edu.ups.icc.fundamentos01.products.services.ProductService;
import ec.edu.ups.icc.fundamentos01.security.services.UserDetailsImpl;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Slice;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import java.util.List;
import java.util.Map;

@Tag(
        name = "Productos",
        description = "Gestión de productos con paginación, roles y ownership"
)
@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/products")
public class ProductsController {

    private final ProductService service;

    public ProductsController(ProductService service) {
        this.service = service;
    }

    @Operation(
            summary = "Listar todos los productos (Sin paginar)",
            description = "Devuelve una lista completa de productos. Solo accesible para administradores."
    )
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<ProductResponseDto> findAll() {
        return service.findAll();
    }

    @Operation(
            summary = "Buscar producto por ID",
            description = "Obtiene los detalles de un producto específico mediante su identificador."
    )
    @GetMapping("/{id}")
    public ProductResponseDto findOne(@PathVariable Long id) {
        return service.findOne(id);
    }

    @Operation(
            summary = "Listar productos con Paginación (Page)",
            description = "Devuelve los productos paginados, incluyendo el conteo total de elementos."
    )
    @GetMapping("/page")
    public Page<ProductResponseDto> findAllPage(@Valid @ModelAttribute PaginationDto pagination) {
        return service.findAllPage(pagination);
    }

    @Operation(
            summary = "Listar productos con Paginación (Slice)",
            description = "Devuelve una porción de productos sin realizar el conteo total, ideal para scroll infinito. Aplica filtros de ownership."
    )
    @GetMapping("/slice")
    public Slice<ProductResponseDto> findAllSlice(
            @Valid @ModelAttribute PaginationDto pagination,
            @Parameter(hidden = true) @AuthenticationPrincipal UserDetailsImpl currentUser
    ) {
        return service.findAllSlice(pagination, currentUser);
    }

    @Operation(
            summary = "Crear un nuevo producto",
            description = "Registra un producto en el sistema. El owner se asigna automáticamente a través del token JWT."
    )
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProductResponseDto create(
            @Valid @RequestBody CreateProductDto dto,
            @Parameter(hidden = true) @AuthenticationPrincipal UserDetailsImpl currentUser
    ) {
        return service.create(dto, currentUser);
    }

    @Operation(
            summary = "Actualización total de un producto",
            description = "Reemplaza todos los datos de un producto existente. Se valida el ownership del usuario actual."
    )
    @PutMapping("/{id}")
    public ProductResponseDto update(
            @PathVariable Long id,
            @Valid @RequestBody UpdateProductDto dto,
            @Parameter(hidden = true) @AuthenticationPrincipal UserDetailsImpl currentUser
    ) {
        return service.update(id, dto, currentUser);
    }

    @Operation(
            summary = "Actualización parcial de un producto (PATCH)",
            description = "Modifica únicamente los campos enviados en la petición. Se valida el ownership."
    )
    @PatchMapping("/{id}")
    public ProductResponseDto partialUpdate(
            @PathVariable Long id,
            @Valid @RequestBody PartialUpdateProductDto dto,
            @Parameter(hidden = true) @AuthenticationPrincipal UserDetailsImpl currentUser
    ) {
        return service.partialUpdate(id, dto, currentUser);
    }

    @Operation(
            summary = "Eliminar un producto",
            description = "Borra un producto del sistema mediante su ID. Requiere ownership o rol de administrador."
    )
    @DeleteMapping("/{id}")
    public Map<String, String> delete(
            @PathVariable Long id,
            @Parameter(hidden = true) @AuthenticationPrincipal UserDetailsImpl currentUser
    ) {
        service.delete(id, currentUser);
        return Map.of("message", "Deleted successfully");
    }
}