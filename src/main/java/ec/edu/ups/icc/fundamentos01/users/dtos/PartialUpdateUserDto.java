

package ec.edu.ups.icc.fundamentos01.users.dtos;

public class PartialUpdateUserDto {
    private String name;
    private String email;

    public PartialUpdateUserDto() {}

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
}