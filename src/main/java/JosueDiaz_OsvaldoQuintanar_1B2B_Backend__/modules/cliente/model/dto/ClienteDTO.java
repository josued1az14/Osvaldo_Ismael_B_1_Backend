package JosueDiaz_OsvaldoQuintanar_1B2B_Backend__.modules.cliente.model.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ClienteDTO {
    private Long idCliente;

    @NotBlank(message = "El nombre es obligatorio.")
    @Size(max = 100, message = "El nombre no debe superar 100 caracteres.")
    private String nombre;

    @NotBlank(message = "El apellido es obligatorio.")
    @Size(max = 100, message = "El apellido no debe superar 100 caracteres.")
    private String apellido;

    @NotBlank(message = "El telefono es obligatorio.")
    @Size(max = 15, message = "El telefono no debe superar 15 caracteres.")
    private String telefono;

    @NotBlank(message = "El correo es obligatorio.")
    @Email(message = "El correo debe tener un formato valido.")
    @Size(max = 100, message = "El correo no debe superar 100 caracteres.")
    private String email;

    @Size(max = 200, message = "La direccion no debe superar 200 caracteres.")
    private String direccion;
}
