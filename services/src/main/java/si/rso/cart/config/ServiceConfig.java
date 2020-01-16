package si.rso.cart.config;

import com.kumuluz.ee.configuration.cdi.ConfigBundle;
import com.kumuluz.ee.configuration.cdi.ConfigValue;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
@ConfigBundle(value = "service-config", watch = true)
public class ServiceConfig {

    @ConfigValue("maintenance")
    private boolean maintenance;
    
    @ConfigValue("stock-url")
    private String stockUrl;
    
    public String getStockUrl() {
        return stockUrl;
    }
    
    public void setStockUrl(String stockUrl) {
        this.stockUrl = stockUrl;
    }
    
    public boolean isMaintenance() {
        return maintenance;
    }

    public void setMaintenance(boolean maintenance) {
        this.maintenance = maintenance;
    }
}
