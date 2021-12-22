package com.jonghyun.koneps.api.g2b.beforeSpec;

import com.jonghyun.koneps.api.data.openapi.BeforeSpecificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class BeforeSpecServiceImpl implements BeforeSpecService {
    private final BeforeSpecificationRepository beforeSpecificationRepository;

    @Override
    public List<Map<String, Object>> searchBeforeSpecList(String keyword) {
        List<Map<String, Object>> result = new ArrayList<>();
        beforeSpecificationRepository.findByBfSpecRgstNoContainsOrPrdctClsfcNoNmContainsOrRlDminsttNmContainsOrderByRcptDtDesc(keyword, keyword, keyword)
                .stream()
                .forEach(beforeSpec -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("rcptDt", beforeSpec.getRcptDt());
                    map.put("bfSpecRgstNo", beforeSpec.getBfSpecRgstNo());
                    map.put("prdctClsfcNoNm", beforeSpec.getPrdctClsfcNoNm());
                    map.put("asignBdgtAmt", beforeSpec.getAsignBdgtAmt());
                    map.put("opninRgstClseDt", beforeSpec.getOpninRgstClseDt());
                    map.put("rlDminsttNm", beforeSpec.getRlDminsttNm());
                    map.put("dlvrTmlmtDt", beforeSpec.getDlvrTmlmtDt());
                    map.put("asignBdgtAmt", beforeSpec.getAsignBdgtAmt());
                    map.put("dlvrDaynum", beforeSpec.getDlvrDaynum());
                    map.put("specDocFileUrl1", beforeSpec.getSpecDocFileUrl1());
                    map.put("specDocFileUrl2", beforeSpec.getSpecDocFileUrl2());

                    result.add(map);
                });

        return result;
    }
}
