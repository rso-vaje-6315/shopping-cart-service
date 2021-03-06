package si.rso.cart.api.endpoints;

import com.kumuluz.ee.logs.cdi.Log;
import com.kumuluz.ee.security.annotations.Secure;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.eclipse.microprofile.metrics.annotation.Counted;
import org.keycloak.KeycloakPrincipal;
import org.keycloak.KeycloakSecurityContext;
import org.keycloak.representations.AccessToken;
import si.rso.cart.api.config.AuthRole;
import si.rso.cart.lib.ShoppingCart;
import si.rso.cart.services.ShoppingCartService;
import si.rso.rest.exceptions.UnauthorizedException;

import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Log
@Path("/shopping-cart")
@RequestScoped
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Secure
public class ShoppingCartEndpoint {
    
    @Inject
    private ShoppingCartService shoppingCartService;
    
    @Context
    SecurityContext securityContext;
    
    private Optional<KeycloakSecurityContext> getKeycloakSecurityContext() {
        if (securityContext != null && securityContext.getUserPrincipal() instanceof KeycloakPrincipal) {
            KeycloakPrincipal<?> principal = ((KeycloakPrincipal<?>) securityContext.getUserPrincipal());
            return Optional.of(principal.getKeycloakSecurityContext());
        } else {
            return Optional.empty();
        }
    }
    
    private Optional<String> getMyCustomerId() throws NoSuchElementException {
        KeycloakSecurityContext context = getKeycloakSecurityContext().orElseThrow();
        AccessToken token = context.getToken();
        return Optional.ofNullable(token.getSubject());
    }
    
    @GET
    @Path("/me")
    @RolesAllowed({AuthRole.CUSTOMER})
    @Operation(description = "Customer retrieves their shopping cart.",
        summary = "Returns users' shopping cart.", tags = "shopping-cart",
        responses = {
            @ApiResponse(responseCode = "200", description = "Returns products from the shopping cart.",
                content = @Content(array = @ArraySchema(schema = @Schema(implementation = ShoppingCart.class))))
        })
    public Response getShoppingCartsForCustomer() {
        String customerId = getMyCustomerId().orElseThrow(() -> new UnauthorizedException("Only for logged in users!"));
        
        List<ShoppingCart> shoppingCarts = shoppingCartService.getShoppingCartsForCustomer(customerId);
        return Response.ok(shoppingCarts).build();
    }
    
    @PUT
    @Path("/me")
    @RolesAllowed({AuthRole.CUSTOMER})
    @Operation(description = "Customer updates their shopping cart.",
        summary = "Update the shopping cart and returns users' updated shopping cart (all products).", tags = "shopping-cart",
        responses = {
            @ApiResponse(responseCode = "200", description = "Returns products from the shopping cart.",
                content = @Content(array = @ArraySchema(schema = @Schema(implementation = ShoppingCart.class))))
        })
    public Response updateShoppingCartForCustomer(ShoppingCart shoppingCart) {
        String customerId = getMyCustomerId().orElseThrow(() -> new UnauthorizedException("Only for logged in users!"));
        shoppingCart.setCustomerId(customerId);
        
        shoppingCartService.updateShoppingCartForCustomer(shoppingCart);
        
        List<ShoppingCart> shoppingCarts = shoppingCartService.getShoppingCartsForCustomer(customerId);
        
        return Response.ok(shoppingCarts).build();
    }
    
    @DELETE
    @Counted(name = "remove-from-shopping-cart-count")
    @Path("/me")
    @RolesAllowed({AuthRole.CUSTOMER})
    @Operation(description = "Customer removes a product from the shopping cart.",
        summary = "Removes a product from the shopping cart.", tags = "shopping-cart",
        responses = {
            @ApiResponse(responseCode = "200", description = "Removes a product from the shopping cart.",
                content = @Content(schema = @Schema(implementation = ShoppingCart.class)))
        })
    public Response removeProductFromShoppingCartForCustomer(ShoppingCart shoppingCart) {
        String customerId = getMyCustomerId().orElseThrow(() -> new UnauthorizedException("Only for logged in users!"));
        shoppingCart.setCustomerId(customerId);
        
        shoppingCartService.deleteShoppingCartForCustomer(shoppingCart);
        return Response.noContent().build();
    }
}
