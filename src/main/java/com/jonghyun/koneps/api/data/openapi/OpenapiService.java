package com.jonghyun.koneps.api.data.openapi;

import javax.transaction.Transactional;
import java.io.IOException;

@Transactional
public interface OpenapiService {
    void callApiForBeforeSpecification() throws IOException;
    void callApiForBidNotice() throws IOException;
}
