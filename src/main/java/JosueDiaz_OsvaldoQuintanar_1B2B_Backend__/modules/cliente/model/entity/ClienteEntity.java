package JosueDiaz_OsvaldoQuintanar_1B2B_Backend__.modules.cliente.model.entity;
import jakarta.persistence.*;
import lombok.*;
@Entity @Table(name="CLIENTES") @Getter @Setter @ToString
public class ClienteEntity {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="ID_CLIENTE") private Long idCliente;
    @Column(name="NOMBRE",length=100,nullable=false)
    private String nombre;
    @Column(name="APELLIDO",length=100,nullable=false)
    private String apellido;
    @Column(name="TELEFONO",length=15,nullable=false)
    private String telefono;
    @Column(name="EMAIL",length=100,nullable=false,unique=true)
    private String email;
    @Column(name="DIRECCION",length=200)
    private String direccion;
}
