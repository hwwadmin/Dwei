package com.dwei.framework.config;

import com.dwei.core.mvc.pojo.response.PageResponse;
import com.dwei.framework.config.domain.request.ConfigAddRequest;
import com.dwei.framework.config.domain.request.ConfigQueryRequest;
import com.dwei.framework.config.domain.response.ConfigResponse;
import com.dwei.framework.config.service.ConfigService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 参数配置管理 Controller
 *
 * @author hww
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/system/config")
public class ConfigController {

    private final ConfigService configService;

    /**
     * 分页查询
     */
    @GetMapping
    public PageResponse<ConfigResponse> list(ConfigQueryRequest request) {
        return configService.page(request);
    }

    /**
     * 新增参数配置
     */
    @PostMapping("/add")
    public void add(@Valid @RequestBody ConfigAddRequest request) {
        configService.add(request);
    }

}
