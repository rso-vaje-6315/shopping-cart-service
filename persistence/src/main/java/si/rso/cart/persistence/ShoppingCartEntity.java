package si.rso.cart.persistence;

import javax.persistence.*;

@Entity
@Table(name = "shopping_cart")
@NamedQueries(value = {
        @NamedQuery(name = ShoppingCartEntity.FIND_BY_CUSTOMER, query = "SELECT s FROM ShoppingCartEntity s WHERE s.customerId = :customerId")
        , @NamedQuery(name = ShoppingCartEntity.FIND_ALL, query = "SELECT s FROM ShoppingCartEntity s")
        , @NamedQuery(name = ShoppingCartEntity.FIND_BY_CUSTOMER_AND_PRODUCT, query = "SELECT s FROM ShoppingCartEntity s WHERE s.customerId = :customerId AND s.productId = :productId")
})
public class ShoppingCartEntity extends BaseEntity {

    public static final String FIND_BY_CUSTOMER = "ShoppingCartEntity.findByCustomer";
    public static final String FIND_ALL = "ShoppingCartEntity.findAll";
    public static final String FIND_BY_CUSTOMER_AND_PRODUCT = "ShoppingCartEntity.findByCustomerAndProduct";

    @Column(name = "customer_id")
    private String customerId;

    @Column(name = "product_id")
    private String productId;

    private int quantity;

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
