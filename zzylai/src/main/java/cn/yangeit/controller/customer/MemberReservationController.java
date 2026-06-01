package cn.yangeit.controller.customer;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.yangeit.common.AjaxResult;
import cn.yangeit.common.JwtUtils;
import cn.yangeit.common.TableDataInfo;
import cn.yangeit.config.BaseContext;
import cn.yangeit.config.BaseException;
import cn.yangeit.dto.ReservationDto;
import cn.yangeit.mapper.ReservationMapper;
import cn.yangeit.pojo.Reservation;
import cn.yangeit.vo.TimeCountVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.jsonwebtoken.Claims;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 预约信息Controller
 */
@RestController
@RequestMapping("/customer/reservation")
@Tag(name = "用户端-预约模块", description = "含有 查询取消预约次数，预约，预约列表，取消预约等功能")
public class MemberReservationController {

    @Autowired
    ReservationMapper reservationMapper;
//查询取消预约的数量
@GetMapping("/cancelled-count")
@Operation(summary = "查询取消预约次数接口",description = "查询取消预约次数接口，返回取消预约次数")
public AjaxResult CancelledCount() {
    Long userId =BaseContext.getCurrentId();

    //查询当前用户预约数量
    QueryWrapper<Reservation> queryWrapper = new QueryWrapper<Reservation>();
    queryWrapper.eq("create_by", userId);
    // 查询当前用户取消的预约数量
    queryWrapper.eq("status", 2);
    Long count = reservationMapper.selectCount(queryWrapper);
    //  返回结果
    return AjaxResult.success(count);
}
//查询每个时间段剩余预约次数
@GetMapping("/countByTime")
@Operation(summary = "查询每个时间段剩余预约次数接口",description = "查询每个时间段剩余预约次数接口，返回每个时间段剩余预约次数")
public AjaxResult countReservationsForEachTimeWithinTimeRange(@Parameter(description = "时间戳", required = true, example = "1680000000000") Long time) {
    //将数字转换为日期
    LocalDateTime localDateTime = LocalDateTimeUtil.of(time);
    //获取每天所有时间段的次数
    LocalDateTime startTime = localDateTime.toLocalDate().atStartOfDay();
    //统计最早/晚时间段之间的次数
    LocalDateTime endTime = startTime.plusHours(24);
    //查询起止时间段内，预约的剩余次数
    List<TimeCountVo> timeCountVoList = reservationMapper.countReservationsForTime(startTime, endTime);

    return AjaxResult.success(timeCountVoList);
}
    /**
     * 新增预约信息
     */
    @PostMapping()
    @Operation(summary = "新增预约接口",description = "新增预约接口，返回成功或者失败")
    public AjaxResult addReservation(@RequestBody ReservationDto reservationDto) {
        Long userId = BaseContext.getCurrentId();
        // 2. 创建预约信息
        Reservation reservation = BeanUtil.toBean(reservationDto, Reservation.class);
        reservation.setCreateBy(Long.valueOf(userId));
        reservation.setUpdateBy(Long.valueOf(userId));
        reservation.setCreateTime(LocalDateTime.now());
        reservation.setStatus(0);
        // 3. 保存预约信息
        Boolean result = reservationMapper.insert(reservation) > 0;
        // 4. 返回结果
        return result ? AjaxResult.success() : AjaxResult.error();
    }
    /**
     * 分页查询增加预约人的姓名，手机号，状态，类型的查询条件
     */
    @GetMapping("/page")
    @Operation(summary = "分页查询预约信息接口",description = "分页查询预约信息接口，返回预约信息列表")
    public AjaxResult findByPage(Integer pageNum,
                                 Integer pageSize,
                                 Integer status
                                 ) {
        System.out.println("控制器 当前线程ID: "+Thread.currentThread().getId());
        Long userId = BaseContext.getCurrentId();
        // 2. 分页查询预约信息
        Page<Reservation> page = new Page<>(pageNum, pageSize);
        QueryWrapper<Reservation> wrapper = new QueryWrapper<>();
        wrapper.eq("create_by", userId);
        wrapper.eq(ObjectUtil.isNotEmpty(status), "status", status);
        wrapper.orderByDesc("create_time");

        List list = reservationMapper.selectList(page, wrapper);

        // 3. 封装返回数据(小程序要求的)
        TableDataInfo rspData = new TableDataInfo();
        rspData.setCode(200);
        rspData.setMsg("请求成功");
        rspData.setRows(list);
        rspData.setTotal(page.getTotal());
        // 4. 返回结果
        return AjaxResult.success(rspData);
    }
    @PutMapping("/{id}/cancel")
    @Operation(summary = "取消预约接口",description = "取消预约接口，返回成功或者失败")
    public AjaxResult cancel(@PathVariable Long id) {
        Long userId =BaseContext.getCurrentId();
        // 2. 先查询预约信息是否存在
        Reservation reservation = reservationMapper.selectById(id);
        if (ObjectUtil.isEmpty(reservation)) {
           throw new BaseException("预约信息不存在");
        }
        // 3. 将状态设置为取消和修改者
        reservation.setStatus(2);
        reservation.setUpdateBy(userId);
        reservationMapper.updateById(reservation);
        return AjaxResult.success();
    }
}

