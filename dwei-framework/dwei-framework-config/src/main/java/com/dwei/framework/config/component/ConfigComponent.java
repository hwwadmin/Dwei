package com.dwei.framework.config.component;

import com.dwei.core.guide.component.ComponentService;
import com.dwei.framework.config.utils.ConfigUtils;
import org.springframework.stereotype.Component;

/**
 * 参数配置组件
 *
 * @author hww
 */
@Component
public class ConfigComponent implements ComponentService {

    @Override
    public String getName() {
        return "参数配置组件";
    }

    @Override
    public void start() {
        ConfigUtils.refresh(false);
    }

    @Override
    public void stop() {

    }

}
