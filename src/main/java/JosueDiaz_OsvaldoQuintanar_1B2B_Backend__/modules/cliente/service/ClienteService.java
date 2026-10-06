package JosueDiaz_OsvaldoQuintanar_1B2B_Backend__.modules.cliente.service;

import JosueDiaz_OsvaldoQuintanar_1B2B_Backend__.modules.cliente.model.dto.ClienteDTO;
import JosueDiaz_OsvaldoQuintanar_1B2B_Backend__.modules.cliente.model.entity.ClienteEntity;
import JosueDiaz_OsvaldoQuintanar_1B2B_Backend__.modules.cliente.repository.ClienteRepository;
import JosueDiaz_OsvaldoQuintanar_1B2B_Backend__.modules.evento.repository.EventoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Slf4j
@Service
public class ClienteService {
    private final ClienteRepository repo;
    private final EventoRepository eventos;

    public ClienteService(ClienteRepository repo, EventoRepository eventos) {
        this.repo = repo;
        this.eventos = eventos;
    }

    public List<ClienteDTO> obtenerTodos() {
        List<ClienteEntity> entidades = repo.findAll();
        List<ClienteDTO> dtos = new ArrayList<>();
        for (ClienteEntity entity : entidades) {
            dtos.add(convertirADTO(entity));
        }
        return dtos;
    }

    public ClienteDTO obtenerPorId(Long id) {
        Optional<ClienteEntity> entidadOptional = repo.findById(id);
        if (entidadOptional.isPresent()) {
            return convertirADTO(entidadOptional.get());
        }
        return null;
    }

    public ClienteDTO crear(ClienteDTO dto) {
        validarEmail(dto.getEmail(), null);
        validarLongitudes(dto);
        ClienteEntity datosConvertidos = convertirAEntity(dto);
        ClienteEntity respuesta = repo.save(datosConvertidos);
        return convertirADTO(respuesta);
    }

    public ClienteDTO actualizar(Long id, ClienteDTO dto) {
        Optional<ClienteEntity> registroExistente = repo.findById(id);
        if (registroExistente.isPresent()) {
            validarEmail(dto.getEmail(), id);
            validarLongitudes(dto);
            ClienteEntity entidad = registroExistente.get();
            entidad.setNombre(dto.getNombre().trim());
            entidad.setApellido(dto.getApellido().trim());
            entidad.setTelefono(dto.getTelefono().trim());
            entidad.setEmail(dto.getEmail().trim().toLowerCase(Locale.ROOT));
            entidad.setDireccion(dto.getDireccion() == null ? null : dto.getDireccion().trim());
            ClienteEntity datosGuardados = repo.save(entidad);
            return convertirADTO(datosGuardados);
        }
        return null;
    }

    public boolean eliminar(Long id) {
        if (repo.existsById(id)) {
            if (eventos.existsByCliente_IdCliente(id)) {
                throw new IllegalArgumentException("No puedes eliminar este cliente porque tiene eventos registrados.");
            }
            repo.deleteById(id);
            return true;
        }
        return false;
    }

    private ClienteEntity convertirAEntity(ClienteDTO dto) {
        ClienteEntity entity = new ClienteEntity();
        entity.setNombre(dto.getNombre().trim());
        entity.setApellido(dto.getApellido().trim());
        entity.setTelefono(dto.getTelefono().trim());
        entity.setEmail(dto.getEmail().trim().toLowerCase(Locale.ROOT));
        entity.setDireccion(dto.getDireccion() == null ? null : dto.getDireccion().trim());
        return entity;
    }

    private ClienteDTO convertirADTO(ClienteEntity entity) {
        ClienteDTO dto = new ClienteDTO();
        dto.setIdCliente(entity.getIdCliente());
        dto.setNombre(entity.getNombre());
        dto.setApellido(entity.getApellido());
        dto.setTelefono(entity.getTelefono());
        dto.setEmail(entity.getEmail());
        dto.setDireccion(entity.getDireccion());
        return dto;
    }

    private void validarEmail(String email, Long id) {
        List<ClienteEntity> clientes = repo.findAll();
        for (ClienteEntity cliente : clientes) {
            if (!cliente.getIdCliente().equals(id)
                    && cliente.getEmail().trim().equalsIgnoreCase(email.trim())) {
                throw new IllegalArgumentException("El correo ya esta registrado por otro cliente.");
            }
        }
    }

    private void validarLongitudes(ClienteDTO dto) {
        validarBytes(dto.getNombre(), 100);
        validarBytes(dto.getApellido(), 100);
        validarBytes(dto.getTelefono(), 15);
        validarBytes(dto.getEmail(), 100);
        validarBytes(dto.getDireccion(), 200);
    }

    private void validarBytes(String valor, int maximo) {
        if (valor != null && valor.getBytes(StandardCharsets.UTF_8).length > maximo) {
            throw new IllegalArgumentException("El texto supera el limite de " + maximo + " bytes del campo.");
        }
    }
}
