package cn.yangeit.controller.admin;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.yangeit.common.AjaxResult;
import cn.yangeit.dto.admin.AdminPageDTO;
import cn.yangeit.dto.admin.RoomTypeAdminDTO;
import cn.yangeit.mapper.RoomTypeMapper;
import cn.yangeit.pojo.RoomType;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Date;


@RestController
@RequestMapping("/admin/roomTypes")
@Tag(name = "管理端房型", description = "房型相关接口")
public class RoomTypeAdminController {
    @Autowired
    RoomTypeMapper roomTypeMapper;

    // 分页查询
    @Operation(summary = "分页查询房型列表")
    @PostMapping("/page")
    public AjaxResult page(@RequestBody AdminPageDTO dto) {
        Page<RoomType> page = new Page<>(dto.getPageNum(), dto.getPageSize());
        QueryWrapper<RoomType> wrapper = new QueryWrapper<>();
        wrapper.eq(ObjectUtil.isNotEmpty(dto.getStatus()), "status", dto.getStatus())
                .like(ObjectUtil.isNotEmpty(dto.getSearchKey()), "name", dto.getSearchKey());
        //需要排序，按照创建时间的倒叙
        wrapper.orderByDesc("create_time");

        Page<RoomType> result = roomTypeMapper.selectPage(page, wrapper);
        return AjaxResult.success(result);
    }

    // 详情
    @GetMapping("/{id}")
    public AjaxResult detail(@PathVariable Long id) {
        RoomType roomType = roomTypeMapper.selectById(id);
        if (roomType == null) return AjaxResult.error("未找到该房型");
        return AjaxResult.success(roomType);
    }

    // 修改
    @Operation(summary = "修改房型信息")
    @PutMapping("")
    public AjaxResult update(@RequestBody RoomTypeAdminDTO dto) {
        RoomType roomType = BeanUtil.toBean(dto, RoomType.class);
        roomType.setUpdateTime(new Date());
        int rows = roomTypeMapper.updateById(roomType);
        if (rows > 0) return AjaxResult.success();
        return AjaxResult.error("修改失败");
    }

    // 删除
    @DeleteMapping("/{id}")
    public AjaxResult delete(@PathVariable Long id) {
        int rows = roomTypeMapper.deleteById(id);
        if (rows > 0) return AjaxResult.success();
        return AjaxResult.error("删除失败");
    }
    //增加房型
    @PostMapping("")
    public AjaxResult add(@RequestBody RoomTypeAdminDTO dto) {
        RoomType roomType = BeanUtil.toBean(dto, RoomType.class);
        roomType.setCreateTime(new Date());
        roomType.setUpdateTime(new Date());
        int rows = roomTypeMapper.insert(roomType);
        if (rows > 0) return AjaxResult.success();
        return AjaxResult.error("添加失败");
    }
}
