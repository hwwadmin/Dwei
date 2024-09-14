package com.dwei.framework.dict;

import com.dwei.core.mvc.pojo.response.PageResponse;
import com.dwei.framework.dict.domain.request.*;
import com.dwei.framework.dict.domain.response.DictResponse;
import com.dwei.framework.dict.service.DictService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 字典管理 Controller
 *
 * @author hww
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/system/dict")
public class DictController {

    private final DictService dictService;

    /**
     * 分页查询
     */
    @GetMapping
    public PageResponse<DictResponse> page(DictPageRequest request) {
        return dictService.page(request);
    }

    /**
     * 根据字典类型编码查询字典
     */
    @GetMapping("/{code}")
    public DictResponse code(@PathVariable String code) {
        return dictService.code(code);
    }

    /**
     * 新增字典类型
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

    /**
     * 更新字典类型
     */
    @PostMapping("/update")
    public void updateDict(@Valid @RequestBody DictUpdateRequest request) {
        dictService.updateDict(request);
    }

    /**
     * 更新字典数据
     */
    @PostMapping("/data/update")
    public void updateDictData(@Valid @RequestBody DictDataUpdateRequest request) {
        dictService.updateDictData(request);
    }

    /**
     * 强制刷新字典缓存
     */
    @PostMapping("/refresh")
    public void refresh() {
        dictService.refresh();
    }

}
