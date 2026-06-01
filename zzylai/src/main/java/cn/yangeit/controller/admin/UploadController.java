package cn.yangeit.controller.admin;

import cn.yangeit.common.AjaxResult;
import cn.yangeit.common.AliOSSUtils;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Slf4j
@RestController
@RequestMapping("/admin")
@Tag(name = "上传接口")
public class UploadController {


    @Autowired
    private AliOSSUtils aliOSSUtils;


    @PostMapping("/upload")
    @Parameter(description = "上传接口")
    public AjaxResult upload(@Parameter(description = "上传的文件", required = true) @RequestBody MultipartFile file) throws IOException {
        log.info("上传源文件文件名字：{}",file.getOriginalFilename());
        //调用阿里云OSS工具类，将上传上来的文件存入阿里云
        String url = aliOSSUtils.upload(file);
        //将图片上传完成后的url返回，用于浏览器回显展示
        return AjaxResult.success("上传成功", url );
    }

}
