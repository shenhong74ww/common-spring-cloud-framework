package com.middleware.common.repository;

import com.middleware.common.model.BaseModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;


@NoRepositoryBean
public interface CustomizedJpaRepository<T extends BaseModel, ID extends Serializable> extends JpaRepository<T, ID> {

    void restore(ID id);
}
