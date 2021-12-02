package com.jonghyun.koneps.core.schedule;

import java.io.IOException;

public interface ScheduleService {

    void runNoticeMailing();

    void getBeforeSpecification() throws IOException;
    void getBidNotice() throws IOException;
}
