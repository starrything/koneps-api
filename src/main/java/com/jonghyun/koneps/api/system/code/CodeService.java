package com.jonghyun.koneps.api.system.code;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;

@Transactional
public interface CodeService {
    List<Map<String, Object>> getTimeGrainCodeLists();

    List<Map<String, Object>> getLimitedRowCountList();

    List<Map<String, Object>> getQueryFilterList();

    List<Map<String, Object>> getCodeList(String code);
}
