package com.jonghyun.koneps.api.platform;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/interface")
public class InterfaceController {
    private final PlatformService platformService;

    /**
     * subject : for Interface - send data
     * */
    @PostMapping("/dashboards")
    public List<Map<String, Object>> getDashboardList(@RequestBody InterfaceDto interfaceDto) {
        return platformService.getDashboardList(interfaceDto);
    }
}
