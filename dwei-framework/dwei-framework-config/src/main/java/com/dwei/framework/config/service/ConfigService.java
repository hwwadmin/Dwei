package com.dwei.framework.config.service;

import com.dwei.common.utils.Assert;
import com.dwei.common.utils.ObjectUtils;
import com.dwei.core.mvc.page.PageUtils;
import com.dwei.core.mvc.pojo.response.PageResponse;
import com.dwei.domain.entity.ConfigEntity;
import com.dwei.domain.repository.IConfigRepository;
import com.dwei.framework.config.domain.request.ConfigAddRequest;
import com.dwei.framework.config.domain.request.ConfigQueryRequest;
import com.dwei.framework.config.domain.response.ConfigResponse;
import com.dwei.framework.config.utils.ConfigUtils;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class ConfigService {

    private final IConfigRepository configRepository;

    public PageResponse<ConfigResponse> list(ConfigQueryRequest request) {
        PageUtils.startPage();
        var configs = configRepository.lambdaQuery()
                .eq(ObjectUtils.nonNull(request.getName()), ConfigEntity::getName, request.getName())
                .list();
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
        config.init();

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
