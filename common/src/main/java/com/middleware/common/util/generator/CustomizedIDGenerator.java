package com.middleware.common.util.generator;

import com.middleware.common.model.BaseModel;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentityGenerator;

import java.io.Serializable;

public class CustomizedIDGenerator extends IdentityGenerator {

    @Override
    public Serializable generate(SharedSessionContractImplementor s, Object entity) {
        if (((BaseModel) entity).getSid() == null) {
            return super.generate(s, entity);
        } else {
            return ((BaseModel) entity).getSid();
        }
    }
}
