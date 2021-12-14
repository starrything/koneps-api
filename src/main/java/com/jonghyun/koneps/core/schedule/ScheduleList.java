package com.jonghyun.koneps.core.schedule;

import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;


@Component
@AllArgsConstructor
@EnableScheduling
public class ScheduleList {
    private final ScheduleService scheduleService;

    /**
     * scheduled Mailing
     * 알림 메일 발송 (D-2 ~ D-day: 3days)
     * 1) 작성중 전표
     * 2) 법인카드 미처리건
     * Desc. 매일 새벽 1시 15분에 실행
     * */
    //@Scheduled(cron = "0 15 1 * * ?")
    //public void cronJobNoticeMailing() { scheduleService.runNoticeMailing(); }

    /**
     * 사전규격공개
     * 오픈API 인터페이스
     * Desc. 2시간 간격으로 데이터 수집
     * */
    @Scheduled(fixedRate = 7200000)
    public void getBeforeSpecification() throws IOException {
        //TODO: build 시 아래 주석 해제
        scheduleService.getBeforeSpecification();
    }

    /**
     * 입찰공고
     * 오픈API 인터페이스
     * Desc. 2시간 간격으로 데이터 수집
     * */
    @Scheduled(fixedRate = 7200000)
    public void getBidNotice() throws IOException {
        //TODO: build 시 아래 주석 해제
        scheduleService.getBidNotice();
    }
}
