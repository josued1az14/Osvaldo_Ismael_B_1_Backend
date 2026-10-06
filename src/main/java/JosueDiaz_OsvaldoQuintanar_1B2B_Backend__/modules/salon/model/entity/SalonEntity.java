package JosueDiaz_OsvaldoQuintanar_1B2B_Backend__.modules.salon.model.entity;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
@Entity @Table(name="SALONES") @Getter @Setter @NoArgsConstructor
public class SalonEntity {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="ID_SALON") private Long idSalon;
    @Column(name="NOMBRE_SALON",length=100,nullable=false)
    private String nombreSalon;
    @Column(name="CAPACIDAD",nullable=false)
    private Integer capacidad;
    @Column(name="PRECIO_RENTA",precision=10,scale=2,nullable=false)
    private BigDecimal precioRenta;
    @Column(name="UBICACION",length=100)
    private String ubicacion;
}
