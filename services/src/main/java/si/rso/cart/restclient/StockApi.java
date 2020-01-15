package si.rso.cart.restclient;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import si.rso.cart.lib.ShoppingCart;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@RegisterRestClient
public interface StockApi {

    @GET
    @Path("/allstock/{productId}")
    ShoppingCart getNumberOfAllProducts(@PathParam("productId") String productId);
}
