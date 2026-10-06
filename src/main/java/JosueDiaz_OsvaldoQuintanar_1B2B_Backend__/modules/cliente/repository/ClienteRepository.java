package JosueDiaz_OsvaldoQuintanar_1B2B_Backend__.modules.cliente.repository;

import JosueDiaz_OsvaldoQuintanar_1B2B_Backend__.modules.cliente.model.entity.ClienteEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClienteRepository extends JpaRepository<ClienteEntity, Long> {
}
