package com.jonghyun.koneps.domain.system.seq;

import javax.transaction.Transactional;

@Transactional
public interface SeqService {
    String getSequenceBySeqPrefix(String type);
}
