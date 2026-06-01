package cn.yangeit.controller.admin;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.yangeit.common.AjaxResult;
import cn.yangeit.dto.admin.AdminPageDTO;
import cn.yangeit.dto.admin.NursingProjectAdminDTO;
import cn.yangeit.mapper.NursingProjectMapper;
import cn.yangeit.pojo.NursingProject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.*;

/**
 * 管理端-护理项目相关接口
 */
@RestController
@RequestMapping("/admin/project")
@Tag(name = "管理端-护理项目管理列表" ,description = "护理项目管理列表")
public class NursingProjectAdminController {
    @Autowired
    NursingProjectMapper nursingProjectMapper;

    // 分页查询
    @Operation(summary = "分页查询护理项目列表")
    @PostMapping("/page")
    public AjaxResult page(@RequestBody AdminPageDTO dto) {
        Page<NursingProject> page = new Page<>(dto.getPageNum(), dto.getPageSize());
        QueryWrapper<NursingProject> wrapper = new QueryWrapper<>();
        wrapper.eq(ObjectUtil.isNotEmpty(dto.getStatus()),  "status", dto.getStatus())
                .like(ObjectUtil.isNotEmpty(dto.getSearchKey()), "name", dto.getSearchKey());
        //创建时间降序，最新的在顶部
        wrapper.orderByDesc("create_time");

        Page<NursingProject> result = nursingProjectMapper.selectPage(page, wrapper);
        return AjaxResult.success(result);
    }

    // 详情
    @Operation(summary = "根据ID查询护理项目详情")
    @GetMapping("/{id}")
    public AjaxResult detail(@Parameter(description = "护理项目ID", required = true) @PathVariable Long id) {
        NursingProject project = nursingProjectMapper.selectById(id);
        if (project == null) return AjaxResult.error("未找到该护理项目");
        return AjaxResult.success(project);
    }

    // 修改
    @Operation(summary = "修改护理项目信息")
    @PutMapping("")
    public AjaxResult update(@RequestBody NursingProjectAdminDTO dto) {
        NursingProject nursingProject = BeanUtil.toBean(dto, NursingProject.class);
        nursingProject.setUpdateTime(new java.util.Date());
        // ... 其它字段按需赋值 ...
        int rows = nursingProjectMapper.updateById(nursingProject);
        if (rows > 0) return AjaxResult.success();
        return AjaxResult.error("修改失败");
    }

    // 删除
    @Operation(summary = "根据ID删除护理项目")
    @DeleteMapping("/{id}")
    public AjaxResult delete(@Parameter(description = "护理项目ID", required = true) @PathVariable Long id) {
        int rows = nursingProjectMapper.deleteById(id);
        if (rows > 0) return AjaxResult.success();
        return AjaxResult.error("删除失败");
    }



    //增加项目
    @Operation(summary = "增加护理项目")
    @PostMapping("")
    public AjaxResult add(@RequestBody NursingProjectAdminDTO dto) {
        NursingProject nursingProject = BeanUtil.toBean(dto, NursingProject.class);
        nursingProject.setCreateTime(new java.util.Date());
        nursingProject.setUpdateTime(new java.util.Date());
        int rows = nursingProjectMapper.insert(nursingProject);
        if (rows > 0) return AjaxResult.success();
        return AjaxResult.error("添加失败");
    }

}
