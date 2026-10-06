package JosueDiaz_OsvaldoQuintanar_1B2B_Backend__.modules.salon.repository;
import JosueDiaz_OsvaldoQuintanar_1B2B_Backend__.modules.salon.model.entity.SalonEntity;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import jakarta.persistence.LockModeType;
import java.util.Optional;
public interface SalonRepository extends JpaRepository<SalonEntity,Long> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select r from SalonEntity r where r.idSalon=:id")
    Optional<SalonEntity> buscarConBloqueo(@Param("id") Long id);

}
