package cn.yangeit.controller.admin;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.yangeit.dto.admin.AdminPageDTO;
import cn.yangeit.dto.admin.OrderAdminDTO;
import cn.yangeit.mapper.ElderMapper;
import cn.yangeit.mapper.NursingProjectMapper;
import cn.yangeit.mapper.OrderMapper;
import cn.yangeit.pojo.Elder;
import cn.yangeit.pojo.NursingProject;
import cn.yangeit.vo.OrderVo;
import cn.yangeit.vo.admin.FamilyMemberAdminVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.*;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import cn.yangeit.pojo.Order;
import cn.yangeit.common.AjaxResult;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/admin/orders")
@Tag(name = "管理端-订单管理列表" ,description = "管理端-订单管理列表")
public class OrderAdminController {
    @Autowired
    OrderMapper orderMapper;
    @Autowired
    NursingProjectMapper nursingProjectMapper;
    @Autowired
    ElderMapper elderMapper;
    // 分页查询
    @Operation(summary = "分页查询订单列表")
    @PostMapping("/page")
    public AjaxResult page(@RequestBody AdminPageDTO dto) {
        Page<Order> page = new Page<>(dto.getPageNum(), dto.getPageSize());
        QueryWrapper<Order> wrapper = new QueryWrapper<>();
        wrapper.eq(ObjectUtil.isNotEmpty(dto.getStatus()), "status", dto.getStatus())
                .like(ObjectUtil.isNotEmpty(dto.getSearchKey()), "order_no", dto.getSearchKey());
        Page<Order> result = orderMapper.selectPage(page, wrapper);


        Page<OrderVo> resultVo = new Page<>();
        BeanUtil.copyProperties(result, resultVo,  "records");

        List<OrderVo> orderVoList = result.getRecords().stream().map(item -> {
            OrderVo orderVo = BeanUtil.toBean(item, OrderVo.class);
            Elder elder = elderMapper.selectById(item.getElderId());
            if (elder != null){
                orderVo.setUserName(elder.getName());
            }
            NursingProject nursingProject = nursingProjectMapper.selectById(item.getProjectId());
            if (nursingProject != null){
                orderVo.setServiceName(nursingProject.getName());
                orderVo.setImage(nursingProject.getImage());
            }
            return orderVo;

        }).collect(Collectors.toList());
        resultVo.setRecords(orderVoList);

        return AjaxResult.success(resultVo);
    }

    // 详情
    @Operation(summary = "根据ID查询订单详情")
    @GetMapping("/{id}")
    public AjaxResult detail(@Parameter(description = "订单ID", required = true) @PathVariable Long id) {
        Order order = orderMapper.selectById(id);
        if (order == null) return AjaxResult.error("未找到该订单");

        OrderVo orderVo = BeanUtil.toBean(order, OrderVo.class);
        Elder elder = elderMapper.selectById(order.getElderId());
        if (elder != null){
            orderVo.setUserName(elder.getName());
        }
        NursingProject nursingProject = nursingProjectMapper.selectById(order.getProjectId());
        if (nursingProject != null){
            orderVo.setServiceName(nursingProject.getName());
            orderVo.setImage(nursingProject.getImage());
        }
        return AjaxResult.success(orderVo);
    }

    // 修改
    @Operation(summary = "修改订单信息")
    @PutMapping("")
    public AjaxResult update(@RequestBody OrderAdminDTO dto) {
        Order order = BeanUtil.toBean(dto, Order.class);
        order.setUpdateTime(LocalDateTime.now());
        // ... 其它字段按需赋值 ...
        int rows = orderMapper.updateById(order);
        if (rows > 0) return AjaxResult.success();
        return AjaxResult.error("修改失败");
    }

    // 删除
    @Operation(summary = "根据ID删除订单")
    @DeleteMapping("/{id}")
    public AjaxResult delete(@Parameter(description = "订单ID", required = true) @PathVariable Long id) {
        int rows = orderMapper.deleteById(id);
        if (rows > 0) return AjaxResult.success();
        return AjaxResult.error("删除失败");
    }
}
