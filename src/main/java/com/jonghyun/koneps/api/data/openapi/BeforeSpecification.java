package com.jonghyun.koneps.api.data.openapi;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@IdClass(BeforeSpecificationKey.class)
@Table(name = "if_bfspec")
public class BeforeSpecification {
    /*사전규격등록번호*/
    @Id
    @Column(name = "bf_spec_rgst_no")
    String bfSpecRgstNo;

    /*등록일시*/
    @Id
    @Column(name = "rgst_dt")
    String rgstDt;

    /*업무구분명*/
    @Column(name = "bsns_div_nm")
    String bsnsDivNm;

    /*참조번호*/
    @Column(name = "ref_no")
    String refNo;

    /*품명(사업명)/물품분류명*/
    @Column(name = "prdct_clsfc_no_nm")
    String prdctClsfcNoNm;

    /*발주기관의 명*/
    @Column(name = "order_instt_nm")
    String orderInsttNm;

    /*실수요기관의 명*/
    @Column(name = "rl_dminstt_nm")
    String rlDminsttNm;

    /*배정예산액(원화,원)*/
    @Column(name = "asign_bdgt_amt")
    String asignBdgtAmt;

    /*접수일시 ‘YYYY-MM-DD HH:MM:SS’*/
    @Column(name = "rcpt_dt")
    String rcptDt;

    /*의견등록마감일시 ‘YYYY-MM-DD HH:MM:SS’*/
    @Column(name = "opnin_rgst_clse_dt")
    String opninRgstClseDt;

    /*사전규격정보의 담당자전화번호*/
    @Column(name = "ofcl_tel_no")
    String ofclTelNo;

    /*사전규격정보의 담당자명*/
    @Column(name = "ofcl_nm")
    String ofclNm;

    /*SW사업대상여부*/
    @Column(name = "sw_biz_obj_yn")
    String swBizObjYn;

    /*납품기한일시 ‘YYYY-MM-DD HH:MM:SS’*/
    @Column(name = "dlvr_tmlmt_dt")
    String dlvrTmlmtDt;

    /*납품일수*/
    @Column(name = "dlvr_daynum")
    String dlvrDaynum;

    /*규격서화일1*/
    @Column(name = "spec_docfile_url1")
    String specDocfileUrl1;

    /*규격서화일2*/
    @Column(name = "spec_docfile_url2")
    String specDocfileUrl2;

    /*규격서화일3*/
    @Column(name = "spec_docfile_url3")
    String specDocfileUrl3;

    /*규격서화일4*/
    @Column(name = "spec_docfile_url4")
    String specDocfileUrl4;

    /*규격서화일5*/
    @Column(name = "spec_docfile_url5")
    String specDocfileUrl5;

    /*물품상세목록 [사전규격물품순번^세부품명번호^세부품명],*/
    @Column(name = "prdct_dtl_list")
    String prdctDtlList;

    /*변경일시 ‘YYYY-MM-DD HH:MM:SS’*/
    @Column(name = "chg_dt")
    String chgDt;

    /*관련된 입찰공고번호 목록 ‘입찰공고번호1,입찰공고번호2’*/
    @Column(name = "bid_ntce_no_list")
    String bidNtceNoList;

    /*인터페이스 시간*/
    @Column(name = "interface_dt")
    LocalDateTime interfaceDt;

    @Builder
    public BeforeSpecification(String bfSpecRgstNo, String rgstDt, String bsnsDivNm, String refNo, String prdctClsfcNoNm, String orderInsttNm, String rlDminsttNm, String asignBdgtAmt, String rcptDt, String opninRgstClseDt, String ofclTelNo, String ofclNm, String swBizObjYn, String dlvrTmlmtDt, String dlvrDaynum, String specDocfileUrl1, String specDocfileUrl2, String specDocfileUrl3, String specDocfileUrl4, String specDocfileUrl5, String prdctDtlList, String chgDt, String bidNtceNoList, LocalDateTime interfaceDt) {
        this.bfSpecRgstNo = bfSpecRgstNo;
        this.rgstDt = rgstDt;
        this.bsnsDivNm = bsnsDivNm;
        this.refNo = refNo;
        this.prdctClsfcNoNm = prdctClsfcNoNm;
        this.orderInsttNm = orderInsttNm;
        this.rlDminsttNm = rlDminsttNm;
        this.asignBdgtAmt = asignBdgtAmt;
        this.rcptDt = rcptDt;
        this.opninRgstClseDt = opninRgstClseDt;
        this.ofclTelNo = ofclTelNo;
        this.ofclNm = ofclNm;
        this.swBizObjYn = swBizObjYn;
        this.dlvrTmlmtDt = dlvrTmlmtDt;
        this.dlvrDaynum = dlvrDaynum;
        this.specDocfileUrl1 = specDocfileUrl1;
        this.specDocfileUrl2 = specDocfileUrl2;
        this.specDocfileUrl3 = specDocfileUrl3;
        this.specDocfileUrl4 = specDocfileUrl4;
        this.specDocfileUrl5 = specDocfileUrl5;
        this.prdctDtlList = prdctDtlList;
        this.chgDt = chgDt;
        this.bidNtceNoList = bidNtceNoList;
        this.interfaceDt = interfaceDt;
    }
}
