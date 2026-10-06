package JosueDiaz_OsvaldoQuintanar_1B2B_Backend__.modules.salon.model.dto;
import lombok.Data;
import java.math.BigDecimal;
@Data
public class SalonDTO {
    private Long idSalon;
    private String nombreSalon,ubicacion;
    private Integer capacidad;
    private BigDecimal precioRenta;
}
