package si.rso.cart.mappers;

import si.rso.cart.lib.ShoppingCart;
import si.rso.cart.persistence.ShoppingCartEntity;

public class ShoppingCartMapper {

    public static ShoppingCart fromEntity(ShoppingCartEntity entity) {
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setId(entity.getId());
        shoppingCart.setTimestamp(entity.getTimestamp());
        shoppingCart.setCustomerId(entity.getCustomerId());
        shoppingCart.setProductId(entity.getProductId());
        shoppingCart.setQuantity(entity.getQuantity());

        return shoppingCart;
    }

    public static ShoppingCartEntity toEntity(ShoppingCart shoppingCart) {
        ShoppingCartEntity entity = new ShoppingCartEntity();
        entity.setCustomerId(shoppingCart.getCustomerId());
        entity.setProductId(shoppingCart.getProductId());
        entity.setQuantity(shoppingCart.getQuantity());
        return entity;
    }
}
