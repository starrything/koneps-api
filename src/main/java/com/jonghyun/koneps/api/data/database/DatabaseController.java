package com.jonghyun.koneps.api.data.database;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/data/database")
public class DatabaseController {
    private final DatabaseService databaseService;

    /**
     * Subject : 커넥션 테스트
     * */
    @GetMapping("/test-connection")
    public String testConnection(@RequestParam String type,
                                 @RequestParam String username,
                                 @RequestParam String password,
                                 @RequestParam String database) {
        log.info("type: {}, username: {}, password: {}, database: {}", type, username, password, database);

        if(databaseService.testConnection(type, username, password, database)) {
            return "connection success";
        } else {
            return "connection failed";
        }
    }

    /**
     * Subject : 커넥션 찾기
     * */
    @GetMapping("/search")
    public List<Map<String, Object>> searchRegisteredDatabase(@RequestParam String keyword) {
        return databaseService.searchDatabaseList(keyword);
    }

    /**
     * Subject : 커넥션 추가
     * */
    @PostMapping("/add-connection")
    public boolean addConnection(@RequestBody DatabaseDto databaseDto) {

        if(databaseService.addConnection(databaseDto)) {
            return true;
        } else {
            return false;
        }

    }

    /**
     * Subject : 커넥션 갱신
     * */
    @PutMapping("/update-connection")
    public boolean updateConnection(@RequestBody DatabaseDto databaseDto) {
        if(databaseService.updateConnection(databaseDto)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Subject : 커넥션 삭제
     * */
    @DeleteMapping("/delete-connection")
    public boolean deleteConnection(@RequestParam String databaseId) {
        if(databaseService.deleteConnection(databaseId)) {
            return true;
        } else {
            return false;
        }
    }
}
