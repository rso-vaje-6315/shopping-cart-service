package si.rso.cart.restclient;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public interface StockApi {

    @GET
    @Path("/allstock/{productId}")
    Long getNumberOfAllProducts(@PathParam("productId") String productId);
}
