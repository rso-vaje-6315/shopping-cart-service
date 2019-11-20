package si.rso.cart.mappers;

import si.rso.cart.lib.Sample;
import si.rso.cart.persistence.SampleEntity;

public class SampleMapper {
    
    public static Sample fromEntity(SampleEntity entity) {
        return new Sample();
    }
    
}