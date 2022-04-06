package com.jonghyun.koneps.domain.g2b.bidNotice;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/g2b/bidnotice")
public class BidNoticeController {
    private final BidNoticeService bidNoticeService;

    @GetMapping("/search")
    public List<Map<String, Object>> searchBidNoticeList(@RequestParam String keyword) {
        return bidNoticeService.searchBidNoticeList(keyword);
    }
}
