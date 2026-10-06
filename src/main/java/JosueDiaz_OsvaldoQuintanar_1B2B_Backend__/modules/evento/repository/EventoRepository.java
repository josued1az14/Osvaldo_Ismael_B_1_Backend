package JosueDiaz_OsvaldoQuintanar_1B2B_Backend__.modules.evento.repository;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import JosueDiaz_OsvaldoQuintanar_1B2B_Backend__.modules.evento.model.entity.EventoEntity;
import java.time.LocalDate;
public interface EventoRepository extends JpaRepository<EventoEntity,Long> {
    boolean existsByCliente_IdCliente(Long idCliente);
    @Query("select count(e) from EventoEntity e where e.salon.idSalon=:salon and e.fechaEvento=:fecha and e.idEvento<>:id and upper(e.estado) in ('PENDIENTE','CONFIRMADO','CONFIRMADA')")
    long contarActivos(@Param("salon") Long salon,@Param("fecha") LocalDate fecha,@Param("id") Long id);
}
