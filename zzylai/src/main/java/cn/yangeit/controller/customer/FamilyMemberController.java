package cn.yangeit.controller.customer;

import cn.hutool.core.util.ObjectUtil;
import cn.yangeit.config.BaseContext;
import cn.yangeit.config.BaseException;
import cn.yangeit.vo.FamilyMemberElderVo;
import cn.yangeit.vo.MemberElderVo;
import io.jsonwebtoken.Claims;
import cn.yangeit.common.AjaxResult;
import cn.yangeit.common.JwtUtils;
import cn.yangeit.dto.MemberElderDto;
import cn.yangeit.dto.UserLoginRequestDto;
import cn.yangeit.mapper.ElderMapper;
import cn.yangeit.mapper.FamilyMemberElderMapper;
import cn.yangeit.pojo.Elder;
import cn.yangeit.pojo.FamilyMemberElder;
import cn.yangeit.service.FamilyMemberService;
import cn.yangeit.vo.LoginVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * 微信端的用户相关接口
 */
@RestController
@RequestMapping("/customer/user")
@Tag(name = "用户端-用户接口", description = "含有登录，绑定家人，解除绑定，家人列表等功能")
public class FamilyMemberController {

    @Autowired
    private FamilyMemberService familyMemberService;
    @Autowired
    ElderMapper elderMapper;
    @Autowired
    FamilyMemberElderMapper familyMemberElderMapper;

    @PostMapping("/login")
    @Operation(summary = "登录接口",description = "微信小程序登录的接口，需要提供授权码和手机号码授权码等信息，返回token令牌")
    public AjaxResult login(@RequestBody UserLoginRequestDto dto) {
        LoginVo loginVo = familyMemberService.login(dto);
        return AjaxResult.success(loginVo);
    }

    // 绑定家人
    @PostMapping("/add")
    @Operation(summary = "绑定家人接口",description = "绑定家人接口，需要提供家人信息，返回成功或者失败")
    public AjaxResult add(@RequestBody MemberElderDto memberElderDto) {
        // 根据身份证号查询老人
        QueryWrapper<Elder> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id_card_no", memberElderDto.getIdCard());
        Elder elder = elderMapper.selectOne(queryWrapper);

        // 判断老人是否存在
        if (ObjectUtil.isEmpty(elder)) {

            throw new BaseException("该老人未入住，请检查输入信息");
        }

        // 获取当前登录人的 userId
        Long userId = BaseContext.getCurrentId();

        // 判断是否已经绑定
        QueryWrapper<FamilyMemberElder> queryWrapper2 = new QueryWrapper<>();
        queryWrapper2.eq("elder_id", elder.getId());
        queryWrapper2.eq("family_member_id", userId);
        long count = familyMemberElderMapper.selectCount(queryWrapper2);

        if (count > 0) {
            throw new BaseException("该老人已绑定，请勿重复绑定");
        }

        // 保存家属与老人的关系
        FamilyMemberElder familyMemberElder = new FamilyMemberElder();
        familyMemberElder.setElderId(elder.getId());
        familyMemberElder.setFamilyMemberId(userId);
        familyMemberElder.setCreateTime(new Date());
        familyMemberElderMapper.insert(familyMemberElder);
        //返回成功
        return AjaxResult.success();
    }
    //获得家人列表
    @GetMapping("/list-by-page")
    @Operation(summary = "获得家人列表接口",description = "获得家人列表接口，返回家人列表")
    public AjaxResult listByPage(@Parameter(description = "当前页码", required = true, example = "1") Integer pageNum,
                                 @Parameter(description = "每页条数", required = true, example = "10") Integer pageSize) {
        //1.从请求头中获取当前userId
        Long userId = BaseContext.getCurrentId();;
        //2查询List<MemberElderVo>集合
        List<MemberElderVo> memberElders = familyMemberElderMapper.listByPage(userId);
        //补点假数据，后期来真的数据
        memberElders.forEach(memberElder -> {
            memberElder.setTypeName("豪华单人间");
            memberElder.setIotId("dHMuAjTk1D9scHXtmpvrk1bok0");
        });

        //3.返回数据
        return AjaxResult.success(memberElders);
    }
    //解除绑定
    @DeleteMapping("/deleteById")
    @Operation(summary = "解除绑定接口",description = "解除绑定接口，需要提供家人id，返回成功或者失败")
    public AjaxResult deleteById(@Parameter(description = "关系表id", required = true, example = "17") Long id) {
        int result = familyMemberElderMapper.deleteById(id);
        //判断是否删除成功 result是影响行数
        return result>0?AjaxResult.success():AjaxResult.error();

    }
    //我的家人列表
    @GetMapping("/my")
    @Operation(summary = "我的家人列表接口",description = "这个接口用来下单，探访预约时选择家人使用")
    public AjaxResult  my(HttpServletRequest request) {
        //获得用户ID
      Long userId = BaseContext.getCurrentId();
        //查询
        if (userId == 0){
            return AjaxResult.error("请先登录");
        }
        List<FamilyMemberElderVo> memberElders = familyMemberElderMapper.selectByMemberId(Long.valueOf(userId));

        return AjaxResult.success(memberElders);
    }



}