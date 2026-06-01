package cn.yangeit.vo.admin;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
//"产品信息响应模型")
public class ProductVo {
   @Schema(description = "产品的ProductKey,物联网平台产品唯一标识")
    private String productId;
    @Schema(description = "产品名称")
    private String name;
}
