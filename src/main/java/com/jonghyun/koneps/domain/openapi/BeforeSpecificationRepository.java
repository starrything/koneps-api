package com.jonghyun.koneps.domain.openapi;

import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BeforeSpecificationRepository extends JpaRepository<BeforeSpecification, BeforeSpecificationKey> {
    /*
    * Contains 사용시 CreateQuery 프로세스에서 Like 문에 escape[\] 오류 발생
    * -> @Query 처리
    * */
    @Query("SELECT bs" +
            " FROM BeforeSpecification bs" +
            " WHERE bs.bfSpecRgstNo LIKE '%' || :bfSpecRgstNo || '%'" +
            "    OR bs.prdctClsfcNoNm LIKE '%' || :prdctClsfcNoNm || '%'" +
            "    OR bs.rlDminsttNm LIKE '%' || :rlDminsttNm || '%'")
    List<BeforeSpecification> findByBfSpecRgstNoContainsOrPrdctClsfcNoNmContainsOrRlDminsttNmContainsOrderByRcptDtDesc(@Param(value = "bfSpecRgstNo") String bfSpecRgstNo,
                                                                                                                       @Param(value = "prdctClsfcNoNm") String prdctClsfcNoNm,
                                                                                                                       @Param(value = "rlDminsttNm") String rlDminsttNm);
}
