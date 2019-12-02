package si.rso.cart.services;

import si.rso.cart.lib.ShoppingCart;

import java.util.List;

public interface ShoppingCartService {

    List<ShoppingCart> getShoppingCartsForCustomer(String customerId);

    List<ShoppingCart> getShoppingCarts();
}
