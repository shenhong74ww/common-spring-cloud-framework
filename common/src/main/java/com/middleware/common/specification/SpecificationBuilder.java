/*
 * Copyright 2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.middleware.common.specification;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public class SpecificationBuilder<T> {
    private JpaSpecificationExecutor repository;
    private SpecificationImpl specification;

    public static <T, R extends JpaRepository<T, ?> & JpaSpecificationExecutor>
    SpecificationBuilder<T> selectFrom(R repository) {
        SpecificationBuilder<T> builder = new SpecificationBuilder<>();
        builder.repository = repository;
        builder.specification = new SpecificationImpl();
        return builder;
    }

    public SpecificationBuilder<T> where(Filter filter) {
        specification.add(new WhereSpecification(filter));
        return this;
    }

    public List<T> findAll() {
        return repository.findAll(specification);
    }

    public Page<T> findPage(Pageable page) {
        return repository.findAll(specification, page);
    }

    public Long count() {
        return repository.count(specification);
    }
}
