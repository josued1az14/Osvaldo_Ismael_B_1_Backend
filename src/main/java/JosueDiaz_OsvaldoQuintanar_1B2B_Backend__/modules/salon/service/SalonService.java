package JosueDiaz_OsvaldoQuintanar_1B2B_Backend__.modules.salon.service;
import JosueDiaz_OsvaldoQuintanar_1B2B_Backend__.modules.salon.model.dto.SalonDTO;
import JosueDiaz_OsvaldoQuintanar_1B2B_Backend__.modules.salon.model.entity.SalonEntity;
import JosueDiaz_OsvaldoQuintanar_1B2B_Backend__.modules.salon.repository.SalonRepository;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;
@Service @Slf4j @Transactional(readOnly=true)
public class SalonService {
    public SalonService(SalonRepository repo) {
        this.repo=repo;
    }

    private final SalonRepository repo;
    public List<SalonDTO> obtenerTodos() { return convertirLista(repo.findAll(Sort.by("idSalon"))); }
    public SalonDTO obtenerPorId(Long id) {
        Optional<SalonEntity> entidadOptional=repo.findById(id);
        if(entidadOptional.isPresent()) return convertirADTO(entidadOptional.get());
        return null;
    }
    private SalonDTO convertirADTO(SalonEntity entity) {
        SalonDTO dto=new SalonDTO();
        dto.setIdSalon(entity.getIdSalon()); dto.setNombreSalon(entity.getNombreSalon());
        dto.setCapacidad(entity.getCapacidad()); dto.setPrecioRenta(entity.getPrecioRenta()); dto.setUbicacion(entity.getUbicacion());
        return dto;
    }

    private List<SalonDTO> convertirLista(List<SalonEntity> entidades) {
        List<SalonDTO> dtos=new java.util.ArrayList<>();
        for(SalonEntity entity:entidades) dtos.add(convertirADTO(entity));
        return dtos;
    }
    private ResponseStatusException noExiste(String recurso) {
        return new ResponseStatusException(HttpStatus.NOT_FOUND,recurso+" no encontrado.");
    }
}
