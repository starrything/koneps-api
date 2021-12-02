package com.jonghyun.koneps.api.data.openapi;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BeforeSpecificationRepository extends JpaRepository<BeforeSpecification, BeforeSpecificationKey> {
}
