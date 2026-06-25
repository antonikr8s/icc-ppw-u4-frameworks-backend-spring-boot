package ec.edu.ups.icc.fundamentos01.users.dtos;

import jakarta.validation.constraints.*;

public class PartialUpdateUserDto {

    @Size(min = 3, max = 150, message = "El nombre debe tener entre 3 y 150 caracteres")
    private String name;

    @Email(message = "Debe ingresar un email válido")
    @Size(max = 150, message = "El email no debe superar los 150 caracteres")
    private String email;

    public PartialUpdateUserDto() {}

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
}