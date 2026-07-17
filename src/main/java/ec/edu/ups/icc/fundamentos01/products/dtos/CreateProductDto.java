package ec.edu.ups.icc.fundamentos01.products.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.Set;

@Schema(description = "Datos requeridos para la creación de un nuevo producto")
public class CreateProductDto {

    @Schema(
            description = "Nombre comercial o descriptivo del producto",
            example = "Laptop Dell Inspiron 15"
    )
    @NotBlank(message = "El nombre es obligatorio")
    @Size(min = 3, max = 150, message = "El nombre debe tener entre 3 y 150 caracteres")
    private String name;

    @Schema(
            description = "Precio de venta al público",
            example = "850.50"
    )
    @NotNull(message = "El precio es obligatorio")
    @DecimalMin(value = "0.0", inclusive = true, message = "El precio no puede ser negativo")
    private Double price;

    @Schema(
            description = "Cantidad inicial disponible en el inventario",
            example = "25"
    )
    @NotNull(message = "El stock es obligatorio")
    @Min(value = 0, message = "El stock no puede ser negativo")
    private Integer stock;

    // ELIMINADO: userId. El owner ahora se obtiene del token JWT (Práctica 13).

    @Schema(
            description = "Lista de IDs de las categorías a las que pertenece el producto",
            example = "[1, 3, 5]"
    )
    @NotEmpty(message = "Debe seleccionar al menos una categoría")
    private Set<Long> categoryIds;

    public CreateProductDto() {}

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }
    public Integer getStock() { return stock; }
    public void setStock(Integer stock) { this.stock = stock; }
    public Set<Long> getCategoryIds() { return categoryIds; }
    public void setCategoryIds(Set<Long> categoryIds) { this.categoryIds = categoryIds; }
}