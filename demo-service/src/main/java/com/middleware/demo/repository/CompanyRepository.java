package com.middleware.demo.repository;

import com.middleware.common.repository.CustomizedJpaRepository;
import com.middleware.demo.model.Company;
import org.springframework.stereotype.Repository;

@Repository
public interface CompanyRepository extends CustomizedJpaRepository<Company, Long> {
}
