package com.jonghyun.koneps.core.schedule;

import com.jonghyun.koneps.api.data.openapi.BeforeSpecificationRepository;
import com.jonghyun.koneps.api.data.openapi.OpenapiService;
import com.jonghyun.koneps.core.mail.MailService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;

import static java.time.temporal.TemporalAdjusters.lastDayOfMonth;

@Service
@AllArgsConstructor
public class ScheduleServiceImpl implements ScheduleService {
    private static final Logger logger = LoggerFactory.getLogger(ScheduleServiceImpl.class);

    private final MailService mailService;
    private final Environment environment;

    private final OpenapiService openapiService;

    @Override
    public void runNoticeMailing() {
        /* 서버 현재 일자 */
        LocalDate today = LocalDate.now();
        /* 현재월의 마지막 일자 */
        LocalDate lastDay = today.with(today.getMonth()).with(lastDayOfMonth());
        /* 마지막 일자와 현재 일자 차이 - 일수 */
        int cmp = lastDay.compareTo(today);

        /* D-2(2), D-1(1), D-day(0)
         * -> 3days
         *  */
        /*if(cmp < 3) {
         *//*
         * 1. 전표 상태가 작성중인 건이 존재하는 직원
         * *//*
            logger.info("[CronJob] Notice Mailing - 전표 작성중");

            List<Map<String, String>> empNoBySlip = slipGroupRepository.getEmpByWorkingSlips();
            for (Map<String, String> dto:
                    empNoBySlip) {
                String empNo = dto.get("empNo");
                Optional<ScheduleDto> cntDto = scheduleCustomRepository.getCntByWorkingSlips(empNo);

                *//* Send Mail *//*
                String mailTo = "";
                mailTo = empNo + "@iljin.co.kr";
                String mailSubjet = "전표 미상신 [" + cntDto.get().getCol1() + "] 건이 존재합니다.";
                String mailText = "전표 저장 이후 미상신 [" + cntDto.get().getCol1() + "] 건이 존재합니다. 미상신 건에 대해 결재상신 하시기 바랍니다. <br><br><a href='http://"
                        + environment.getProperty("server.domain-name")
                        + "/slipLst' target='_blank'>전표내역 조회 Open</a>";
                MailDto mailDto = new MailDto(
                        "No_reply@iljin.co.kr",
                        mailTo,
                        mailSubjet,
                        mailText
                );
                mailService.sendSimpleMessage(mailDto);

                logger.info(">> EMP_NO : " + empNo + " / WORKING SLIPS -> " + cntDto.get().getCol1());
            }

            *//*
         * 2. 법인카드 미처리 건이 존재하는 직원
         * *//*
            logger.info("[CronJob] Notice Mailing - 법인카드 미처리건");

            List<ScheduleDto> empNoByCard = scheduleCustomRepository.getEmpByUndlCard();
            for (ScheduleDto dto:
                    empNoByCard) {
                String empNo = dto.getCol1();
                Optional<ScheduleDto> cntDto = scheduleCustomRepository.getCntByUndlCard(empNo);

                *//* Send Mail *//*
                String mailTo = "";
                mailTo = empNo + "@iljin.co.kr";
                String mailSubjet = "법인카드 미처리 [" + cntDto.get().getCol1() + "] 건이 존재합니다.";
                String mailText = "법인카드 미처리 [" + cntDto.get().getCol1() + "] 건이 존재합니다. 개인비용 전표 작성 및 상신하시기 바랍니다. <br><br><a href='http://"
                        + environment.getProperty("server.domain-name")
                        + "/pExpense' target='_blank'>개인비용(경비)신청서 Open</a>";
                MailDto mailDto = new MailDto(
                        "No_reply@iljin.co.kr",
                        mailTo,
                        mailSubjet,
                        mailText
                );
                mailService.sendSimpleMessage(mailDto);

                logger.info(">> EMP_NO : " + empNo + " / UN HANDLED CARD usage -> " + cntDto.get().getCol1());
            }
        }*/
    }

    @Override
    public void getBeforeSpecification() throws IOException {
        openapiService.callApiForBeforeSpecification("", "");
    }

    @Override
    public void getBidNotice() throws IOException {
        openapiService.callApiForBidNotice("", "");
    }

}
