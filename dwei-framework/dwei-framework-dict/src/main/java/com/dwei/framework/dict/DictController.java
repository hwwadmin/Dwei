package com.dwei.framework.dict;

import com.dwei.core.mvc.pojo.response.PageResponse;
import com.dwei.framework.dict.domain.request.DictAddRequest;
import com.dwei.framework.dict.domain.request.DictDataAddRequest;
import com.dwei.framework.dict.domain.request.DictQueryRequest;
import com.dwei.framework.dict.domain.response.DictResponse;
import com.dwei.framework.dict.service.DictService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

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

    /**
     * 分页查询
     */
    @GetMapping
    public PageResponse<DictResponse> list(DictQueryRequest request) {
        return dictService.list(request);
    }

    /**
     * 根据字典类型编码查询字典
     */
    @GetMapping("/{code}")
    public DictResponse code(@PathVariable String code) {
        return dictService.code(code);
    }

    /**
     * 新增字典分类
     */
    @PostMapping("/add")
    public void addDict(@Valid @RequestBody DictAddRequest request) {
        dictService.addDict(request);
    }

    /**
     * 新增字典数据
     */
    @PostMapping("/data/add")
    public void addDictData(@Valid @RequestBody DictDataAddRequest request) {
        dictService.addDictData(request);
    }

}
