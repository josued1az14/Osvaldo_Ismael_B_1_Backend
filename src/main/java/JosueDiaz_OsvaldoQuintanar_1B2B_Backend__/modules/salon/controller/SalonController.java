package JosueDiaz_OsvaldoQuintanar_1B2B_Backend__.modules.salon.controller;
import JosueDiaz_OsvaldoQuintanar_1B2B_Backend__.modules.salon.model.dto.*;
import JosueDiaz_OsvaldoQuintanar_1B2B_Backend__.modules.salon.service.SalonService;
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
@RequestMapping("/api/salon")
@CrossOrigin(origins="*")
public class SalonController {
    private final SalonService service;
    public SalonController(SalonService service) { this.service=service; }

    @GetMapping
    public ResponseEntity<APIResponse<List<SalonDTO>>> obtenerDatos() {
        try { return ResponseEntity.ok(new APIResponse<>(true,"Proceso completado",service.obtenerTodos())); }

        catch(ResponseStatusException e) { return ResponseEntity.status(e.getStatusCode()).body(new APIResponse<>(false,e.getReason())); }
        catch(IllegalArgumentException e) { return ResponseEntity.badRequest().body(new APIResponse<>(false,e.getMessage())); }
        catch(DataIntegrityViolationException e) { return ResponseEntity.status(HttpStatus.CONFLICT).body(new APIResponse<>(false,"El registro tiene datos duplicados o relaciones con otros registros.")); }
        catch(Exception e) { log.error("Error en SalonController",e); return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new APIResponse<>(false,"No se pudo completar el proceso.")); }

    }
    @GetMapping("/{id}")
    public ResponseEntity<APIResponse<SalonDTO>> obtenerDatosId(@PathVariable Long id) {
        try {
            SalonDTO dto=service.obtenerPorId(id);
            if(dto!=null) return ResponseEntity.ok(new APIResponse<>(true,"Registro encontrado",dto));
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new APIResponse<>(false,"Registro no encontrado"));
        }

        catch(ResponseStatusException e) { return ResponseEntity.status(e.getStatusCode()).body(new APIResponse<>(false,e.getReason())); }
        catch(IllegalArgumentException e) { return ResponseEntity.badRequest().body(new APIResponse<>(false,e.getMessage())); }
        catch(DataIntegrityViolationException e) { return ResponseEntity.status(HttpStatus.CONFLICT).body(new APIResponse<>(false,"El registro tiene datos duplicados o relaciones con otros registros.")); }
        catch(Exception e) { log.error("Error en SalonController",e); return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new APIResponse<>(false,"No se pudo completar el proceso.")); }

    }


    @ExceptionHandler({org.springframework.http.converter.HttpMessageNotReadableException.class,org.springframework.web.method.annotation.MethodArgumentTypeMismatchException.class})
    public ResponseEntity<APIResponse<Void>> formatoInvalido(Exception e) {
        return ResponseEntity.badRequest().body(new APIResponse<>(false,"Formato invalido. Revisa el ID, los numeros y la fecha AAAA-MM-DD."));
    }

}
