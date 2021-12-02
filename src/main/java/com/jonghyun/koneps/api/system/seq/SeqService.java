package com.jonghyun.koneps.api.system.seq;

import javax.transaction.Transactional;

@Transactional
public interface SeqService {
    String getSequenceBySeqType(String type);
}
