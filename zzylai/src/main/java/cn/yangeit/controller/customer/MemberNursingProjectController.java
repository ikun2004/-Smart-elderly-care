package cn.yangeit.controller.customer;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.yangeit.common.AjaxResult;
import cn.yangeit.common.TableDataInfo;
import cn.yangeit.config.BaseContext;
import cn.yangeit.config.BaseException;
import cn.yangeit.dto.CheckOrderDto;
import cn.yangeit.dto.ReFundOrderDto;
import cn.yangeit.mapper.ElderMapper;
import cn.yangeit.mapper.NursingProjectMapper;
import cn.yangeit.mapper.OrderMapper;
import cn.yangeit.pojo.Elder;
import cn.yangeit.pojo.NursingProject;
import cn.yangeit.pojo.Order;
import cn.yangeit.vo.OrderVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/customer/orders")
@Tag(name = "用户端-项目和订单接口", description = "含有服务项目列表，服务详情，订单查询，订单创建，订单退款，订单详情等功能")
public class MemberNursingProjectController {
    @Autowired
    NursingProjectMapper nursingProjectMapper;
    @Autowired
    OrderMapper orderMapper;
    @Autowired
    ElderMapper elderMapper; // 注入订单表的Mapper
    @GetMapping("/project/{id}")
    @Operation(summary = "查询护理项目接口",description = "获取护理项目信息接口，返回护理项目信息")
    public AjaxResult getById(@PathVariable("id") Long id) {
        NursingProject nursingProject = nursingProjectMapper.selectById(id);
        return AjaxResult.success(nursingProject);
    }
    /**
     * 分页查询护理项目列表
     * @return 护理项目列表
     */
    @GetMapping("/project/page")
    @Operation(summary = "分页查询护理项目列表接口",description = "分页查询护理项目列表接口，返回护理项目列表")
    @Cacheable(value = "nursingProjectCache", key = "#pageNum + '::' + #pageSize")
    public TableDataInfo getByPage(@RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                   @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {
        //1. 构建分页参数
        Page<NursingProject> page = new Page<>(pageNum, pageSize);
        //2. 查询分页参数
        Page<NursingProject> projectPage = nursingProjectMapper.selectPage(page,  null);
        //3. 封装返回数据（小程序要求的）
        TableDataInfo rspData = new TableDataInfo();
        rspData.setCode(200);
        rspData.setMsg("请求成功");
        rspData.setRows(projectPage.getRecords());
        rspData.setTotal(projectPage.getTotal());
        return rspData;
    }
    @PostMapping
    @Operation(summary = "创建订单接口",description = "创建订单接口，需要提供老人id，项目id，订单备注，预计到达时间")
    public AjaxResult addOrder(@RequestBody CheckOrderDto dto) {
        Long userId = BaseContext.getCurrentId();

        Order order = new Order();
        order.setElderId(dto.getElderId());
        order.setProjectId(dto.getProjectId());
        order.setMemberId(userId);
        order.setAmount(new BigDecimal(dto.getAmount()));
        order.setCreateBy(userId);
        order.setUpdateBy(userId);
        order.setCreateTime(LocalDateTime.now());
        order.setUpdateTime(LocalDateTime.now());
        order.setStatus(1);
        order.setOrderNo(UUID.randomUUID().toString());
        order.setOCreateType(1);
        order.setRemark(dto.getRemark());
        order.setEstimatedArrivalTime(dto.getEstimatedArrivalTime());
        orderMapper.insert(order);
        return AjaxResult.success("下单成功");
    }
    @GetMapping("/order/page")
    @Operation(summary = "分页查询订单列表接口",description = "分页查询订单列表接口，返回订单列表")
    public AjaxResult listByPage(Integer pageNum, Integer pageSize, Integer status) {
        // 1. 获取当前登录用户的id
        Long userId = BaseContext.getCurrentId();
        // 2. 构建查询条件
        Page<Order> page = new Page<>(pageNum, pageSize);
        QueryWrapper<Order> wrapper = new QueryWrapper<>();
        wrapper.eq(ObjectUtil.isNotEmpty(status), "status", status);
        wrapper.eq("member_id", userId);
        // 3. 分页查询订单信息
        Page<Order> orders = orderMapper.selectPage(page, wrapper);
        // 4. 设置订单信息其他字段（需要查询其他表）
        List<OrderVo> orderVoList = orders.getRecords().stream().map(order -> {
            // 4.1 创建订单信息VO对象
            OrderVo orderVo = OrderVo.builder().build();
            // 4.2 拷贝属性
            BeanUtil.copyProperties(order, orderVo);
            // 4.3 获取护理项目名称和图片以及期望到达时间
            NursingProject nursingProject = nursingProjectMapper.selectById(order.getProjectId());
            orderVo.setServiceName(nursingProject.getName());
            orderVo.setImage(nursingProject.getImage());
            orderVo.setCreateTime(order.getEstimatedArrivalTime());
            // 老人名称
            Elder elder = elderMapper.selectById(order.getElderId());
            orderVo.setUserName(elder.getName());
            return orderVo;
        }).collect(Collectors.toList());
        return AjaxResult.success(orderVoList);
    }
    @PostMapping("/refund")
    @Operation(summary = "退款接口")
    public AjaxResult refund(@RequestBody ReFundOrderDto dto) {
        // 1. 判断订单是否存在
        Order order = orderMapper.selectById(dto.getProductOrderNo());
        if (ObjectUtil.isEmpty(order)) {
            throw new BaseException("订单不存在");
        }
        // 2. 判断订单是否是待退款状态
        if (order.getStatus() != 1) {
            throw new BaseException("订单状态错误");
        }
        // 3. 修改订单状态为待退款状态
        order.setStatus(5); // 订单状态 0待支付 1待执行 2已完成 3已关闭 4已退款 5已退款
        order.setReason(dto.getTradingChannel()); // 退款原因
        order.setRefund(order.getAmount()); // 退款金额
        order.setUpdateBy(BaseContext.getCurrentId()); // 修改人id
        order.setUpdateTime(LocalDateTime.now()); // 修改时间
        order.setIsRefund("YES"); // 是否退款
        order.setOCreateType(1); // 取消人类型 1前台 2后台

        // 4. 修改订单
        orderMapper.updateById(order);
        return AjaxResult.success();
    }
    @DeleteMapping("/{id}")
    @Operation(summary = "删除订单接口")
    public AjaxResult deleteById(@PathVariable("id") Long id) {
        // 1. 判断订单是否存在
        Order order = orderMapper.selectById(id);
        if (ObjectUtil.isEmpty(order)) {
            throw new BaseException("订单不存在");
        }
        // 2. 删除订单
        orderMapper.deleteById(id);
        return AjaxResult.success();
    }
}
