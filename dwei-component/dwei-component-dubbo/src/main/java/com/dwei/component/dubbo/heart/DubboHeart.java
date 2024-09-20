package com.dwei.component.dubbo.heart;

import org.apache.dubbo.config.annotation.DubboService;

@DubboService
public class DubboHeart implements IDubboHeart {

    private static final String HEART_MSG = "hello Dubbo";

    @Override
    public String ping() {
        return HEART_MSG;
    }

}
