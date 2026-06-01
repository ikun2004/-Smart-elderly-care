package cn.yangeit.vo.admin;

import cn.yangeit.pojo.Elder;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class FamilyMemberAdminVo {
    private Long id;
    private String phone;
    private String name;
    private String avatar;
    private String openId;
    private Integer gender;
    private Date createTime;
    private Date updateTime;
    private Long createBy;
    private Long updateBy;
    private String remark;
    private List<Elder> elderList;
}