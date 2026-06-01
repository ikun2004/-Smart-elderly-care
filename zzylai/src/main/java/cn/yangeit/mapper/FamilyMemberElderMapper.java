package cn.yangeit.mapper;


import cn.yangeit.pojo.FamilyMemberElder;
import cn.yangeit.vo.FamilyMemberElderVo;
import cn.yangeit.vo.MemberElderVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface FamilyMemberElderMapper extends BaseMapper<FamilyMemberElder> {


    List<MemberElderVo> listByPage(Long userId);

    List<FamilyMemberElderVo> selectByMemberId(Long valueOf);
}




