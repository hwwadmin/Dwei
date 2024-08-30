package com.dwei.framework.dict;

import com.dwei.framework.dict.service.DictService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 管理员用户 Controller
 *
 * @author hww
 */
@RestController
@AllArgsConstructor
@RequestMapping("/system/dict")
public class DictController {

    private final DictService dictService;

}
