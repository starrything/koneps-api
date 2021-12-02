package com.jonghyun.koneps.api.system.code;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CodeRepository extends JpaRepository<Code, CodeKey> {
    List<Code> findByUpperCodeOrderBySortingNumberAsc(String upperCode);
}
