package com.jonghyun.koneps.domain.openapi;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Getter
@NoArgsConstructor
public class BidNoticeKey implements Serializable {
    /*입찰공고번호: 년도(4)+월(2)+순번(5)*/
    String bidNtceNo;

    /*재공고 및 재입찰 등이 발생되었을 경우 증가되는 수*/
    String bidNtceOrd;

    @Builder
    public BidNoticeKey(String bidNtceNo, String bidNtceOrd) {
        this.bidNtceNo = bidNtceNo;
        this.bidNtceOrd = bidNtceOrd;
    }
}
