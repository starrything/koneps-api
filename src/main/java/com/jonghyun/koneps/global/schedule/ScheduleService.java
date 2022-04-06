package com.jonghyun.koneps.global.schedule;

import java.io.IOException;

public interface ScheduleService {

    void runNoticeMailing();

    void getBeforeSpecification() throws IOException;
    void getBidNotice() throws IOException;
}
