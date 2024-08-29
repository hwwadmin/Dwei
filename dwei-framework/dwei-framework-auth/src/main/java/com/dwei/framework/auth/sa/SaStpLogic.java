package com.dwei.framework.auth.sa;

import cn.dev33.satoken.stp.StpLogic;
import org.springframework.stereotype.Component;

/**
 * 账号体系
 * <p>
 * 组件都是通过 SaBeanInject.setStpLogic 注入 StpUtil
 *
 * @see cn.dev33.satoken.spring.SaBeanInject
 */
@Component
public class SaStpLogic extends StpLogic {

    /**
     * 初始化StpLogic, 并指定账号类型
     */
    public SaStpLogic() {
        super("admin");
    }

}
