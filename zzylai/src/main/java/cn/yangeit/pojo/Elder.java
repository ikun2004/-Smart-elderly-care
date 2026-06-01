package cn.yangeit.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.util.Date;

/**
 * 老人表
 * @TableName elder
 */
@TableName(value ="elder")
public class Elder {
    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 名称
     */
    private String name;

    /**
     * 图片
     */
    private String image;

    /**
     * 身份证号
     */
    private String idCardNo;

    /**
     * 性别（0:女  1:男）
     */
    private Integer sex;

    /**
     * 状态（0：禁用，1:启用  2:请假 3:退住中 4入住中 5已退住）
     */
    private Integer status;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 出生日期
     */
    private String birthday;

    /**
     * 家庭住址
     */
    private String address;

    /**
     * 身份证国徽面
     */
    private String idCardNationalEmblemImg;

    /**
     * 身份证人像面
     */
    private String idCardPortraitImg;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 创建人id
     */
    private Long createBy;

    /**
     * 更新人id
     */
    private Long updateBy;

    /**
     * 备注
     */
    private String remark;

    /**
     * 床位编号
     */
    private String bedNumber;

    /**
     * 床位id
     */
    private Long bedId;

    /**
     * id
     */
    public Long getId() {
        return id;
    }

    /**
     * id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 名称
     */
    public String getName() {
        return name;
    }

    /**
     * 名称
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 图片
     */
    public String getImage() {
        return image;
    }

    /**
     * 图片
     */
    public void setImage(String image) {
        this.image = image;
    }

    /**
     * 身份证号
     */
    public String getIdCardNo() {
        return idCardNo;
    }

    /**
     * 身份证号
     */
    public void setIdCardNo(String idCardNo) {
        this.idCardNo = idCardNo;
    }

    /**
     * 性别（0:女  1:男）
     */
    public Integer getSex() {
        return sex;
    }

    /**
     * 性别（0:女  1:男）
     */
    public void setSex(Integer sex) {
        this.sex = sex;
    }

    /**
     * 状态（0：禁用，1:启用  2:请假 3:退住中 4入住中 5已退住）
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * 状态（0：禁用，1:启用  2:请假 3:退住中 4入住中 5已退住）
     */
    public void setStatus(Integer status) {
        this.status = status;
    }

    /**
     * 手机号
     */
    public String getPhone() {
        return phone;
    }

    /**
     * 手机号
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * 出生日期
     */
    public String getBirthday() {
        return birthday;
    }

    /**
     * 出生日期
     */
    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    /**
     * 家庭住址
     */
    public String getAddress() {
        return address;
    }

    /**
     * 家庭住址
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * 身份证国徽面
     */
    public String getIdCardNationalEmblemImg() {
        return idCardNationalEmblemImg;
    }

    /**
     * 身份证国徽面
     */
    public void setIdCardNationalEmblemImg(String idCardNationalEmblemImg) {
        this.idCardNationalEmblemImg = idCardNationalEmblemImg;
    }

    /**
     * 身份证人像面
     */
    public String getIdCardPortraitImg() {
        return idCardPortraitImg;
    }

    /**
     * 身份证人像面
     */
    public void setIdCardPortraitImg(String idCardPortraitImg) {
        this.idCardPortraitImg = idCardPortraitImg;
    }

    /**
     * 创建时间
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * 创建时间
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * 更新时间
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * 更新时间
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    /**
     * 创建人id
     */
    public Long getCreateBy() {
        return createBy;
    }

    /**
     * 创建人id
     */
    public void setCreateBy(Long createBy) {
        this.createBy = createBy;
    }

    /**
     * 更新人id
     */
    public Long getUpdateBy() {
        return updateBy;
    }

    /**
     * 更新人id
     */
    public void setUpdateBy(Long updateBy) {
        this.updateBy = updateBy;
    }

    /**
     * 备注
     */
    public String getRemark() {
        return remark;
    }

    /**
     * 备注
     */
    public void setRemark(String remark) {
        this.remark = remark;
    }

    /**
     * 床位编号
     */
    public String getBedNumber() {
        return bedNumber;
    }

    /**
     * 床位编号
     */
    public void setBedNumber(String bedNumber) {
        this.bedNumber = bedNumber;
    }

    /**
     * 床位id
     */
    public Long getBedId() {
        return bedId;
    }

    /**
     * 床位id
     */
    public void setBedId(Long bedId) {
        this.bedId = bedId;
    }

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        Elder other = (Elder) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getName() == null ? other.getName() == null : this.getName().equals(other.getName()))
            && (this.getImage() == null ? other.getImage() == null : this.getImage().equals(other.getImage()))
            && (this.getIdCardNo() == null ? other.getIdCardNo() == null : this.getIdCardNo().equals(other.getIdCardNo()))
            && (this.getSex() == null ? other.getSex() == null : this.getSex().equals(other.getSex()))
            && (this.getStatus() == null ? other.getStatus() == null : this.getStatus().equals(other.getStatus()))
            && (this.getPhone() == null ? other.getPhone() == null : this.getPhone().equals(other.getPhone()))
            && (this.getBirthday() == null ? other.getBirthday() == null : this.getBirthday().equals(other.getBirthday()))
            && (this.getAddress() == null ? other.getAddress() == null : this.getAddress().equals(other.getAddress()))
            && (this.getIdCardNationalEmblemImg() == null ? other.getIdCardNationalEmblemImg() == null : this.getIdCardNationalEmblemImg().equals(other.getIdCardNationalEmblemImg()))
            && (this.getIdCardPortraitImg() == null ? other.getIdCardPortraitImg() == null : this.getIdCardPortraitImg().equals(other.getIdCardPortraitImg()))
            && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
            && (this.getUpdateTime() == null ? other.getUpdateTime() == null : this.getUpdateTime().equals(other.getUpdateTime()))
            && (this.getCreateBy() == null ? other.getCreateBy() == null : this.getCreateBy().equals(other.getCreateBy()))
            && (this.getUpdateBy() == null ? other.getUpdateBy() == null : this.getUpdateBy().equals(other.getUpdateBy()))
            && (this.getRemark() == null ? other.getRemark() == null : this.getRemark().equals(other.getRemark()))
            && (this.getBedNumber() == null ? other.getBedNumber() == null : this.getBedNumber().equals(other.getBedNumber()))
            && (this.getBedId() == null ? other.getBedId() == null : this.getBedId().equals(other.getBedId()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getName() == null) ? 0 : getName().hashCode());
        result = prime * result + ((getImage() == null) ? 0 : getImage().hashCode());
        result = prime * result + ((getIdCardNo() == null) ? 0 : getIdCardNo().hashCode());
        result = prime * result + ((getSex() == null) ? 0 : getSex().hashCode());
        result = prime * result + ((getStatus() == null) ? 0 : getStatus().hashCode());
        result = prime * result + ((getPhone() == null) ? 0 : getPhone().hashCode());
        result = prime * result + ((getBirthday() == null) ? 0 : getBirthday().hashCode());
        result = prime * result + ((getAddress() == null) ? 0 : getAddress().hashCode());
        result = prime * result + ((getIdCardNationalEmblemImg() == null) ? 0 : getIdCardNationalEmblemImg().hashCode());
        result = prime * result + ((getIdCardPortraitImg() == null) ? 0 : getIdCardPortraitImg().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        result = prime * result + ((getUpdateTime() == null) ? 0 : getUpdateTime().hashCode());
        result = prime * result + ((getCreateBy() == null) ? 0 : getCreateBy().hashCode());
        result = prime * result + ((getUpdateBy() == null) ? 0 : getUpdateBy().hashCode());
        result = prime * result + ((getRemark() == null) ? 0 : getRemark().hashCode());
        result = prime * result + ((getBedNumber() == null) ? 0 : getBedNumber().hashCode());
        result = prime * result + ((getBedId() == null) ? 0 : getBedId().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", name=").append(name);
        sb.append(", image=").append(image);
        sb.append(", idCardNo=").append(idCardNo);
        sb.append(", sex=").append(sex);
        sb.append(", status=").append(status);
        sb.append(", phone=").append(phone);
        sb.append(", birthday=").append(birthday);
        sb.append(", address=").append(address);
        sb.append(", idCardNationalEmblemImg=").append(idCardNationalEmblemImg);
        sb.append(", idCardPortraitImg=").append(idCardPortraitImg);
        sb.append(", createTime=").append(createTime);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", createBy=").append(createBy);
        sb.append(", updateBy=").append(updateBy);
        sb.append(", remark=").append(remark);
        sb.append(", bedNumber=").append(bedNumber);
        sb.append(", bedId=").append(bedId);
        sb.append("]");
        return sb.toString();
    }
}