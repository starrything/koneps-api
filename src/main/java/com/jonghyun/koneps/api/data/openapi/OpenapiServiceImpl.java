package com.jonghyun.koneps.api.data.openapi;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.util.internal.StringUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@RequiredArgsConstructor
public class OpenapiServiceImpl implements OpenapiService {
    private final BeforeSpecificationRepository beforeSpecificationRepository;
    private final BidNoticeRepository bidNoticeRepository;

    @Override
    @Modifying
    public void callApiForBeforeSpecification(String paramDateFrom, String paramDateTo) throws IOException {
        LocalDateTime dateFrom = LocalDateTime.now().minusHours(2);
        LocalDateTime dateTo = LocalDateTime.now();
        String searchDateFr = paramDateFrom;
        if(StringUtil.isNullOrEmpty(searchDateFr)) {
            searchDateFr = dateFrom.format(DateTimeFormatter.ofPattern("YYYYMMddHHmm"));
        }
        String searchDateTo = paramDateTo;
        if(StringUtil.isNullOrEmpty(searchDateTo)) {
            dateTo.format(DateTimeFormatter.ofPattern("YYYYMMddHHmm"));
        }

        /*URL*/
        StringBuilder urlBuilder = new StringBuilder("http://apis.data.go.kr/1230000/HrcspSsstndrdInfoService/getPublicPrcureThngInfoServcPPSSrch");
        /*Service Key*/
        urlBuilder.append("?" + URLEncoder.encode("serviceKey", "UTF-8") + "=" + URLEncoder.encode("B1CsUiO26Y56VDOKIParM6z394FXvTQC0rafsREBzSnOl8Cc1PUFY98LOcqKq5OahD5s2AhvszA2AIIYj0KXvg==", "UTF-8"));
        /*한 페이지 결과 수*/
        urlBuilder.append("&" + URLEncoder.encode("numOfRows", "UTF-8") + "=" + URLEncoder.encode("900", "UTF-8"));
        /*페이지 번호*/
        urlBuilder.append("&" + URLEncoder.encode("pageNo", "UTF-8") + "=" + URLEncoder.encode("1", "UTF-8"));
        /*검색하고자하는 조회구분 1. 등록일시 2. 사전규격등록번호 3. 변경일시*/
        urlBuilder.append("&" + URLEncoder.encode("inqryDiv", "UTF-8") + "=" + URLEncoder.encode("1", "UTF-8"));
        /*검색하고자하는 조회시작일시 'YYYYMMDDHHMM' 조회구분이 1인 경우 필수*/
        urlBuilder.append("&" + URLEncoder.encode("inqryBgnDt", "UTF-8") + "=" + URLEncoder.encode(searchDateFr, "UTF-8"));
        /*검색하고자하는 조회종료일시 'YYYYMMDDHHMM' 조회구분이 1인 경우 필수*/
        urlBuilder.append("&" + URLEncoder.encode("inqryEndDt", "UTF-8") + "=" + URLEncoder.encode(searchDateTo, "UTF-8"));
        /*오픈API 리턴 타입을 JSON으로 받고 싶을 경우 'json' 으로 지정*/
        urlBuilder.append("&" + URLEncoder.encode("type", "UTF-8") + "=" + URLEncoder.encode("json", "UTF-8"));

        /*검색하고자하는 SW사업대상여부 Y/N*/
        urlBuilder.append("&" + URLEncoder.encode("swBizObjYn", "UTF-8") + "=" + URLEncoder.encode("Y", "UTF-8"));

        URL url = new URL(urlBuilder.toString());
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-type", "application/json");
        System.out.println("Response code: " + conn.getResponseCode());

        BufferedReader rd;
        if (conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
            rd = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
        } else {
            rd = new BufferedReader(new InputStreamReader(conn.getErrorStream(), "UTF-8"));
        }

        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = rd.readLine()) != null) {
            sb.append(line);
        }

        /*TODO: get words*/
        String[] words = {"음식", "생물", "녹색", "청소", "하수처리", "폐수", "폐기물", "대형폐기물", "폐목재", "폐매트리스", "폐터널"
                , "환경미화", "전용차량", "운행", "인터넷회선", "자동차종합보험", "상시보수공사", "전기안전관리", "콘텐츠 제작", "방과후학교"
                , "사진 공모전", "홍보사업", "성과평가", "사용성평가", "시설개선사업", "학교급식", "웹진 제작", "도로유지보수사업"
        };
        if (conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
            ObjectMapper objectMapper = new ObjectMapper();
            HashMap map = objectMapper.readValue(sb.toString(), HashMap.class);
            Map<String, Object> response = (Map<String, Object>) map.get("response");
            Map<String, Object> body = (Map<String, Object>) response.get("body");

            List<Map<String, String>> mapList = new ArrayList<>();
            mapList = (List<Map<String, String>>) body.get("items");

            mapList.forEach(c -> {
                boolean found = false;
                for (String word : words) {
                    if (c.get("prdctClsfcNoNm").contains(word)) {
                        found = true;
                        break;
                    }
                }

                if (!found) {
                    BeforeSpecificationKey beforeSpecificationKey = new BeforeSpecificationKey(c.get("bfSpecRgstNo"), c.get("rgstDt"));
                    Optional<BeforeSpecification> beforeSpecification = beforeSpecificationRepository.findById(beforeSpecificationKey);
                    if (beforeSpecification.isPresent()) {
                    } else {
                        BeforeSpecification bs = new BeforeSpecification(
                                c.get("bfSpecRgstNo"),
                                c.get("rgstDt"),
                                c.get("bsnsDivNm"),
                                c.get("refNo"),
                                c.get("prdctClsfcNoNm"),
                                c.get("orderInsttNm"),
                                c.get("rlDminsttNm"),
                                c.get("asignBdgtAmt"),
                                c.get("rcptDt"),
                                c.get("opninRgstClseDt"),
                                c.get("ofclTelNo"),
                                c.get("ofclNm"),
                                c.get("swBizObjYn"),
                                c.get("dlvrTmlmtDt"),
                                c.get("dlvrDaynum"),
                                c.get("specDocFileUrl1"),
                                c.get("specDocFileUrl2"),
                                c.get("specDocFileUrl3"),
                                c.get("specDocFileUrl4"),
                                c.get("specDocFileUrl5"),
                                c.get("prdctDtlList"),
                                c.get("chgDt"),
                                c.get("bidNtceNoList"),
                                LocalDateTime.now()
                        );
                        beforeSpecificationRepository.save(bs);
                    }
                }
            });
        } else {
            //
        }

        rd.close();
        conn.disconnect();
        System.out.println(sb.toString());
    }

    @Override
    @Modifying
    public void callApiForBidNotice(String paramDateFrom, String paramDateTo) throws IOException {
        LocalDateTime dateFrom = LocalDateTime.now().minusHours(2);
        LocalDateTime dateTo = LocalDateTime.now();
        String searchDateFr = paramDateFrom;
        if(StringUtil.isNullOrEmpty(searchDateFr)) {
            searchDateFr = dateFrom.format(DateTimeFormatter.ofPattern("YYYYMMddHHmm"));
        }
        String searchDateTo = paramDateTo;
        if(StringUtil.isNullOrEmpty(searchDateTo)) {
            dateTo.format(DateTimeFormatter.ofPattern("YYYYMMddHHmm"));
        }

        /*URL*/
        StringBuilder urlBuilder = new StringBuilder("http://apis.data.go.kr/1230000/BidPublicInfoService02/getBidPblancListInfoServcPPSSrch");
        /*Service Key*/
        urlBuilder.append("?" + URLEncoder.encode("serviceKey", "UTF-8") + "=" + URLEncoder.encode("B1CsUiO26Y56VDOKIParM6z394FXvTQC0rafsREBzSnOl8Cc1PUFY98LOcqKq5OahD5s2AhvszA2AIIYj0KXvg==", "UTF-8"));
        /*한 페이지 결과 수*/
        urlBuilder.append("&" + URLEncoder.encode("numOfRows", "UTF-8") + "=" + URLEncoder.encode("900", "UTF-8"));
        /*페이지 번호*/
        urlBuilder.append("&" + URLEncoder.encode("pageNo", "UTF-8") + "=" + URLEncoder.encode("1", "UTF-8"));
        /*검색하고자하는 조회구분 1. 등록일시 2. 사전규격등록번호 3. 변경일시*/
        urlBuilder.append("&" + URLEncoder.encode("inqryDiv", "UTF-8") + "=" + URLEncoder.encode("1", "UTF-8"));
        /*검색하고자하는 조회시작일시 'YYYYMMDDHHMM' 조회구분이 1인 경우 필수*/
        urlBuilder.append("&" + URLEncoder.encode("inqryBgnDt", "UTF-8") + "=" + URLEncoder.encode(searchDateFr, "UTF-8"));
        /*검색하고자하는 조회종료일시 'YYYYMMDDHHMM' 조회구분이 1인 경우 필수*/
        urlBuilder.append("&" + URLEncoder.encode("inqryEndDt", "UTF-8") + "=" + URLEncoder.encode(searchDateTo, "UTF-8"));
        /*오픈API 리턴 타입을 JSON으로 받고 싶을 경우 'json' 으로 지정*/
        urlBuilder.append("&" + URLEncoder.encode("type", "UTF-8") + "=" + URLEncoder.encode("json", "UTF-8"));

        /*검색하고자하는 국제구분코드 국내:1, 국제:2(방위사업청 연계건의 경우 아래 내용 참고하여 검색) 국내/시설 입찰 공고일 경우 : 1, 국외 입찰 공고일 경우 : 2*/
        urlBuilder.append("&" + URLEncoder.encode("intrntnlDivCd", "UTF-8") + "=" + URLEncoder.encode("1", "UTF-8"));
        /*입찰마감건을 제외하고 검색하고자 하는 경우 Y*/
        urlBuilder.append("&" + URLEncoder.encode("bidClseExcpYn", "UTF-8") + "=" + URLEncoder.encode("N", "UTF-8"));
        /*검색하고자하는 업종명, ※ 업종명 일부 입력시에도 조회 가능(방위사업청 연계건의 경우 : 면허제한목록(lcnsLmttList) 내 업종명으로 검색)*/
        urlBuilder.append("&" + URLEncoder.encode("indstrytyNm", "UTF-8") + "=" + URLEncoder.encode("소프트웨어", "UTF-8"));

        URL url = new URL(urlBuilder.toString());
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-type", "application/json");
        System.out.println("Response code: " + conn.getResponseCode());

        BufferedReader rd;
        if (conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
            rd = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
        } else {
            rd = new BufferedReader(new InputStreamReader(conn.getErrorStream(), "UTF-8"));
        }

        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = rd.readLine()) != null) {
            sb.append(line);
        }

        /*TODO: get words*/
        String[] words = {"음식", "생물", "녹색", "청소", "하수처리", "폐수", "폐기물", "대형폐기물", "폐목재", "폐매트리스", "폐터널"
                , "환경미화", "전용차량", "운행", "인터넷회선", "자동차종합보험", "상시보수공사", "전기안전관리", "콘텐츠 제작", "방과후학교"
                , "사진 공모전", "홍보사업", "성과평가", "사용성평가", "시설개선사업", "학교급식", "웹진 제작", "도로유지보수사업"
        };
        if (conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
            ObjectMapper objectMapper = new ObjectMapper();
            HashMap map = objectMapper.readValue(sb.toString(), HashMap.class);
            Map<String, Object> response = (Map<String, Object>) map.get("response");
            Map<String, Object> body = (Map<String, Object>) response.get("body");

            List<Map<String, String>> mapList = new ArrayList<>();
            mapList = (List<Map<String, String>>) body.get("items");

            mapList.forEach(c -> {
                boolean found = false;
                for (String word : words) {
                    if (c.get("bidNtceNm").contains(word)) {
                        found = true;
                        break;
                    }
                }

                if (!found) {
                    BidNoticeKey bidNoticeKey = new BidNoticeKey(c.get("bidNtceNo"), c.get("bidNtceOrd"));
                    Optional<BidNotice> bidNotice = bidNoticeRepository.findById(bidNoticeKey);
                    if (bidNotice.isPresent()) {
                    } else {
                        BidNotice bn = new BidNotice(
                                c.get("bidNtceNo"),
                                c.get("bidNtceOrd"),
                                c.get("reNtceYn"),
                                c.get("rgstTyNm"),
                                c.get("ntceKindNm"),
                                c.get("bidNtceDt"),
                                c.get("bidNtceNm"),
                                c.get("ntceInsttCd"),
                                c.get("ntceInsttNm"),
                                c.get("dminsttCd"),
                                c.get("dminsttNm"),
                                c.get("bidMethdNm"),
                                c.get("cntrctCnclsMthdNm"),
                                c.get("ntceInsttOfclNm"),
                                c.get("ntceInsttOfclTelNo"),
                                c.get("ntceInsttOfclEmailAdrs"),
                                c.get("exctvNm"),
                                c.get("bidGeginDt"),
                                c.get("bidClseDt"),
                                c.get("opengDt"),
                                c.get("ntceSpecDocUrl1"),
                                c.get("ntceSpecDocUrl2"),
                                c.get("ntceSpecDocUrl3"),
                                c.get("ntceSpecDocUrl4"),
                                c.get("ntceSpecDocUrl5"),
                                c.get("ntceSpecDocUrl6"),
                                c.get("ntceSpecDocUrl7"),
                                c.get("ntceSpecDocUrl8"),
                                c.get("ntceSpecDocUrl9"),
                                c.get("ntceSpecDocUrl10"),
                                c.get("ntceSpecFileNm1"),
                                c.get("ntceSpecFileNm2"),
                                c.get("ntceSpecFileNm3"),
                                c.get("ntceSpecFileNm4"),
                                c.get("ntceSpecFileNm5"),
                                c.get("ntceSpecFileNm6"),
                                c.get("ntceSpecFileNm7"),
                                c.get("ntceSpecFileNm8"),
                                c.get("ntceSpecFileNm9"),
                                c.get("ntceSpecFileNm10"),
                                c.get("rbidPermsnYn"),
                                c.get("prearngPrceDcsnMthdNm"),
                                c.get("totPrdprcNum"),
                                c.get("drwtPrdprcNum"),
                                c.get("asignBdgtAmt"),
                                c.get("presmptPrce"),
                                c.get("opengPlce"),
                                c.get("dcmtgOprtnDt"),
                                c.get("dcmtgOprtnPlce"),
                                c.get("bidNtceDtlUrl"),
                                c.get("bidNtceUrl"),
                                c.get("crdtrNm"),
                                c.get("cmmnSpldmdMethdNm"),
                                c.get("sucsfbidMthdCd"),
                                c.get("sucsfbidMthdNm"),
                                c.get("dsgntCmptYn"),
                                c.get("linkInsttNm"),
                                c.get("dminsttOfclEmailAdrs"),
                                c.get("rbidOpengDt"),
                                c.get("indstrytyLmtYn"),
                                c.get("rgstDt"),
                                c.get("chgDt"),
                                LocalDateTime.now()
                        );
                        bidNoticeRepository.save(bn);
                    }
                }
            });
        } else {
            //
        }

        rd.close();
        conn.disconnect();
        System.out.println(sb.toString());
    }
}
