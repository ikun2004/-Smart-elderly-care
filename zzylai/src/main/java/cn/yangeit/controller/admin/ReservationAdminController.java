package cn.yangeit.controller.admin;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.yangeit.common.AjaxResult;
import cn.yangeit.dto.admin.AdminPageDTO;
import cn.yangeit.dto.admin.ReservationAdminDTO;
import cn.yangeit.mapper.ReservationMapper;
import cn.yangeit.pojo.Reservation;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;


@RestController
@RequestMapping("/admin/reservation")
@Tag(name = "管理端-预约模块", description = "用来管理预约模块")
public class ReservationAdminController {

    @Autowired
    ReservationMapper reservationMapper;

    // 分页查询
    @Operation(summary = "分页查询预约列表")
    @PostMapping("/page")
    public AjaxResult page(@RequestBody AdminPageDTO dto) {
        Page<Reservation> page = new Page<>(dto.getPageNum(), dto.getPageSize());
        QueryWrapper<Reservation> wrapper = new QueryWrapper<>();
        wrapper.eq(ObjectUtil.isNotEmpty(dto.getStatus()), "status", dto.getStatus())
                .like(ObjectUtil.isNotEmpty(dto.getSearchKey()), "name", dto.getSearchKey());
        Page<Reservation> result = reservationMapper.selectPage(page, wrapper);
        return AjaxResult.success(result);
    }

    // 详情
    @GetMapping("/{id}")
    @Operation(summary = "根据ID查询预约详情")
    public AjaxResult detail(@Parameter(description = "预约ID", required = true) @PathVariable Long id) {
        Reservation reservation = reservationMapper.selectById(id);
        if (reservation == null) return AjaxResult.error("未找到该预约");
        return AjaxResult.success(reservation);
    }

    // 修改
    @Operation(summary = "修改预约信息")
    @PutMapping("")
    public AjaxResult update(@RequestBody ReservationAdminDTO dto) {
        Reservation reservation = BeanUtil.toBean(dto, Reservation.class);
        reservation.setUpdateTime(LocalDateTime.now());
        int rows = reservationMapper.updateById(reservation);
        if (rows > 0) return AjaxResult.success();
        return AjaxResult.error("修改失败");
    }

    // 删除
    @DeleteMapping("/{id}")
    @Operation(summary = "根据ID删除预约")
    public AjaxResult delete(@Parameter(description = "预约ID", required = true) @PathVariable Long id) {
        int rows = reservationMapper.deleteById(id);
        if (rows > 0) return AjaxResult.success();
        return AjaxResult.error("删除失败");
    }
}