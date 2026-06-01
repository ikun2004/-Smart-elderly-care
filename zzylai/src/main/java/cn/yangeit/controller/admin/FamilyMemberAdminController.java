package cn.yangeit.controller.admin;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import cn.yangeit.config.BaseException;
import cn.yangeit.dto.admin.AdminPageDTO;
import cn.yangeit.dto.admin.FamilyMemberAdminDTO;
import cn.yangeit.dto.admin.MemberElderBindDto;
import cn.yangeit.mapper.ElderMapper;
import cn.yangeit.mapper.FamilyMemberElderMapper;
import cn.yangeit.mapper.FamilyMemberMapper;
import cn.yangeit.pojo.Elder;
import cn.yangeit.pojo.FamilyMember;
import cn.yangeit.pojo.FamilyMemberElder;
import cn.yangeit.vo.FamilyMemberElderVo;
import cn.yangeit.vo.admin.FamilyMemberAdminVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import cn.yangeit.common.AjaxResult;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/admin/user")
@Tag(name = "管理端-用户管理模块", description = "用户相关接口，提供用户管理，禁用，启动，修改名字，分页查询等功能")
public class FamilyMemberAdminController {
    @Autowired
    ElderMapper elderMapper;
    @Autowired
    FamilyMemberMapper familyMemberMapper;
    @Autowired
    FamilyMemberElderMapper familyMemberElderMapper;

    // 分页查询
    @Operation(summary = "分页查询用户列表")
    @PostMapping("/page")
    public AjaxResult page(@RequestBody AdminPageDTO dto) {
        Page<FamilyMember> page = new Page<>(dto.getPageNum(), dto.getPageSize());
        QueryWrapper<FamilyMember> wrapper = new QueryWrapper<>();

        //构建条件查询器，status状态和搜索关键词（name,phone,openId)这个三个关键词是or的关系
        wrapper.like(StrUtil.isNotBlank(dto.getSearchKey()), "name", dto.getSearchKey())
                .or()
                .like(StrUtil.isNotBlank(dto.getSearchKey()), "phone", dto.getSearchKey())
                .or()
                .like(StrUtil.isNotBlank(dto.getSearchKey()), "openId", dto.getSearchKey());

        Page<FamilyMember> result = familyMemberMapper.selectPage(page, wrapper);

        //将 Page<FamilyMember>转成  Page<FamilyMemberAdminVo>
        Page<FamilyMemberAdminVo> resultVo = new Page<>();
        BeanUtil.copyProperties(result, resultVo);
        List<FamilyMemberAdminVo> familyMemberAdminVos =result.getRecords().stream().map(item -> {
                    FamilyMemberAdminVo adminVo = BeanUtil.toBean(item, FamilyMemberAdminVo.class);
                    //通过memberId查询elderId列表
                    List<FamilyMemberElderVo> familyMemberElderVos = familyMemberElderMapper.selectByMemberId(item.getId());
                    if (familyMemberElderVos != null && familyMemberElderVos.size() > 0){
                        List<Elder> elders = familyMemberElderVos.stream().map(elder -> {
                            Elder elder1 = new Elder();
                            elder1.setId(elder.getElderId());
                            elder1.setName(elder.getElderName());
                            return elder1;
                        }).collect(Collectors.toList());
                        adminVo.setElderList(elders);
                    }
                    return adminVo;
                }).collect(Collectors.toList());

        resultVo.setRecords(familyMemberAdminVos);

        return AjaxResult.success(resultVo);
    }

    // 详情
    @Operation(summary = "根据ID查询家庭成员详情")
    @GetMapping("/{id}")
    public AjaxResult detail(@Parameter(description = "家庭成员ID", required = true) @PathVariable Long id) {
        FamilyMember member = familyMemberMapper.selectById(id);
        if (member == null) return AjaxResult.error("未找到该家庭成员");
        return AjaxResult.success(member);
    }

    // 修改
    @Operation(summary = "修改家庭成员信息")
    @PutMapping("")
    public AjaxResult update(@RequestBody FamilyMemberAdminDTO dto) {
        FamilyMember member = BeanUtil.toBean(dto, FamilyMember.class);
        member.setUpdateTime(new Date());
        int rows = familyMemberMapper.updateById(member);
        if (rows > 0) return AjaxResult.success();
        return AjaxResult.error("修改失败");
    }

    // 删除
    @Operation(summary = "根据ID删除家庭成员")
    @DeleteMapping("/{id}")
    public AjaxResult delete(@Parameter(description = "家庭成员ID", required = true) @PathVariable Long id) {
        //先判断是否存在
        FamilyMember member = familyMemberMapper.selectById(id);
        if (member == null) {
            throw new BaseException("未找到该用户，无法删除");
        }

        //删除关系
        QueryWrapper<FamilyMemberElder> familyMemberElderQueryWrapper = new QueryWrapper<>();
        familyMemberElderQueryWrapper.eq("member_id", id);
        familyMemberElderMapper.delete(familyMemberElderQueryWrapper);


        int rows = familyMemberMapper.deleteById(id);
        if (rows > 0) return AjaxResult.success();
        return AjaxResult.error("删除失败");
    }


    //绑定家人
    @PostMapping("/add")
    @Operation(summary = "绑定家人")
    public AjaxResult addElder(@RequestBody MemberElderBindDto memberElderBindDto ){
        //4.通过userId和老人id查询是否已经绑定
        QueryWrapper<FamilyMemberElder> wrapper2 = new QueryWrapper<>();
        wrapper2.eq("elder_id",memberElderBindDto.getElderId());
        wrapper2.eq("family_member_id",memberElderBindDto.getMemberId());
        Long count = familyMemberElderMapper.selectCount(wrapper2);
        if (count>0){
            throw new BaseException("该老人已经绑定了，请勿重复绑定！");
        }
        //5.如果没有绑定，那么就绑定 插入数据
        FamilyMemberElder familyMemberElder = new FamilyMemberElder();
        familyMemberElder.setRemark(memberElderBindDto.getRemark());
        familyMemberElder.setElderId(memberElderBindDto.getElderId());
        familyMemberElder.setFamilyMemberId(memberElderBindDto.getMemberId());
        familyMemberElder.setCreateTime(new Date());
        familyMemberElderMapper.insert(familyMemberElder);
        //6. 返回成功
        return AjaxResult.success("绑定成功");
    }

//    解绑老人
    @PostMapping("/unbing")
    @Operation(summary = "解绑老人")
    public AjaxResult unbing(@RequestBody MemberElderBindDto memberElderBindDto ){
        //4.通过userId和老人id查询是否已经绑定
        QueryWrapper<FamilyMemberElder> wrapper2 = new QueryWrapper<>();
        wrapper2.eq("elder_id",memberElderBindDto.getElderId());
        wrapper2.eq("family_member_id",memberElderBindDto.getMemberId());
        Long count = familyMemberElderMapper.selectCount(wrapper2);
        if (count<=0){
            throw new BaseException("该老人没有绑定，请勿重复解绑！");
        }
        //5.如果已经绑定，那么就解绑
        familyMemberElderMapper.delete(wrapper2);
        return AjaxResult.success("解绑成功");
    }
}

