package si.rso.cart.services;

import java.util.List;

import com.kumuluz.ee.rest.beans.QueryParameters;
import si.rso.cart.lib.Sample;

public interface SampleService {
    
    List<Sample> getSamples(QueryParameters query);
    
    long getSamplesCount(QueryParameters query);
    
    List<Sample> findByAge(int age);
    
}