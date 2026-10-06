package JosueDiaz_OsvaldoQuintanar_1B2B_Backend__;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
public class ServletInitializer extends SpringBootServletInitializer {
    @Override protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(JosueDiazOsvaldoQuintanar1B2BBackendApplication.class);
    }
}
