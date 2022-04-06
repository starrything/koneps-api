package com.jonghyun.koneps.domain.beforeSpec;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;

@Transactional
public interface BeforeSpecService {
    List<Map<String, Object>> searchBeforeSpecList(String keyword);
}
