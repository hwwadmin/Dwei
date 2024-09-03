package com.dwei.framework.dict.component;

import com.dwei.core.guide.component.ComponentService;
import com.dwei.framework.dict.utils.DictUtils;
import org.springframework.stereotype.Component;

/**
 * 字典组件
 *
 * @author hww
 */
@Component
public class DictComponent implements ComponentService {

    @Override
    public String getName() {
        return "字典组件";
    }

    @Override
    public void start() {
        DictUtils.refresh(false);
    }

    @Override
    public void stop() {

    }

}
