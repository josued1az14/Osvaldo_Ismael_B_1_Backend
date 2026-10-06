package JosueDiaz_OsvaldoQuintanar_1B2B_Backend__.modules.evento.controller;
import JosueDiaz_OsvaldoQuintanar_1B2B_Backend__.modules.evento.model.dto.*;
import JosueDiaz_OsvaldoQuintanar_1B2B_Backend__.modules.evento.service.EventoService;
import JosueDiaz_OsvaldoQuintanar_1B2B_Backend__.apiResponse.APIResponse;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.*;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.validation.BindingResult;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import java.util.List;
@Slf4j
@RestController
@RequestMapping("/api/evento")
@CrossOrigin(origins="*")
public class EventoController {
    private final EventoService service;
    public EventoController(EventoService service) { this.service=service; }

    @GetMapping
    public ResponseEntity<APIResponse<List<EventoDTO>>> obtenerDatos() {
        try { return ResponseEntity.ok(new APIResponse<>(true,"Proceso completado",service.obtenerTodos())); }

        catch(ResponseStatusException e) { return ResponseEntity.status(e.getStatusCode()).body(new APIResponse<>(false,e.getReason())); }
        catch(IllegalArgumentException e) { return ResponseEntity.badRequest().body(new APIResponse<>(false,e.getMessage())); }
        catch(DataIntegrityViolationException e) { return ResponseEntity.status(HttpStatus.CONFLICT).body(new APIResponse<>(false,"El registro tiene datos duplicados o relaciones con otros registros.")); }
        catch(Exception e) { log.error("Error en EventoController",e); return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new APIResponse<>(false,"No se pudo completar el proceso.")); }

    }
    @GetMapping("/{id}")
    public ResponseEntity<APIResponse<EventoDTO>> obtenerDatosId(@PathVariable Long id) {
        try {
            EventoDTO dto=service.obtenerPorId(id);
            if(dto!=null) return ResponseEntity.ok(new APIResponse<>(true,"Registro encontrado",dto));
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new APIResponse<>(false,"Registro no encontrado"));
        }

        catch(ResponseStatusException e) { return ResponseEntity.status(e.getStatusCode()).body(new APIResponse<>(false,e.getReason())); }
        catch(IllegalArgumentException e) { return ResponseEntity.badRequest().body(new APIResponse<>(false,e.getMessage())); }
        catch(DataIntegrityViolationException e) { return ResponseEntity.status(HttpStatus.CONFLICT).body(new APIResponse<>(false,"El registro tiene datos duplicados o relaciones con otros registros.")); }
        catch(Exception e) { log.error("Error en EventoController",e); return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new APIResponse<>(false,"No se pudo completar el proceso.")); }

    }

    @PostMapping
    public ResponseEntity<APIResponse<EventoDTO>> nuevoEvento(@Valid @RequestBody EventoDTO json,BindingResult errores) {
        if(errores.hasErrors()) return ResponseEntity.badRequest().body(new APIResponse<>(false,mensajeValidacion(errores)));
        try { return ResponseEntity.status(HttpStatus.CREATED).body(new APIResponse<>(true,"Registro creado",service.crear(json))); }

        catch(ResponseStatusException e) { return ResponseEntity.status(e.getStatusCode()).body(new APIResponse<>(false,e.getReason())); }
        catch(IllegalArgumentException e) { return ResponseEntity.badRequest().body(new APIResponse<>(false,e.getMessage())); }
        catch(DataIntegrityViolationException e) { return ResponseEntity.status(HttpStatus.CONFLICT).body(new APIResponse<>(false,"El registro tiene datos duplicados o relaciones con otros registros.")); }
        catch(Exception e) { log.error("Error en EventoController",e); return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new APIResponse<>(false,"No se pudo completar el proceso.")); }

    }
    @PutMapping("/{id}")
    public ResponseEntity<APIResponse<EventoDTO>> actualizarEvento(@PathVariable Long id,@Valid @RequestBody EventoDTO json,BindingResult errores) {
        if(errores.hasErrors()) return ResponseEntity.badRequest().body(new APIResponse<>(false,mensajeValidacion(errores)));
        try {
            EventoDTO dto=service.actualizar(id,json);
            if(dto!=null) return ResponseEntity.ok(new APIResponse<>(true,"Registro actualizado",dto));
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new APIResponse<>(false,"Registro no encontrado"));
        }

        catch(ResponseStatusException e) { return ResponseEntity.status(e.getStatusCode()).body(new APIResponse<>(false,e.getReason())); }
        catch(IllegalArgumentException e) { return ResponseEntity.badRequest().body(new APIResponse<>(false,e.getMessage())); }
        catch(DataIntegrityViolationException e) { return ResponseEntity.status(HttpStatus.CONFLICT).body(new APIResponse<>(false,"El registro tiene datos duplicados o relaciones con otros registros.")); }
        catch(Exception e) { log.error("Error en EventoController",e); return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new APIResponse<>(false,"No se pudo completar el proceso.")); }

    }
    @DeleteMapping("/{id}")
    public ResponseEntity<APIResponse<Void>> eliminarDatos(@PathVariable Long id) {
        try {
            boolean respuesta=service.eliminar(id);
            if(respuesta) return ResponseEntity.ok(new APIResponse<>(true,"Registro eliminado"));
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new APIResponse<>(false,"Registro no encontrado"));
        }

        catch(ResponseStatusException e) { return ResponseEntity.status(e.getStatusCode()).body(new APIResponse<>(false,e.getReason())); }
        catch(IllegalArgumentException e) { return ResponseEntity.badRequest().body(new APIResponse<>(false,e.getMessage())); }
        catch(DataIntegrityViolationException e) { return ResponseEntity.status(HttpStatus.CONFLICT).body(new APIResponse<>(false,"El registro tiene datos duplicados o relaciones con otros registros.")); }
        catch(Exception e) { log.error("Error en EventoController",e); return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new APIResponse<>(false,"No se pudo completar el proceso.")); }

    }
    private String mensajeValidacion(BindingResult errores) {
        return errores.getFieldErrors().stream().map(error->error.getField()+": "+error.getDefaultMessage()).collect(java.util.stream.Collectors.joining("; "));
    }

    @PutMapping("/{id}/estado")
    public ResponseEntity<APIResponse<EventoDTO>> cambiarEstado(@PathVariable Long id,@Valid @RequestBody EstadoRequestDTO json,BindingResult errores) {
        if(errores.hasErrors()) return ResponseEntity.badRequest().body(new APIResponse<>(false,mensajeValidacion(errores)));
        try { return ResponseEntity.ok(new APIResponse<>(true,"Estado actualizado",service.cambiarEstado(id,json.getEstado()))); }

        catch(ResponseStatusException e) { return ResponseEntity.status(e.getStatusCode()).body(new APIResponse<>(false,e.getReason())); }
        catch(IllegalArgumentException e) { return ResponseEntity.badRequest().body(new APIResponse<>(false,e.getMessage())); }
        catch(DataIntegrityViolationException e) { return ResponseEntity.status(HttpStatus.CONFLICT).body(new APIResponse<>(false,"El registro tiene datos duplicados o relaciones con otros registros.")); }
        catch(Exception e) { log.error("Error en EventoController",e); return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new APIResponse<>(false,"No se pudo completar el proceso.")); }

    }


    @ExceptionHandler({org.springframework.http.converter.HttpMessageNotReadableException.class,org.springframework.web.method.annotation.MethodArgumentTypeMismatchException.class})
    public ResponseEntity<APIResponse<Void>> formatoInvalido(Exception e) {
        return ResponseEntity.badRequest().body(new APIResponse<>(false,"Formato invalido. Revisa el ID, los numeros y la fecha AAAA-MM-DD."));
    }

}
