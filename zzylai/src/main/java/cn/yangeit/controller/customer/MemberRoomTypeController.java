package cn.yangeit.controller.customer;


import cn.hutool.core.util.ObjectUtil;
import cn.yangeit.common.AjaxResult;
import cn.yangeit.mapper.RoomTypeMapper;
import cn.yangeit.pojo.RoomType;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.jsonwebtoken.Claims;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 小程序端，房型相关接口
 */
@RestController
@RequestMapping("/customer/roomTypes")
@Tag(name = "用户端-房型接口", description = "获取房型列表")
public class MemberRoomTypeController  {
    @Autowired
    RoomTypeMapper roomTypeMapper;

        // 获取房型列表
        @GetMapping
        @Cacheable(value = "roomType",key = "#status")//key   roomType:1:有用的房型列表，roomType:0:无用的房型列表
        @Operation(summary = "获取房型列表")
        public AjaxResult findRoomTypeListByStatus(@Parameter  (name = "状态") Integer status) {

        //1. 判断参数是否为空或者null
        if(ObjectUtil.isEmpty(status)){
            return AjaxResult.error("状态为空");
        }
        //2. 创建查询条件
        QueryWrapper<RoomType> wrapper = new QueryWrapper<>();
        wrapper.eq("status",status);
        //3. 查询
        List<RoomType> roomTypes = roomTypeMapper.selectList(wrapper);
        //4. 返回结果
        return AjaxResult.success(roomTypes);
    }
}
