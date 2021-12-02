package com.jonghyun.koneps.api.system.seq;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "tb_seq")
public class Seq {
    @Id
    @Column(name = "seq_type")
    String seqType;

    @Column(name = "seq")
    String seq;

    @Column(name = "created_by")
    String createdBy;

    @Column(name = "creation_date")
    LocalDateTime creationDate;

    @Column(name = "modified_by")
    String modifiedBy;

    @Column(name = "modified_date")
    LocalDateTime modifiedDate;

    public Seq newSeq(String seqType, String seq, String createdBy, LocalDateTime creationDate) {
        this.seqType = seqType;
        this.seq = seq;
        this.createdBy = createdBy;
        this.creationDate = creationDate;

        return this;
    }

    public Seq editSeq(String seqType, String seq, String createdBy, LocalDateTime creationDate, String modifiedBy, LocalDateTime modifiedDate) {
        this.seqType = seqType;
        this.seq = seq;
        this.createdBy = createdBy;
        this.creationDate = creationDate;
        this.modifiedBy = modifiedBy;
        this.modifiedDate = modifiedDate;

        return this;
    }
}
