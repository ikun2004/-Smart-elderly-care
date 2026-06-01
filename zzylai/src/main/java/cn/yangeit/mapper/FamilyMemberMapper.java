package cn.yangeit.mapper;


import cn.yangeit.pojo.FamilyMember;
import cn.yangeit.vo.FamilyMemberElderVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface FamilyMemberMapper extends BaseMapper<FamilyMember> {
    List<FamilyMemberElderVo> selectByMemberId(Long userId);
}




