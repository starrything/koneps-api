package com.jonghyun.koneps.api.system.code;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Getter
@NoArgsConstructor
public class CodeKey implements Serializable {
    String code;
    String upperCode;

    @Builder
    public CodeKey(String code, String upperCode) {
        this.code = code;
        this.upperCode = upperCode;
    }
}
