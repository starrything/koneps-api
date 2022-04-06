package com.jonghyun.koneps.domain.g2b.bidNotice;

import com.jonghyun.koneps.domain.data.openapi.BidNoticeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class BidNoticeServiceImpl implements BidNoticeService {
    private final BidNoticeRepository bidNoticeRepository;

    @Override
    public List<Map<String, Object>> searchBidNoticeList(String keyword) {
        List<Map<String, Object>> result = new ArrayList<>();
        bidNoticeRepository.findByBidNtceNoContainsOrBidNtceNmContainsOrNtceInsttNmContainsOrDminsttNmContainsOrderByBidNtceDtDesc(keyword, keyword, keyword, keyword)
                .stream()
                .forEach(bidNotice -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("bidNtceDt", bidNotice.getBidNtceDt());
                    map.put("bidNtceNo", bidNotice.getBidNtceNo());
                    map.put("bidNtceNm", bidNotice.getBidNtceNm());
                    map.put("ntceInsttCd", bidNotice.getNtceInsttCd());
                    map.put("ntceInsttNM", bidNotice.getNtceInsttNm());
                    map.put("dminsttCd", bidNotice.getDminsttCd());
                    map.put("dminsttNm", bidNotice.getDminsttNm());
                    map.put("asignBdgtAmt", bidNotice.getAsignBdgtAmt());
                    map.put("presmptPrce", bidNotice.getPresmptPrce());
                    map.put("bidMethdNm", bidNotice.getBidMethdNm());
                    map.put("cntrctCnclsMthdNm", bidNotice.getCntrctCnclsMthdNm());
                    map.put("bidClseDt", bidNotice.getBidClseDt());
                    map.put("opengDt", bidNotice.getOpengDt());
                    map.put("sucsfbidMthdNm", bidNotice.getSucsfbidMthdNm());
                    map.put("ntceSpecDocUrl1", bidNotice.getNtceSpecDocUrl1());
                    map.put("ntceSpecDocUrl2", bidNotice.getNtceSpecDocUrl2());
                    map.put("ntceSpecDocUrl3", bidNotice.getNtceSpecDocUrl3());
                    map.put("ntceSpecFileNm1", bidNotice.getNtceSpecFileNm1());
                    map.put("ntceSpecFileNm2", bidNotice.getNtceSpecFileNm2());
                    map.put("ntceSpecFileNm3", bidNotice.getNtceSpecFileNm3());
                    map.put("bidNtceDtlUrl", bidNotice.getBidNtceDtlUrl());
                    map.put("bidNtceUrl", bidNotice.getBidNtceUrl());
                    map.put("rgstDt", bidNotice.getRgstDt());

                    result.add(map);
                });

        return result;
    }
}
