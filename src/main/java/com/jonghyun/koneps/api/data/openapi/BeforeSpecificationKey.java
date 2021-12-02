package com.jonghyun.koneps.api.data.openapi;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Getter
@NoArgsConstructor
public class BeforeSpecificationKey implements Serializable {
    /*사전규격등록번호*/
    String bfSpecRgstNo;

    /*등록일시*/
    String rgstDt;

    @Builder
    public BeforeSpecificationKey(String bfSpecRgstNo, String rgstDt) {
        this.bfSpecRgstNo = bfSpecRgstNo;
        this.rgstDt = rgstDt;
    }
}
