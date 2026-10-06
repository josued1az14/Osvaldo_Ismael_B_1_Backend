package JosueDiaz_OsvaldoQuintanar_1B2B_Backend__.apiResponse;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class APIResponse <T>{

    private boolean success;
    private String message;
    private T data;

    public APIResponse(boolean success, String message, T data) {
        this.success = success;
        this.message = message;
        this.data = data;
    }

    public APIResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
    }
}
