package si.rso.cart.services.impl;

import si.rso.cart.lib.ShoppingCart;
import si.rso.cart.mappers.ShoppingCartMapper;
import si.rso.cart.persistence.ShoppingCartEntity;
import si.rso.cart.services.ShoppingCartService;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@ApplicationScoped
public class ShoppingCartServiceImpl implements ShoppingCartService {

    @PersistenceContext(unitName = "main-jpa-unit")
    private EntityManager em;

    @Override
    public List<ShoppingCart> getShoppingCartsForCustomer(String customerId) {
        TypedQuery<ShoppingCartEntity> query = em.createNamedQuery(ShoppingCartEntity.FIND_BY_CUSTOMER,
                ShoppingCartEntity.class);
        query.setParameter("customerId", customerId);

        return query.getResultStream()
                .map(ShoppingCartMapper::fromEntity)
                .collect(Collectors.toList());
    }

    private Optional<ShoppingCartEntity> findByCustomerAndProduct(ShoppingCart shoppingCart) {
        TypedQuery<ShoppingCartEntity> query = em.createNamedQuery(ShoppingCartEntity.FIND_BY_CUSTOMER_AND_PRODUCT, ShoppingCartEntity.class);
        query.setParameter("customerId", shoppingCart.getCustomerId());
        query.setParameter("productId", shoppingCart.getProductId());

        return Optional.ofNullable(query.getSingleResult());
    }

    @Override
    @Transactional
    public ShoppingCart updateShoppingCartForCustomer(ShoppingCart shoppingCart) {
        // TODO check stock (sets the highest possible quantity if not enough stock)

        if (findByCustomerAndProduct(shoppingCart).isPresent()) {
            ShoppingCartEntity cartFromDB = findByCustomerAndProduct(shoppingCart).get();
            cartFromDB.setQuantity(shoppingCart.getQuantity());
            return ShoppingCartMapper.fromEntity(em.merge(cartFromDB));
        } else {
            em.persist(ShoppingCartMapper.toEntity(shoppingCart));
            return shoppingCart;
        }
    }

    @Override
    @Transactional
    public ShoppingCart deleteShoppingCartForCustomer(ShoppingCart shoppingCart) {
        ShoppingCartEntity cartFromDB = findByCustomerAndProduct(shoppingCart).orElseThrow();
        em.remove(cartFromDB);
        return ShoppingCartMapper.fromEntity(cartFromDB);
    }
}
