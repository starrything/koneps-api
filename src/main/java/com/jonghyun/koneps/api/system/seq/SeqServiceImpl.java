package com.jonghyun.koneps.api.system.seq;

import com.jonghyun.koneps.core.util.Util;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SeqServiceImpl implements SeqService {
    private final Util util;
    private final SeqRepository seqRepository;

    @Override
    public String getSequenceBySeqType(String seqType) {
        StringBuilder sequenceId = new StringBuilder();
        Optional<Seq> seq = seqRepository.findBySeqType(seqType);

        String loginId = util.getLoginId();
        if(seq.isPresent()) {
            String prevSeq = seq.get().getSeq();
            int currSeq = Integer.parseInt(prevSeq) + 1;
            int numOfDigits = (int) Math.log10(currSeq) + 1;

            sequenceId.append(seqType + "-");
            for (int i = 0; i < 5 - numOfDigits; i++) {
                sequenceId.append("0");
            }
            sequenceId.append(currSeq);

            // update Seq by seqType
            Seq editSeq = new Seq();
            editSeq.editSeq(
                    seqType,
                    String.valueOf(currSeq),
                    seq.get().getCreatedBy(),
                    seq.get().getCreationDate(),
                    loginId,
                    LocalDateTime.now());
            seqRepository.save(editSeq);
        } else {
            sequenceId.append(seqType + "-00001");

            // insert new Seq
            Seq newSeq = new Seq();
            newSeq.newSeq(
                    seqType,
                    "1",
                    loginId,
                    LocalDateTime.now());
            seqRepository.save(newSeq);
        }

        return sequenceId.toString();
    }
}
