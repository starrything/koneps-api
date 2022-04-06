package com.jonghyun.koneps.domain.g2b.bidNotice;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;

@Transactional
public interface BidNoticeService {
    List<Map<String, Object>> searchBidNoticeList(String keyword);
}
