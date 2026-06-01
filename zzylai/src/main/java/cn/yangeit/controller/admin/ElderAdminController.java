package cn.yangeit.controller.admin;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.yangeit.config.BaseException;
import cn.yangeit.dto.admin.AdminPageDTO;
import cn.yangeit.mapper.ElderMapper;
import cn.yangeit.dto.admin.ElderAdminDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import cn.yangeit.pojo.Elder;
import cn.yangeit.common.AjaxResult;

import java.util.Date;


@RestController
@RequestMapping("/admin/elder")
@CrossOrigin
@Tag(name = "管理端-老人管理列表" ,description = "管理端-老人管理列表")
public class ElderAdminController {
    @Autowired
    ElderMapper elderMapper;

    // 分页查询
    @Operation(summary = "分页查询老人列表")
    @PostMapping("/page")
    @CrossOrigin
    public AjaxResult page(@RequestBody AdminPageDTO dto) {
        Page<Elder> page = new Page<>(dto.getPageNum(), dto.getPageSize());
        QueryWrapper<Elder> wrapper = new QueryWrapper<>();
        // 模糊匹配 name id_card_no phone
        wrapper.like(ObjectUtil.isNotEmpty(dto.getSearchKey()), "name", dto.getSearchKey())
                .or()
                .like(ObjectUtil.isNotEmpty(dto.getSearchKey()), "id_card_no", dto.getSearchKey())
                .or()
                .like(ObjectUtil.isNotEmpty(dto.getSearchKey()), "phone", dto.getSearchKey());
        wrapper.orderByDesc("update_time");
        Page<Elder> result = elderMapper.selectPage(page, wrapper);
        return AjaxResult.success(result);
    }


    @Operation(summary = "根据ID查询老人详情")
    @GetMapping("/{id}")
    public AjaxResult detail(@Parameter(description = "老人ID", required = true) @PathVariable Long id) {
        Elder elder = elderMapper.selectById(id);
        if (elder == null) {
            throw new BaseException("没有该老人");
        }
        return AjaxResult.success(elder);
    }

    // 修改
    @Operation(summary = "修改老人信息")
    @PutMapping("")
    public AjaxResult update(@RequestBody ElderAdminDTO dto) {
        Elder elder = BeanUtil.toBean(dto, Elder.class);
        elder.setUpdateTime(new Date());
        int rows = elderMapper.updateById(elder);
        if (rows > 0) return AjaxResult.success();
        return AjaxResult.error("修改失败");
    }

    // 删除
    @Operation(summary = "根据ID删除老人")
    @DeleteMapping("/{id}")
    public AjaxResult delete(@Parameter(description = "老人ID", required = true) @PathVariable Long id) {
        Elder elder = elderMapper.selectById(id);
        if (elder == null) {
            throw new BaseException("未找到该用户，无法删除");
        }

        int rows = elderMapper.deleteById(id);
        if (rows > 0) return AjaxResult.success();
        return AjaxResult.error("删除失败");
    }

    //增加老人
    @Operation(summary = "增加老人")
    @PostMapping("")
    public AjaxResult add(@RequestBody ElderAdminDTO dto) {
        Elder elder = BeanUtil.toBean(dto, Elder.class);
        elder.setId(null);
        elder.setStatus(1);
        elder.setCreateTime(new Date());
        elder.setUpdateTime(new Date());
        int rows = elderMapper.insert(elder);
        if (rows > 0) return AjaxResult.success();
        return AjaxResult.error("添加失败");
    }
}
