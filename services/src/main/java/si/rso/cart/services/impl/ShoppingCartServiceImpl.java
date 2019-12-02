package si.rso.cart.services.impl;

import si.rso.cart.lib.ShoppingCart;
import si.rso.cart.mappers.ShoppingCartMapper;
import si.rso.cart.persistence.ShoppingCartEntity;
import si.rso.cart.services.ShoppingCartService;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class ShoppingCartServiceImpl implements ShoppingCartService {

    @PersistenceContext(unitName = "main-jpa-unit")
    private EntityManager em;

    @Override
    public List<ShoppingCart> getShoppingCartsForCustomer(String customerId) {
        // TODO ne dela, neka napaka je v query-ju
        TypedQuery<ShoppingCartEntity> query = em.createNamedQuery(ShoppingCartEntity.FIND_BY_CUSTOMER,
                ShoppingCartEntity.class);
        query.setParameter("customerId", customerId);

        return query.getResultStream()
                .map(ShoppingCartMapper::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<ShoppingCart> getShoppingCarts() {
        TypedQuery<ShoppingCartEntity> query = em.createNamedQuery(ShoppingCartEntity.FIND_ALL,
                ShoppingCartEntity.class);

        return query.getResultStream()
                .map(ShoppingCartMapper::fromEntity)
                .collect(Collectors.toList());
    }
}
