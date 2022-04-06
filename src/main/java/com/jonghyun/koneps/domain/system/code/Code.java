package com.jonghyun.koneps.domain.system.code;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@IdClass(CodeKey.class)
@Table(name = "a_code")
public class Code {
    @Id
    @Column(name = "code")
    String code;

    @Column(name = "sorting_number")
    Long sortingNumber;

    @Column(name = "code_type")
    String codeType;

    @Id
    @Column(name = "upper_code")
    String upperCode;

    @Column(name = "value")
    String value;

    @Column(name = "code_desc")
    String codeDesc;

    @Column(name = "created_by")
    String createdBy;

    @Column(name = "creation_dt")
    LocalDateTime creationDate;

    @Column(name = "modified_by")
    String modifiedBy;

    @Column(name = "modified_dt")
    LocalDateTime modifiedDate;

    @Builder
    public Code(String code, Long sortingNumber, String codeType, String upperCode, String codeDesc, String createdBy, LocalDateTime creationDate, String modifiedBy, LocalDateTime modifiedDate) {
        this.code = code;
        this.sortingNumber = sortingNumber;
        this.codeType = codeType;
        this.upperCode = upperCode;
        this.codeDesc = codeDesc;
        this.createdBy = createdBy;
        this.creationDate = creationDate;
        this.modifiedBy = modifiedBy;
        this.modifiedDate = modifiedDate;
    }
}
