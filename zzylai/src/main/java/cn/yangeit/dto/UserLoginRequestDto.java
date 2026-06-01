package cn.yangeit.dto;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserLoginRequestDto {


    private String nickName;


    private String code;


    private String phoneCode;
}