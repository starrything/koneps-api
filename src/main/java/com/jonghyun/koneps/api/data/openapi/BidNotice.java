package com.jonghyun.koneps.api.data.openapi;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@IdClass(BidNoticeKey.class)
@Table(name = "if_bidntce")
public class BidNotice {
    /*입찰공고번호: 년도(4)+월(2)+순번(5)*/
    @Id
    @Column(name = "bid_ntce_no")
    String bidNtceNo;

    /*재공고 및 재입찰 등이 발생되었을 경우 증가되는 수*/
    @Id
    @Column(name = "bid_ntce_ord")
    String bidNtceOrd;

    /*재공고 여부(Y/N)*/
    @Column(name = "re_ntce_yn")
    String reNtceYn;

    /*등록유형명*/
    @Column(name = "rgst_ty_nm")
    String rgstTyNm;

    /*공고종류명*/
    @Column(name = "ntce_kind_nm")
    String ntceKindNm;

    /*입찰공고일시*/
    @Column(name = "bid_ntce_dt")
    String bidNtceDt;

    /*입찰공고명*/
    @Column(name = "bid_ntce_nm")
    String bidNtceNm;

    /*공고기관코드*/
    @Column(name = "ntce_instt_cd")
    String ntceInsttCd;

    /*공고기관명*/
    @Column(name = "ntce_instt_nm")
    String ntceInsttNm;

    /*수요기관코드*/
    @Column(name = "dminstt_cd")
    String dminsttCd;

    /*수요기관명*/
    @Column(name = "dminstt_Nm")
    String dminsttNm;

    /*입찰방식명*/
    @Column(name = "bid_methd_nm")
    String bidMethdNm;

    /*계약체결방법명*/
    @Column(name = "cntrct_cncls_mthd_nm")
    String cntrctCnclsMthdNm;

    /*공고기관담당자명*/
    @Column(name = "ntce_instt_ofcl_nm")
    String ntceInsttOfclNm;

    /*공고기관담당자전화번호*/
    @Column(name = "ntce_instt_ofcl_tel_no")
    String ntceInsttOfclTelNo;

    /*공고기관담당자이메일주소*/
    @Column(name = "ntce_instt_ofcl_email_adrs")
    String ntceInsttOfclEmailAdrs;

    /*집행관명*/
    @Column(name = "exctv_nm")
    String exctvNm;

    /*입찰개시일시*/
    @Column(name = "bid_gegin_dt")
    String bidGeginDt;

    /*입찰마감일시*/
    @Column(name = "bid_clse_dt")
    String bidClseDt;

    /*개찰일시*/
    @Column(name = "openg_dt")
    String opengDt;

    /*공고규격서URL1*/
    @Column(name = "ntce_spec_doc_url1")
    String ntceSpecDocUrl1;
    @Column(name = "ntce_spec_doc_url2")
    String ntceSpecDocUrl2;
    @Column(name = "ntce_spec_doc_url3")
    String ntceSpecDocUrl3;
    @Column(name = "ntce_spec_doc_url4")
    String ntceSpecDocUrl4;
    @Column(name = "ntce_spec_doc_url5")
    String ntceSpecDocUrl5;
    @Column(name = "ntce_spec_doc_url6")
    String ntceSpecDocUrl6;
    @Column(name = "ntce_spec_doc_url7")
    String ntceSpecDocUrl7;
    @Column(name = "ntce_spec_doc_url8")
    String ntceSpecDocUrl8;
    @Column(name = "ntce_spec_doc_url9")
    String ntceSpecDocUrl9;
    @Column(name = "ntce_spec_doc_url10")
    String ntceSpecDocUrl10;

    /*공고규격파일명1*/
    @Column(name = "ntce_spec_file_nm1")
    String ntceSpecFileNm1;
    @Column(name = "ntce_spec_file_nm2")
    String ntceSpecFileNm2;
    @Column(name = "ntce_spec_file_nm3")
    String ntceSpecFileNm3;
    @Column(name = "ntce_spec_file_nm4")
    String ntceSpecFileNm4;
    @Column(name = "ntce_spec_file_nm5")
    String ntceSpecFileNm5;
    @Column(name = "ntce_spec_file_nm6")
    String ntceSpecFileNm6;
    @Column(name = "ntce_spec_file_nm7")
    String ntceSpecFileNm7;
    @Column(name = "ntce_spec_file_nm8")
    String ntceSpecFileNm8;
    @Column(name = "ntce_spec_file_nm9")
    String ntceSpecFileNm9;
    @Column(name = "ntce_spec_file_nm10")
    String ntceSpecFileNm10;

    /*재입찰허용여부*/
    @Column(name = "rbid_permsn_yn")
    String rbidPermsnYn;

    /*예정가격결정방법명*/
    @Column(name = "prearng_prce_dcsn_mthd_nm")
    String prearngPrceDcsnMthdNm;

    /*총예가건수*/
    @Column(name = "tot_prdprc_num")
    String totPrdprcNum;

    /*추첨예가건수*/
    @Column(name = "drwt_prdprc_num")
    String drwtPrdprcNum;

    /*예산금액*/
    @Column(name = "asign_bdgt_amt")
    String asignBdgtAmt;

    /*추정가격*/
    @Column(name = "presmpt_prce")
    String presmptPrce;

    /*개찰장소*/
    @Column(name = "openg_plce")
    String opengPlce;

    /*설명회일시*/
    @Column(name = "dcmtg_oprtn_dt")
    String dcmtgOprtnDt;

    /*설명회장소*/
    @Column(name = "dcmtg_oprtn_plce")
    String dcmtgOprtnPlce;

    /*입찰공고상세URL*/
    @Column(name = "bid_ntce_dtl_url")
    String bidNtceDtlUrl;

    /*입찰공고URL*/
    @Column(name = "bid_ntce_url")
    String bidNtceUrl;

    /*채권자명*/
    @Column(name = "crdtr_nm")
    String crdtrNm;

    /*공동수급방식명*/
    @Column(name = "cmmn_spldmd_methd_nm")
    String cmmnSpldmdMethdNm;

    /*낙찰방법코드*/
    @Column(name = "sucsfbid_mthd_cd")
    String sucsfbidMthdCd;

    /*낙찰방법명*/
    @Column(name = "sucsfbid_mthd_nm")
    String sucsfbidMthdNm;

    /*지명경쟁여부*/
    @Column(name = "dsgnt_cmpt_yn")
    String dsgntCmptYn;

    /*연계기관명*/
    @Column(name = "link_instt_nm")
    String linkInsttNm;

    /*수요기관담당자이메일주소*/
    @Column(name = "dminstt_ofcl_email_adrs")
    String dminsttOfclEmailAdrs;

    /*재입찰개찰일시*/
    @Column(name = "rbid_openg_dt")
    String rbidOpengDt;

    /*업종제한여부*/
    @Column(name = "indstryty_lmt_yn")
    String indstrytyLmtYn;

    /*등록일시*/
    @Column(name = "rgst_dt")
    String rgstDt;

    /*변경일시*/
    @Column(name = "chg_dt")
    String chgDt;

    /*인터페이스 시간*/
    @Column(name = "interface_dt")
    LocalDateTime interfaceDt;

    @Builder
    public BidNotice(String bidNtceNo, String bidNtceOrd, String reNtceYn, String rgstTyNm, String ntceKindNm, String bidNtceDt, String bidNtceNm, String ntceInsttCd, String ntceInsttNm, String dminsttCd, String dminsttNm, String bidMethdNm, String cntrctCnclsMthdNm, String ntceInsttOfclNm, String ntceInsttOfclTelNo, String ntceInsttOfclEmailAdrs, String exctvNm, String bidGeginDt, String bidClseDt, String opengDt,
                     String ntceSpecDocUrl1, String ntceSpecDocUrl2, String ntceSpecDocUrl3, String ntceSpecDocUrl4, String ntceSpecDocUrl5, String ntceSpecDocUrl6, String ntceSpecDocUrl7, String ntceSpecDocUrl8, String ntceSpecDocUrl9, String ntceSpecDocUrl10,
                     String ntceSpecFileNm1, String ntceSpecFileNm2, String ntceSpecFileNm3, String ntceSpecFileNm4, String ntceSpecFileNm5, String ntceSpecFileNm6, String ntceSpecFileNm7, String ntceSpecFileNm8, String ntceSpecFileNm9, String ntceSpecFileNm10,
                     String rbidPermsnYn,
                     String prearngPrceDcsnMthdNm, String totPrdprcNum, String drwtPrdprcNum, String asignBdgtAmt, String presmptPrce, String opengPlce, String dcmtgOprtnDt, String dcmtgOprtnPlce, String bidNtceDtlUrl, String bidNtceUrl, String crdtrNm, String cmmnSpldmdMethdNm, String sucsfbidMthdCd, String sucsfbidMthdNm, String dsgntCmptYn, String linkInsttNm, String dminsttOfclEmailAdrs, String rbidOpengDt, String indstrytyLmtYn, String rgstDt, String chgDt, LocalDateTime interfaceDt) {
        this.bidNtceNo = bidNtceNo;
        this.bidNtceOrd = bidNtceOrd;
        this.reNtceYn = reNtceYn;
        this.rgstTyNm = rgstTyNm;
        this.ntceKindNm = ntceKindNm;
        this.bidNtceDt = bidNtceDt;
        this.bidNtceNm = bidNtceNm;
        this.ntceInsttCd = ntceInsttCd;
        this.ntceInsttNm = ntceInsttNm;
        this.dminsttCd = dminsttCd;
        this.dminsttNm = dminsttNm;
        this.bidMethdNm = bidMethdNm;
        this.cntrctCnclsMthdNm = cntrctCnclsMthdNm;
        this.ntceInsttOfclNm = ntceInsttOfclNm;
        this.ntceInsttOfclTelNo = ntceInsttOfclTelNo;
        this.ntceInsttOfclEmailAdrs = ntceInsttOfclEmailAdrs;
        this.exctvNm = exctvNm;
        this.bidGeginDt = bidGeginDt;
        this.bidClseDt = bidClseDt;
        this.opengDt = opengDt;
        this.ntceSpecDocUrl1 = ntceSpecDocUrl1;
        this.ntceSpecDocUrl2 = ntceSpecDocUrl2;
        this.ntceSpecDocUrl3 = ntceSpecDocUrl3;
        this.ntceSpecDocUrl4 = ntceSpecDocUrl4;
        this.ntceSpecDocUrl5 = ntceSpecDocUrl5;
        this.ntceSpecDocUrl6 = ntceSpecDocUrl6;
        this.ntceSpecDocUrl7 = ntceSpecDocUrl7;
        this.ntceSpecDocUrl8 = ntceSpecDocUrl8;
        this.ntceSpecDocUrl9 = ntceSpecDocUrl9;
        this.ntceSpecDocUrl10 = ntceSpecDocUrl10;
        this.ntceSpecFileNm1 = ntceSpecFileNm1;
        this.ntceSpecFileNm2 = ntceSpecFileNm2;
        this.ntceSpecFileNm3 = ntceSpecFileNm3;
        this.ntceSpecFileNm4 = ntceSpecFileNm4;
        this.ntceSpecFileNm5 = ntceSpecFileNm5;
        this.ntceSpecFileNm6 = ntceSpecFileNm6;
        this.ntceSpecFileNm7 = ntceSpecFileNm7;
        this.ntceSpecFileNm8 = ntceSpecFileNm8;
        this.ntceSpecFileNm9 = ntceSpecFileNm9;
        this.ntceSpecFileNm10 = ntceSpecDocUrl10;
        this.rbidPermsnYn = rbidPermsnYn;
        this.prearngPrceDcsnMthdNm = prearngPrceDcsnMthdNm;
        this.totPrdprcNum = totPrdprcNum;
        this.drwtPrdprcNum = drwtPrdprcNum;
        this.asignBdgtAmt = asignBdgtAmt;
        this.presmptPrce = presmptPrce;
        this.opengPlce = opengPlce;
        this.dcmtgOprtnDt = dcmtgOprtnDt;
        this.dcmtgOprtnPlce = dcmtgOprtnPlce;
        this.bidNtceDtlUrl = bidNtceDtlUrl;
        this.bidNtceUrl = bidNtceUrl;
        this.crdtrNm = crdtrNm;
        this.cmmnSpldmdMethdNm = cmmnSpldmdMethdNm;
        this.sucsfbidMthdCd = sucsfbidMthdCd;
        this.sucsfbidMthdNm = sucsfbidMthdNm;
        this.dsgntCmptYn = dsgntCmptYn;
        this.linkInsttNm = linkInsttNm;
        this.dminsttOfclEmailAdrs = dminsttOfclEmailAdrs;
        this.rbidOpengDt = rbidOpengDt;
        this.indstrytyLmtYn = indstrytyLmtYn;
        this.rgstDt = rgstDt;
        this.chgDt = chgDt;
        this.interfaceDt = interfaceDt;
    }
}
