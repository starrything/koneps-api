package com.jonghyun.koneps.domain.system.code;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/code")
public class CodeController {
    private final CodeService codeService;

    @GetMapping("/time-grain")
    public List<Map<String, Object>> getTimeGrainCode() {
        return codeService.getTimeGrainCodeLists();
    }

    @GetMapping("/row-limit")
    public List<Map<String, Object>> getLimitedRowCountList() {
        return codeService.getLimitedRowCountList();
    }

    @GetMapping("/query-filter")
    public List<Map<String, Object>> getQueryFilterList() {
        return codeService.getQueryFilterList();
    }

    @GetMapping()
    public List<Map<String, Object>> getCodeList(@RequestParam String code) {
        return codeService.getCodeList(code);
    }
}
