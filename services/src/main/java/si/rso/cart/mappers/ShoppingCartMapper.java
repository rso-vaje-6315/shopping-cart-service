package si.rso.cart.mappers;

import si.rso.cart.lib.ShoppingCart;
import si.rso.cart.persistence.ShoppingCartEntity;

public class ShoppingCartMapper {

    public static ShoppingCart fromEntity(ShoppingCartEntity entity) {
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setCustomerId(entity.getCustomerId());
        shoppingCart.setProductId(entity.getProductId());
        shoppingCart.setQuantity(entity.getQuantity());

        return shoppingCart;
    }
}
