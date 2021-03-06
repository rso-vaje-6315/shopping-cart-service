package si.rso.cart.api;

import com.kumuluz.ee.discovery.annotations.RegisterService;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import si.rso.cart.api.config.AuthRole;
import si.rso.cart.api.endpoints.ShoppingCartEndpoint;

import javax.annotation.security.DeclareRoles;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import java.util.HashSet;
import java.util.Set;

@ApplicationPath("/v1")
@DeclareRoles({AuthRole.SERVICE, AuthRole.ADMIN, AuthRole.SELLER, AuthRole.CUSTOMER})
@OpenAPIDefinition(
        info = @Info(title = "Shopping cart service", version = "1.0.0", contact = @Contact(name = "Matej Bizjak"),
                description = "Service for managing shopping carts.")
)
@RegisterService
public class RestService extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> classes = new HashSet<>();

        classes.add(ShoppingCartEndpoint.class);

        return classes;
    }
}
