package com.jonghyun.koneps.global.schedule;

import java.io.IOException;

public interface ScheduleService {

    void runNoticeMailing();

    void getBeforeSpecification(String dateFrom, String dateTo) throws IOException;
    void getBidNotice(String dateFrom, String dateTo) throws IOException;
}
