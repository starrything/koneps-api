package com.jonghyun.koneps.domain.data.openapi;

import javax.transaction.Transactional;
import java.io.IOException;

@Transactional
public interface OpenapiService {
    void callApiForBeforeSpecification(String paramDateFrom, String paramDateTo) throws IOException;
    void callApiForBidNotice(String paramDateFrom, String paramDateTo) throws IOException;
}
