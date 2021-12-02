package com.jonghyun.koneps.api.system.code;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class CodeServiceImpl implements CodeService {
    private final CodeRepository codeRepository;

    private List<Map<String, Object>> getKeyValuePair(String upperCode) {
        List<Map<String, Object>> result = new ArrayList<>();

        codeRepository.findByUpperCodeOrderBySortingNumberAsc(upperCode)
                .stream()
                .forEach(code -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("code", code.getCode());
                    map.put("value", code.getValue());

                    result.add(map);
                });
        return result;
    }

    @Override
    public List<Map<String, Object>> getTimeGrainCodeLists() {
        return this.getKeyValuePair("TIME_GRAIN");
    }

    @Override
    public List<Map<String, Object>> getLimitedRowCountList() {
        return this.getKeyValuePair("ROW_LIMIT");
    }

    @Override
    public List<Map<String, Object>> getQueryFilterList() {
        return this.getKeyValuePair("QUERY_FILTERS");
    }

    @Override
    public List<Map<String, Object>> getCodeList(String code) {
        return this.getKeyValuePair(code);
    }
}
