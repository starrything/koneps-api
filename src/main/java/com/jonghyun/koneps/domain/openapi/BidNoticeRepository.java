package com.jonghyun.koneps.domain.openapi;

import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BidNoticeRepository extends JpaRepository<BidNotice, BidNoticeKey> {
    /*
     * Contains 사용시 CreateQuery 프로세스에서 Like 문에 escape[\] 오류 발생
     * -> @Query 처리
     * */
    @Query("SELECT bn" +
            " FROM BidNotice bn" +
            " WHERE bn.bidNtceNo LIKE '%' || :bidNtceNo || '%'" +
            "    OR bn.bidNtceNm LIKE '%' || :bidNtceNm || '%'" +
            "    OR bn.ntceInsttNm LIKE '%' || :ntceInsttNm || '%'" +
            "    OR bn.dminsttNm LIKE '%' || :dminsttNm || '%'")
    List<BidNotice> findByBidNtceNoContainsOrBidNtceNmContainsOrNtceInsttNmContainsOrDminsttNmContainsOrderByBidNtceDtDesc(@Param(value = "bidNtceNo") String bidNtceNo,
                                                                                                                           @Param(value = "bidNtceNm") String bidNtceNm,
                                                                                                                           @Param(value = "ntceInsttNm") String ntceInsttNm,
                                                                                                                           @Param(value = "dminsttNm") String dminsttNm);
}
