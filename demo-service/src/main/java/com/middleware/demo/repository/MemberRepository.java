package com.middleware.demo.repository;

import com.middleware.common.repository.CustomizedJpaRepository;
import com.middleware.demo.model.Member;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface MemberRepository extends CustomizedJpaRepository<Member, Long>, JpaSpecificationExecutor {
    Member findByPhoneAndRegionCode(String phone, Integer regionCode);
}
