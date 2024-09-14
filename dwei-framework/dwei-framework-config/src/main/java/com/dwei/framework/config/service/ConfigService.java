package com.dwei.framework.config.service;

import com.dwei.common.utils.Assert;
import com.dwei.core.mvc.pojo.response.PageResponse;
import com.dwei.domain.entity.ConfigEntity;
import com.dwei.domain.query.config.ConfigQuery;
import com.dwei.domain.repository.IConfigRepository;
import com.dwei.framework.config.domain.request.ConfigAddRequest;
import com.dwei.framework.config.domain.request.ConfigQueryRequest;
import com.dwei.framework.config.domain.response.ConfigResponse;
import com.dwei.framework.config.utils.ConfigUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ConfigService {

    private final IConfigRepository configRepository;

    public PageResponse<ConfigResponse> list(ConfigQueryRequest request) {
        var configs = configRepository.autoPage(ConfigQuery.builder()
                .name(request.getName())
                .build());
        return PageResponse.of(configs, this::covertResponse);
    }

    @Transactional(rollbackFor = {Exception.class, RuntimeException.class})
    public void add(ConfigAddRequest request) {
        Assert.isFalse(configRepository.lambdaQuery().eq(ConfigEntity::getKey, request.getKey()).exists(),
                "重复的参数键");

        var config = ConfigEntity.builder()
                .name(request.getName())
                .key(request.getKey())
                .value(request.getValue())
                .isSystem(false)
                .remark(request.getRemark())
                .build();

        configRepository.save(config);
        ConfigUtils.refresh(config.getKey());
    }

    private ConfigResponse covertResponse(ConfigEntity config) {
        return ConfigResponse.builder()
                .id(config.getId())
                .name(config.getName())
                .key(config.getKey())
                .value(config.getValue())
                .isSystem(config.getIsSystem())
                .remark(config.getRemark())
                .build();
    }

}
