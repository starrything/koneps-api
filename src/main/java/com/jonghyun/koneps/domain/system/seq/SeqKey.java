package com.jonghyun.koneps.domain.system.seq;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
@Getter
@NoArgsConstructor
public class SeqKey implements Serializable {
    private String seqType;
    private String seq;

    @Builder
    public SeqKey(String seqType, String seq) {
        this.seqType = seqType;
        this.seq = seq;
    }
}
