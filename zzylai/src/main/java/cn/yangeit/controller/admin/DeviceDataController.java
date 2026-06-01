package cn.yangeit.controller.admin;


import cn.hutool.core.util.ObjectUtil;
import cn.yangeit.common.AjaxResult;
import cn.yangeit.dto.admin.AdminPageDTO;
import cn.yangeit.mapper.DeviceDataMapper;
import cn.yangeit.pojo.DeviceData;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/devicedata")
@Tag(name = "管理端-设备数据的接口")
public class DeviceDataController
{
    @Autowired
    private DeviceDataMapper deviceDataMapper;

    /**
     * 查询设备数据列表
     */
    @Operation(summary = "查询设备数据列表")
    @PostMapping("/list")
    public AjaxResult list(@RequestBody AdminPageDTO dto)
    {
        Page<DeviceData> page = new Page<>(dto.getPageNum(), dto.getPageSize());
        QueryWrapper<DeviceData> wrapper = new QueryWrapper<>();
        // 模糊匹配 name id_card_no phone
        wrapper.like(ObjectUtil.isNotEmpty(dto.getSearchKey()), "device_name", dto.getSearchKey())
                .or()
                .like(ObjectUtil.isNotEmpty(dto.getSearchKey()), "nickname", dto.getSearchKey())
                .or()
                .like(ObjectUtil.isNotEmpty(dto.getSearchKey()), "product_key", dto.getSearchKey())
                .or()
                .like(ObjectUtil.isNotEmpty(dto.getSearchKey()), "product_name", dto.getSearchKey())
                .or()
                .like(ObjectUtil.isNotEmpty(dto.getSearchKey()), "device_description", dto.getSearchKey())
                .or()
                .like(ObjectUtil.isNotEmpty(dto.getSearchKey()), "iot_id", dto.getSearchKey());
        wrapper.orderByDesc("create_time");
        Page<DeviceData> result = deviceDataMapper.selectPage(page, wrapper);
        return AjaxResult.success(result);
    }



    /**
     * 获取设备数据详细信息
     */
    @Operation(summary = "获取设备数据详细信息")
    @GetMapping(value = "/{iot_id}")
    public AjaxResult getInfo(@Parameter(description = "设备数据ID", required = true)
                              @PathVariable("iot_id") String iot_id)
    {
        QueryWrapper<DeviceData> wrapper = new QueryWrapper<>();
        wrapper.eq("iot_id", iot_id);
        wrapper.orderByDesc("create_time");
        List<DeviceData> deviceData = deviceDataMapper.selectList(wrapper);


        return  AjaxResult.success(deviceData);

    }




    /**
     * 删除设备数据
     */
    @Operation(summary = "删除设备数据")
    @DeleteMapping("/{iot_id}")
    public AjaxResult remove(@Parameter(description = "设备数据ID", required = true)@PathVariable String iot_id)
    {
        QueryWrapper<DeviceData> wrapper = new QueryWrapper<>();
        wrapper.eq("iot_id", iot_id);
        int rows = deviceDataMapper.delete(wrapper);
        if (rows > 0) return AjaxResult.success();
        return AjaxResult.error("删除失败");

    }
}
