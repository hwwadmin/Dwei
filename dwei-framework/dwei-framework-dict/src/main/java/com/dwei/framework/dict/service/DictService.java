package com.dwei.framework.dict.service;

import com.dwei.domain.entity.DictEntity;
import com.dwei.domain.repository.IDictDataRepository;
import com.dwei.domain.repository.IDictRepository;
import com.dwei.framework.dict.domain.request.DictAddRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DictService {

    private final IDictRepository dictRepository;
    private final IDictDataRepository dictDataRepository;

    public void addDict(DictAddRequest request) {
        var dict = DictEntity.builder()
                .name(request.getName())
                .code(request.getCode())
                .remark(request.getRemark())
                .build();
        dict.init();
        dictRepository.save(dict);
    }

}
