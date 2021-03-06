package si.rso.cart.services.impl;

import com.kumuluz.ee.discovery.annotations.DiscoverService;
import com.kumuluz.ee.logs.LogManager;
import com.kumuluz.ee.logs.Logger;
import org.eclipse.microprofile.faulttolerance.CircuitBreaker;
import org.eclipse.microprofile.faulttolerance.Timeout;
import org.eclipse.microprofile.rest.client.RestClientBuilder;
import si.rso.cart.config.ServiceConfig;
import si.rso.cart.lib.ShoppingCart;
import si.rso.cart.mappers.ShoppingCartMapper;
import si.rso.cart.persistence.ShoppingCartEntity;
import si.rso.cart.restclient.StockApi;
import si.rso.cart.services.ShoppingCartService;
import si.rso.rest.exceptions.NotFoundException;
import si.rso.rest.exceptions.RestException;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.*;
import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@ApplicationScoped
public class ShoppingCartServiceImpl implements ShoppingCartService {

    private static final Logger LOG = LogManager.getLogger(ShoppingCartServiceImpl.class.getSimpleName());

    @PersistenceContext(unitName = "main-jpa-unit")
    private EntityManager em;

    @Inject
    @DiscoverService("stock-service")
    private Optional<String> stockBaseUrl;
    
    @Inject
    private ServiceConfig serviceConfig;

    @CircuitBreaker
    @Timeout
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

        try {
            return Optional.ofNullable(query.getSingleResult());
        } catch (NoResultException | NonUniqueResultException e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    @CircuitBreaker
    @Timeout(2000)
    @Override
    public ShoppingCart updateShoppingCartForCustomer(ShoppingCart shoppingCart) {
        if (stockBaseUrl.isEmpty()) {
            throw new RestException("Cannot find the url for the stock-service");
        }
        try {
            // preveri in popravi quantity glede na stock
            StockApi stockApi = RestClientBuilder.newBuilder()
                    .baseUri(URI.create(serviceConfig.getStockUrl()))
                    .build(StockApi.class);
            ShoppingCart temp = stockApi.getNumberOfAllProducts(shoppingCart.getProductId());
            int stockNumber = temp.getQuantity();
            if (shoppingCart.getQuantity() > stockNumber) {
                shoppingCart.setQuantity(stockNumber);
            }

            // ostalo
            em.getTransaction().begin();
            ShoppingCartEntity entity;
            Optional<ShoppingCartEntity> cart = findByCustomerAndProduct(shoppingCart);
            if (cart.isPresent()) {
                entity = cart.get();
                entity.setQuantity(shoppingCart.getQuantity());
                em.merge(entity);
            } else {
                entity = ShoppingCartMapper.toEntity(shoppingCart);
                em.persist(entity);
            }
            em.getTransaction().commit();
            return ShoppingCartMapper.fromEntity(entity);
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw new RestException(e.toString());
        }
    }

    @CircuitBreaker
    @Timeout
    @Override
    public ShoppingCart deleteShoppingCartForCustomer(ShoppingCart shoppingCart) {
        try {
            em.getTransaction().begin();
            ShoppingCartEntity cartFromDB = findByCustomerAndProduct(shoppingCart)
                    .orElseThrow(() -> new NotFoundException(ShoppingCartEntity.class, shoppingCart.getProductId()));
            em.remove(cartFromDB);
            em.getTransaction().commit();
            return ShoppingCartMapper.fromEntity(cartFromDB);
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw new RestException(e.toString());
        }
    }
}
