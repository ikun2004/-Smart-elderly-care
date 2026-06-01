package cn.yangeit.service;


import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.yangeit.common.JwtUtils;
import cn.yangeit.dto.UserLoginRequestDto;
import cn.yangeit.mapper.FamilyMemberMapper;
import cn.yangeit.pojo.FamilyMember;
import cn.yangeit.vo.LoginVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 老人家属Service业务层处理

 */
@Service
public class FamilyMemberService {

    @Autowired
    private FamilyMemberMapper familyMemberMapper;
    @Autowired
    private WechatService wechatService;//微信服务
    //随机昵称
    static List<String> DEFAULT_NICK_NAME = ListUtil.of(
            "生活更美好",
            "大桔大利",
            "日富一日",
            "好柿开花",
            "柿柿如意",
            "一椰暴富",
            "大柚所为",
            "杨梅吐气",
            "天生荔枝");

    /**
     * 小程序登录
     *
     * @param dto
     * @return
     */
    public LoginVo login(UserLoginRequestDto dto) {

        //1.获取openid(调用微信的接口获取) ***
        String openid = wechatService.getOpenid(dto.getCode());

        //2.根据openid查询用户
        LambdaQueryWrapper<FamilyMember> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(FamilyMember::getOpenId,openid);
        FamilyMember familyMember = familyMemberMapper.selectOne(queryWrapper);

        //3.判断用户是否存在，用户不存在，就构建用户
        if(ObjectUtil.isEmpty(familyMember)){
            familyMember = FamilyMember.builder()
                    .openId(openid)
                    .build();
        }

        //4.获取用户的手机号（调用微信的接口获取）
        String phone = wechatService.getPhoneNumber(dto.getPhoneCode());

//5. 新增或修改用户
        inserOrUpdateFamilyMember(familyMember, phone);

        //6. 把用户的信息封装到token,返回
        HashMap<String,Object> maps = new HashMap<>();
        maps.put("username",familyMember.getName());
        maps.put("userId",familyMember.getId());
        String token = JwtUtils.generateJwt(maps);

        LoginVo loginVo = new LoginVo();
        loginVo.setToken(token);
        loginVo.setNickName(familyMember.getName());

        return loginVo;
    }
    /**
     * 新增或修改用户
     * @param familyMember
     * @param phone
     */
    private void inserOrUpdateFamilyMember(FamilyMember familyMember, String phone) {

        //1. 判断新的手机跟数据库中保存的手机号是否一致
        if(ObjectUtil.notEqual(familyMember.getPhone(),phone)){
            familyMember.setPhone(phone);
        }
        //2. id是否存在，如存在，就修改
        if(ObjectUtil.isNotEmpty(familyMember.getId())){
            familyMemberMapper.updateById(familyMember);
            return; // 更新后直接返回，避免执行后续新增逻辑
        }
        //3. 不存在，就新增 (拼装用户的昵称：随机字符串+手机号后4位)
        int index = (int)(Math.random() * DEFAULT_NICK_NAME.size());
        String nickName = DEFAULT_NICK_NAME.get(index)+ phone.substring(phone.length()-4);
        familyMember.setName(nickName);
        familyMemberMapper.insert(familyMember);
    }
}


