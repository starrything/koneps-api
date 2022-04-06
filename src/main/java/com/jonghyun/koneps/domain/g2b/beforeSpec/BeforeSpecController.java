package com.jonghyun.koneps.domain.g2b.beforeSpec;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/g2b/beforespec")
public class BeforeSpecController {
    private final BeforeSpecService beforeSpecService;

    @GetMapping("/search")
    public List<Map<String, Object>> searchBeforeSpecList(@RequestParam String keyword) {
        return beforeSpecService.searchBeforeSpecList(keyword);
    }
}
