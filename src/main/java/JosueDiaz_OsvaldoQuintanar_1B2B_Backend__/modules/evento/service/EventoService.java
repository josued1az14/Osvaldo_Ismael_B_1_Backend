package JosueDiaz_OsvaldoQuintanar_1B2B_Backend__.modules.evento.service;
import JosueDiaz_OsvaldoQuintanar_1B2B_Backend__.modules.evento.model.dto.*;
import JosueDiaz_OsvaldoQuintanar_1B2B_Backend__.modules.evento.model.entity.EventoEntity;
import JosueDiaz_OsvaldoQuintanar_1B2B_Backend__.modules.evento.repository.EventoRepository;
import JosueDiaz_OsvaldoQuintanar_1B2B_Backend__.modules.cliente.model.entity.ClienteEntity;
import JosueDiaz_OsvaldoQuintanar_1B2B_Backend__.modules.cliente.repository.ClienteRepository;
import JosueDiaz_OsvaldoQuintanar_1B2B_Backend__.modules.salon.model.entity.SalonEntity;
import JosueDiaz_OsvaldoQuintanar_1B2B_Backend__.modules.salon.repository.SalonRepository;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Sort;

import java.util.*;
import jakarta.validation.Validator;
import java.nio.charset.StandardCharsets;
import java.math.*;
@Service @Slf4j @Transactional
public class EventoService {
    public EventoService(EventoRepository repo,ClienteRepository clientes,SalonRepository salones,Validator validator) {
        this.repo=repo;
        this.clientes=clientes;
        this.salones=salones;
        this.validator=validator;
    }

    private final Validator validator;
    private final EventoRepository repo;
    private final ClienteRepository clientes;
    private final SalonRepository salones;
    @Transactional(readOnly=true) public List<EventoDTO> obtenerTodos() { return convertirLista(repo.findAll(Sort.by("fechaEvento").descending().and(Sort.by("idEvento").descending()))); }
    @Transactional(readOnly=true) public EventoDTO obtenerPorId(Long id) {
        Optional<EventoEntity> entidadOptional=repo.findById(id);
        if(entidadOptional.isPresent()) return convertirADTO(entidadOptional.get());
        return null;
    }
    public EventoDTO crear(EventoDTO dto) {
        validarEntrada(dto);
        SalonEntity salon=bloquearSalon(dto.getIdSalon());
        ClienteEntity cliente=clientes.findById(dto.getIdCliente()).orElseThrow(()->noExiste("ClienteEntity"));
        EventoEntity entity=convertirAEntity(dto,cliente,salon);
        entity.setEstado("CONFIRMADO");
        validar(entity,-1L);
        return convertirADTO(repo.saveAndFlush(entity));
    }
    public EventoDTO actualizar(Long id,EventoDTO dto) {
        validarEntrada(dto);
        EventoEntity actual=repo.findById(id).orElse(null);
        if(actual==null) return null;
        bloquearSalones(actual.getSalon().getIdSalon(),dto.getIdSalon());
        SalonEntity salon=bloquearSalon(dto.getIdSalon());
        ClienteEntity cliente=clientes.findById(dto.getIdCliente()).orElseThrow(()->noExiste("ClienteEntity"));
        EventoEntity nuevo=convertirAEntity(dto,cliente,salon); nuevo.setIdEvento(id); nuevo.setEstado(actual.getEstado());
        validar(nuevo,id);
        actual.setCliente(cliente); actual.setSalon(salon); actual.setNombreEvento(nuevo.getNombreEvento());
        actual.setFechaEvento(nuevo.getFechaEvento()); actual.setCantidadPersonas(nuevo.getCantidadPersonas());
        actual.setCantidadHoras(nuevo.getCantidadHoras()); actual.setTotalPago(nuevo.getTotalPago());
        return convertirADTO(repo.saveAndFlush(actual));
    }
    public EventoDTO cambiarEstado(Long id,String estado) {
        EventoEntity actual=entidad(id); bloquearSalon(actual.getSalon().getIdSalon());
        String canonico=normalizarEstado(estado);
        actual.setEstado(canonico); validar(actual,id);
        return convertirADTO(repo.saveAndFlush(actual));
    }
    public boolean eliminar(Long id) {
        Optional<EventoEntity> actual=repo.findById(id);
        if(actual.isEmpty()) return false;
        bloquearSalon(actual.get().getSalon().getIdSalon());
        repo.deleteById(id); repo.flush(); return true;
    }
    private EventoEntity entidad(Long id) { return repo.findById(id).orElseThrow(()->noExiste("EventoEntity")); }
    private SalonEntity bloquearSalon(Long id) { return salones.buscarConBloqueo(id).orElseThrow(()->noExiste("SalonEntity")); }
    private void bloquearSalones(Long a,Long b) { java.util.stream.Stream.of(a,b).distinct().sorted().forEach(this::bloquearSalon); }
    private boolean activo(String estado) { return Set.of("PENDIENTE","CONFIRMADO","CONFIRMADA").contains(estado); }
    private void validar(EventoEntity entity,Long excluirId) {
        SalonEntity salon=entity.getSalon();
        if(salon.getCapacidad()==null || salon.getCapacidad()<=0 || salon.getPrecioRenta()==null || salon.getPrecioRenta().signum()<=0) throw new IllegalArgumentException("El salon tiene capacidad o precio invalidos en la base de datos.");
        if(entity.getCantidadPersonas()>salon.getCapacidad()) throw new IllegalArgumentException("La cantidad de personas supera la capacidad del salon ("+salon.getCapacidad()+").");
        if(activo(entity.getEstado()) && repo.contarActivos(salon.getIdSalon(),entity.getFechaEvento(),excluirId)>0) throw new IllegalArgumentException("El salon ya tiene un evento activo en esa fecha.");
        if(entity.getTotalPago()==null || entity.getTotalPago().compareTo(new BigDecimal("999999.99"))>0) throw new IllegalArgumentException("El total supera el limite de TOTAL_PAGO: 999999.99.");
    }
    private String normalizarEstado(String estado) {
        String e=estado.trim().toUpperCase(Locale.ROOT);
        if(e.equals("CONFIRMADA")) e="CONFIRMADO";
        if(e.equals("COMPLETADA")) e="FINALIZADO";
        if(!Set.of("PENDIENTE","CONFIRMADO","CANCELADO","FINALIZADO").contains(e)) throw new IllegalArgumentException("Estado invalido. Usa PENDIENTE, CONFIRMADO, CANCELADO o FINALIZADO.");
        return e;
    }
    private EventoEntity convertirAEntity(EventoDTO dto,ClienteEntity cliente,SalonEntity salon) {
        if(salon.getPrecioRenta()==null) throw new IllegalArgumentException("El salon no tiene precio de renta.");
        EventoEntity entity=new EventoEntity();
        entity.setCliente(cliente); entity.setSalon(salon); entity.setNombreEvento(dto.getNombreEvento().trim());
        entity.setFechaEvento(dto.getFechaEvento()); entity.setCantidadPersonas(dto.getCantidadPersonas()); entity.setCantidadHoras(dto.getCantidadHoras());
        entity.setTotalPago(dto.getCantidadHoras().multiply(salon.getPrecioRenta()).setScale(2,RoundingMode.HALF_UP));
        return entity;
    }
    private EventoDTO convertirADTO(EventoEntity entity) {
        EventoDTO dto=new EventoDTO();
        dto.setIdEvento(entity.getIdEvento()); dto.setIdCliente(entity.getCliente().getIdCliente()); dto.setIdSalon(entity.getSalon().getIdSalon());
        dto.setNombreCliente(entity.getCliente().getNombre()+" "+entity.getCliente().getApellido()); dto.setNombreSalon(entity.getSalon().getNombreSalon());
        dto.setNombreEvento(entity.getNombreEvento()); dto.setFechaEvento(entity.getFechaEvento()); dto.setCantidadPersonas(entity.getCantidadPersonas());
        dto.setCantidadHoras(entity.getCantidadHoras()); dto.setTotalPago(entity.getTotalPago()); dto.setEstado(entity.getEstado());
        return dto;
    }

    private List<EventoDTO> convertirLista(List<EventoEntity> entidades) {
        List<EventoDTO> dtos=new java.util.ArrayList<>();
        for(EventoEntity entity:entidades) dtos.add(convertirADTO(entity));
        return dtos;
    }
    private ResponseStatusException noExiste(String recurso) {
        return new ResponseStatusException(HttpStatus.NOT_FOUND,recurso+" no encontrado.");
    }

    private void validarEntrada(EventoDTO dto) {
        var errores=validator.validate(dto);
        if(!errores.isEmpty()) throw new IllegalArgumentException(errores.iterator().next().getMessage());
        validarBytes(dto.getNombreEvento(),100);
    }
    private void validarBytes(String value,int max) {
        if(value!=null && value.getBytes(StandardCharsets.UTF_8).length>max) throw new IllegalArgumentException("El texto supera el limite de "+max+" bytes del campo.");
    }
}
