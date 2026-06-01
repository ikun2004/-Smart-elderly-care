package cn.yangeit.controller.admin;

import cn.yangeit.common.AjaxResult;
import cn.yangeit.mapper.*;
import cn.yangeit.pojo.Order;
import cn.yangeit.pojo.Reservation;
import cn.yangeit.vo.admin.AdminDataVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/admin/home")
@CrossOrigin
@Tag(name = "管理端-首页数据" ,description = "首页显示数据")
public class AdminDataController {
    @Autowired
    ElderMapper elderMapper;

    @Autowired
    FamilyMemberMapper  familyMemberMapper;


    @Autowired
    OrderMapper orderMapper;

    @Autowired
    NursingProjectMapper nursingProjectMapper;


    @Autowired
    RoomTypeMapper roomMapper;


    @Autowired
    ReservationMapper reservationMapper;


    //获取首页数据
    @GetMapping("/data")
    @Schema(description = "获取首页数据")
    public AjaxResult getData() {
        AdminDataVo vo = new AdminDataVo();
        //用户数量
        vo.setUserCount(familyMemberMapper.selectCount(null).intValue());
        //老人数量
        vo.setElderCount(elderMapper.selectCount(null).intValue());
        //护理服务数量
        vo.setProjectCount(nursingProjectMapper.selectCount(null).intValue());
        //房型数量
        vo.setRoomCount(roomMapper.selectCount(null).intValue());
        //今日预约数量
        LocalDateTime startTime = LocalDateTime.now().toLocalDate().atStartOfDay();
        LocalDateTime endTime = startTime.plusHours(24);
        QueryWrapper<Reservation> queryWrapper = new QueryWrapper<Reservation>().eq("status", 1).between("time", startTime, endTime);
        vo.setReservationCount(reservationMapper.selectCount(queryWrapper).intValue());
        //今日订单数量
        QueryWrapper<Order> queryWrapper1 = new QueryWrapper<Order>().between("create_time", startTime, endTime);
        vo.setOrderCount(orderMapper.selectCount(queryWrapper1).intValue());
        return AjaxResult.success(vo);
    }





}
