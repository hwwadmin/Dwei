package com.dwei.framework.dict.service;

import com.dwei.common.utils.Assert;
import com.dwei.common.utils.Lists;
import com.dwei.common.utils.ObjectUtils;
import com.dwei.core.mvc.page.PageUtils;
import com.dwei.core.mvc.pojo.response.PageResponse;
import com.dwei.domain.entity.DictDataEntity;
import com.dwei.domain.entity.DictEntity;
import com.dwei.domain.query.dict.DictQuery;
import com.dwei.domain.query.dictdata.DictDataQuery;
import com.dwei.domain.repository.IDictDataRepository;
import com.dwei.domain.repository.IDictRepository;
import com.dwei.framework.dict.domain.request.*;
import com.dwei.framework.dict.domain.response.DictDataResponse;
import com.dwei.framework.dict.domain.response.DictResponse;
import com.dwei.framework.dict.utils.DictUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DictService {

    private final IDictRepository dictRepository;
    private final IDictDataRepository dictDataRepository;

    public PageResponse<DictResponse> list(DictQueryRequest request) {
        PageUtils.startPage();
        List<DictEntity> dicts = dictRepository.query(DictQuery.builder()
                .name(request.getName())
                .code(request.getCode())
                .build());
        if (ObjectUtils.isNull(dicts)) return PageResponse.empty();

        // dict data
        List<String> dictCodeList = Lists.map(dicts, DictEntity::getCode);
        List<DictDataEntity> dictDataEntities = dictDataRepository.query(DictDataQuery.builder()
                .dictCodeIn(dictCodeList)
                .build());
        Map<String, List<DictDataResponse>> dictDataGroup = dictDataEntities.stream()
                .map(this::convertResponse)
                .collect(Collectors.groupingBy(DictDataResponse::getDictCode));

        return PageResponse.of(dicts, t -> convertResponse(t, dictDataGroup));
    }

    public DictResponse code(String code) {
        var dict = DictUtils.get(code);
        Assert.nonNull(dict.getFirst(), "不存在的字典类型");
        return convertResponse(dict.getFirst(), Lists.map(dict.getSecond(), this::convertResponse));
    }

    @Transactional(rollbackFor = {Exception.class, RuntimeException.class})
    public void addDict(DictAddRequest request) {
        Assert.isFalse(dictRepository.existsByCode(request.getCode()), "重复的字段类型");

        var dict = DictEntity.builder()
                .name(request.getName())
                .code(request.getCode())
                .remark(request.getRemark())
                .enable(true)
                .build();
        dict.init();

        dictRepository.save(dict);
        DictUtils.refresh(dict.getCode());
    }

    @Transactional(rollbackFor = {Exception.class, RuntimeException.class})
    public void addDictData(DictDataAddRequest request) {
        Assert.isTrue(dictRepository.existsByCode(request.getDictCode()), "字段类型不存在");

        var dicData = DictDataEntity.builder()
                .dictCode(request.getDictCode())
                .seq(request.getSeq())
                .label(request.getLabel())
                .labelEn(request.getLabelEn())
                .value(request.getValue())
                .enable(true)
                .build();
        dicData.init();

        dictDataRepository.save(dicData);
        DictUtils.refresh(dicData.getDictCode());
    }

    @Transactional(rollbackFor = {Exception.class, RuntimeException.class})
    public void updateDict(DictUpdateRequest request) {
        var dict = dictRepository.getById(request.getId());
        Assert.nonNull(dict);

        dict.setName(request.getName());
        dict.setRemark(request.getRemark());
        dict.setEnable(request.getEnable());
        dict.update();

        dictRepository.saveOrUpdate(dict);
        DictUtils.refresh(dict.getCode());
    }

    @Transactional(rollbackFor = {Exception.class, RuntimeException.class})
    public void updateDictData(DictDataUpdateRequest request) {
        var dictData = dictDataRepository.getById(request.getId());
        Assert.nonNull(dictData);

        dictData.setSeq(request.getSeq());
        dictData.setLabel(request.getLabel());
        dictData.setLabelEn(request.getLabelEn());
        dictData.setValue(request.getValue());
        dictData.setEnable(request.getEnable());
        dictData.update();

        dictDataRepository.saveOrUpdate(dictData);
        DictUtils.refresh(dictData.getDictCode());
    }

    public void refresh() {
        DictUtils.refresh(true);
    }

    private DictResponse convertResponse(DictEntity dict, Map<String, List<DictDataResponse>> dictDataGroup) {
        return convertResponse(dict, dictDataGroup.get(dict.getCode()));
    }

    private DictResponse convertResponse(DictEntity dict, List<DictDataResponse> dictData) {
        return DictResponse.builder()
                .id(dict.getId())
                .name(dict.getName())
                .code(dict.getCode())
                .remark(dict.getRemark())
                .enable(dict.getEnable())
                .dictData(dictData)
                .build();
    }

    private DictDataResponse convertResponse(DictDataEntity dictDataEntity) {
        return DictDataResponse.builder()
                .id(dictDataEntity.getId())
                .dictCode(dictDataEntity.getDictCode())
                .seq(dictDataEntity.getSeq())
                .label(dictDataEntity.getLabel())
                .labelEn(dictDataEntity.getLabelEn())
                .value(dictDataEntity.getValue())
                .enable(dictDataEntity.getEnable())
                .build();
    }

}
