package si.rso.cart.api.endpoints;

import si.rso.cart.lib.ShoppingCart;
import si.rso.cart.services.ShoppingCartService;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/")
@RequestScoped
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ShoppingCartEndpoint {

    @Inject
    private ShoppingCartService shoppingCartService;

    @GET
    public Response getShoppingCartsForCustomer() {
        String customerId = "TODO"; // TODO get current logged user's id
        List<ShoppingCart> shoppingCarts = shoppingCartService.getShoppingCartsForCustomer(customerId);

        return Response.ok(shoppingCarts).build();
    }

}
