package JosueDiaz_OsvaldoQuintanar_1B2B_Backend__.modules.evento.model.dto;
import lombok.Data;
import jakarta.validation.constraints.*;

import java.time.LocalDate;
import java.math.BigDecimal;
@Data
public class EventoDTO {
    private Long idEvento;
    private String nombreCliente,nombreSalon,estado;
    private BigDecimal totalPago;
    @NotNull @Positive private Long idCliente;
    @NotNull @Positive private Long idSalon;
    @NotBlank @Size(max=100) private String nombreEvento;
    @NotNull private LocalDate fechaEvento;
    @NotNull @Positive private Integer cantidadPersonas;
    @NotNull @DecimalMin(value="0",inclusive=false) @Digits(integer=8,fraction=2) private BigDecimal cantidadHoras;
}
