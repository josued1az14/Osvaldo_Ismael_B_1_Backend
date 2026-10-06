package JosueDiaz_OsvaldoQuintanar_1B2B_Backend__.modules.evento.model.entity;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.math.BigDecimal;
import JosueDiaz_OsvaldoQuintanar_1B2B_Backend__.modules.cliente.model.entity.ClienteEntity;
import JosueDiaz_OsvaldoQuintanar_1B2B_Backend__.modules.salon.model.entity.SalonEntity;
@Entity @Table(name="EVENTOS") @Getter @Setter @NoArgsConstructor
public class EventoEntity {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="ID_EVENTO") private Long idEvento;
    @ManyToOne(fetch=FetchType.LAZY,optional=false) @JoinColumn(name="ID_CLIENTE",nullable=false) private ClienteEntity cliente;
    @ManyToOne(fetch=FetchType.LAZY,optional=false) @JoinColumn(name="ID_SALON",nullable=false) private SalonEntity salon;
    @Column(name="NOMBRE_EVENTO",length=100,nullable=false) private String nombreEvento;
    @Column(name="FECHA_EVENTO",nullable=false) private LocalDate fechaEvento;
    @Column(name="CANTIDAD_PERSONAS",nullable=false) private Integer cantidadPersonas;
    @Column(name="CANTIDAD_HORAS") private BigDecimal cantidadHoras;
    @Column(name="ESTADO",length=20) private String estado;
    @Column(name="TOTAL_PAGO",precision=8,scale=2) private BigDecimal totalPago;
}
