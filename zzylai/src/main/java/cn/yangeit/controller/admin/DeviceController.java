package cn.yangeit.controller.admin;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONUtil;
import cn.yangeit.common.AjaxResult;
import cn.yangeit.config.BaseException;
import cn.yangeit.dto.admin.AdminPageDTO;
import cn.yangeit.dto.admin.DeviceDto;
import cn.yangeit.mapper.DeviceMapper;
import cn.yangeit.pojo.Device;
import cn.yangeit.vo.admin.ProductVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.huaweicloud.sdk.iotda.v5.IoTDAClient;
import com.huaweicloud.sdk.iotda.v5.model.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

@RestController
@Slf4j
@RequestMapping("/admin/device")
@Tag(name = "管理端-设备接口")
public class DeviceController
{

    @Autowired
    private DeviceMapper deviceMapper;
    @Autowired
    private RedisTemplate<String,String> redisTemplate;
    @Autowired
    private IoTDAClient client;

    /**
     * 验证并清理设备名称
     * @param deviceName 原始设备名称
     * @return 清理后的设备名称
     */
    private String validateAndSanitizeDeviceName(String deviceName) {
        if (StringUtils.isEmpty(deviceName)) {
            throw new BaseException("设备名称不能为空");
        }
        
        // 去除首尾空格
        deviceName = deviceName.trim();
        
        // 华为云IoT平台设备名称正则表达式验证
        String deviceNamePattern = "^[\\u4e00-\\u9fa5a-zA-Z0-9_?'#()\\.,&%@!-]*$";
        if (!deviceName.matches(deviceNamePattern)) {
            throw new BaseException("设备名称只能包含中文、英文字母、数字和特殊字符(_?'#()\\.,&%@!-)，请检查设备名称格式");
        }
        
        return deviceName;
    }
    /**
     * 查询设备列表
     */
    @PostMapping("/list")
    @Operation(summary = "查询设备列表")
    public AjaxResult list(@RequestBody AdminPageDTO dto) {
        Page<Device> page = new Page<>(dto.getPageNum(), dto.getPageSize());
        QueryWrapper<Device> wrapper = new QueryWrapper<>();
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
        Page<Device> result = deviceMapper.selectPage(page, wrapper);
        return AjaxResult.success(result);
    }
    @PostMapping("/syncProductList")
    @Operation(summary = "从物联网平台同步产品列表")
    public AjaxResult syncProductList() {
        //请求参数
        ListProductsRequest listProductsRequest = new ListProductsRequest();
        //设置条数
        listProductsRequest.setLimit(50);
        //发起请求
        ListProductsResponse response = client.listProducts(listProductsRequest);
        if(response.getHttpStatusCode() != 200){
            throw new BaseException("物联网接口 - 查询产品，同步失败");
        }
        //存储到redis
        redisTemplate.opsForValue().set("iot:all_product", JSONUtil.toJsonStr(response.getProducts()));

        return AjaxResult.success("同步成功");

    }
    @GetMapping("/allProduct")
    @Operation(summary = "查询所有产品列表")
    public AjaxResult allProduct() {
        //从redis中查询数据
        String jsonStr = redisTemplate.opsForValue().get("iot:all_product");
        //如果数据为空，则返回一个空集合
        if(StringUtils.isEmpty(jsonStr)){
            return AjaxResult.success( Collections.emptyList());
        }
        //解析数据，并返回
        List<ProductVo> productVoList = JSONUtil.toList(jsonStr, ProductVo.class);
        return AjaxResult.success(productVoList) ;
    }
    //注册设备
    @PostMapping("/register")
    @Operation(summary = "注册设备")
    public AjaxResult registerDevice(@RequestBody DeviceDto deviceDto) {

        // 验证设备名称格式是否符合华为云IoT平台要求
        String deviceName = validateAndSanitizeDeviceName(deviceDto.getDeviceName());

        //判断设备名称是否重复
        QueryWrapper<Device> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("device_name", deviceDto.getDeviceName());
        if(deviceMapper.selectCount(queryWrapper) > 0){
            throw new BaseException("设备名称已存在，请重新输入");
        }
        //检验设备标识码是否重复
        QueryWrapper<Device> queryWrapperNodeId = new QueryWrapper<>();
        queryWrapperNodeId.eq("node_id", deviceDto.getNodeId());
        if(deviceMapper.selectCount(queryWrapperNodeId) > 0){
            throw new BaseException("设备标识码已存在，请重新输入");
        }

        //校验同一位置是否绑定了同一类产品
        QueryWrapper<Device> condition = new QueryWrapper<>();
        condition.eq("product_key", deviceDto.getProductKey())
                .eq("location_type", deviceDto.getLocationType())
                .eq("physical_location_type", deviceDto.getPhysicalLocationType())
                .eq("binding_location", deviceDto.getBindingLocation());
        if (deviceMapper.selectCount(condition) > 0) {
            throw new BaseException("该老人/位置已绑定该产品，请重新选择");
        }

        //iot中新增设备
        AddDeviceRequest request = new AddDeviceRequest();
        AddDevice body = new AddDevice();
        body.withProductId(deviceDto.getProductKey());
        body.withDeviceName(deviceName);
        //设备标识码，使用uuid代替
        body.withNodeId(UUID.randomUUID().toString());
        request.withBody(body);
        AuthInfo authInfo = new AuthInfo();
        //秘钥
        String secret = UUID.randomUUID().toString().replaceAll("-", "");
        authInfo.withSecret(secret);
        body.setAuthInfo(authInfo);
        AddDeviceResponse response;
        try {
            response = client.addDevice(request);
        } catch (Exception e) {
            e.printStackTrace();
            throw new BaseException("物联网接口 - 注册设备，调用失败");
        }

        //设备数据保存到数据库
        //属性拷贝
        Device device = BeanUtil.toBean(deviceDto, Device.class);
        //设备id、设备绑定状态
        device.setIotId(response.getDeviceId());
        //秘钥
        device.setSecret(secret);

        //在数据库中新增设备
        deviceMapper.insert(device);

        return   AjaxResult.success("注册成功");
    }

    //获取设备详情
    @GetMapping("/{id}")
    @Operation(summary = "获取设备详情")
    public AjaxResult getDeviceDetail(@Parameter(description = "设备ID或物联网设备ID") @PathVariable("id") String id) {
        Device device = null;

        // 先嘗試作為數據庫ID查詢
        try {
            Long dbId = Long.valueOf(id);
            device = deviceMapper.selectById(dbId);
        } catch (NumberFormatException e) {
            // 如果不是數字，則作為IoT ID查詢
            device = deviceMapper.selectOne(new QueryWrapper<Device>().eq("iot_id", id));
        }

        if(ObjectUtil.isEmpty(device)){
            throw new BaseException("设备不存在");
        }
        return AjaxResult.success(device);
    }
    //修改设备
    @PostMapping("/edit")
    @Operation(summary = "修改设备")
    public AjaxResult edit(@RequestBody DeviceDto deviceDto){
        Device device = deviceMapper.selectById(deviceDto.getId());
        if(ObjectUtil.isEmpty(device)){
            throw new BaseException("设备不存在");
        }

        // 验证设备名称格式是否符合华为云IoT平台要求
        String deviceName = validateAndSanitizeDeviceName(deviceDto.getDeviceName());
        //修改华为云平台的信息
        UpdateDeviceRequest request = new UpdateDeviceRequest();
        request.withDeviceId(device.getIotId());
        UpdateDevice body = new UpdateDevice();
        body.withDeviceName(deviceName);
        request.withBody(body);
        try {
            client.updateDevice(request);
        } catch (Exception e) {
            log.error("物联网接口 - 修改设备，调用失败:{}", e.getMessage());
            throw new BaseException("物联网接口 - 修改设备，调用失败");
        }
        //更新数据库中设备信息
        BeanUtil.copyProperties(deviceDto, device);
        int i = deviceMapper.updateById(device);
        return  i>0?AjaxResult.success("操作成功"):AjaxResult.error("修改失败");
    }

    //删除设备
    @DeleteMapping("/{iotId}")
    @Operation(summary = "删除设备")
    public AjaxResult detele(@Parameter(description = "物联网设备id") @PathVariable("iotId") String iotId){


        DeleteDeviceRequest request = new DeleteDeviceRequest();
        request.withDeviceId(iotId);
        try {
            client.deleteDevice(request);
        } catch (Exception e) {

            throw new BaseException("物联网接口 - 删除设备，调用失败");
        }


        //从数据库删除设备
        deviceMapper.delete(new QueryWrapper<Device>().eq("iot_id", iotId));
        return AjaxResult.success();


    }



}
