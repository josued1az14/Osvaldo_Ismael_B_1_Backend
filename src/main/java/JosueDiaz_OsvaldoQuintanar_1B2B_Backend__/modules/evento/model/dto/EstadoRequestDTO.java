package JosueDiaz_OsvaldoQuintanar_1B2B_Backend__.modules.evento.model.dto;
import lombok.Data;
import jakarta.validation.constraints.NotBlank;
@Data
public class EstadoRequestDTO { @NotBlank private String estado; }
