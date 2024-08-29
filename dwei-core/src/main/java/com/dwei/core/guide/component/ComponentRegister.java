package com.dwei.core.guide.component;

import com.dwei.common.utils.Lists;
import com.dwei.common.utils.ObjectUtils;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 组件自动注册
 *
 * @author hww
 */
@Component
public class ComponentRegister {

    private final List<ComponentService> componentServices;

    public ComponentRegister(List<ComponentService> componentServices) {
        this.componentServices = componentServices;
    }

    public List<ComponentService> getComponent() {
        return ObjectUtils.nonNull(componentServices) ? componentServices : Lists.of();
    }

}
