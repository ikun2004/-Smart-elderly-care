package cn.yangeit.config;

import cn.yangeit.common.AjaxResult;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Hidden// 不显示在swagger中
@RestControllerAdvice
public class GlobalExceptionHandler {

    //处理异常
    @ExceptionHandler
    public AjaxResult ex(BaseException e){//方法形参中指定能够处理的异常类型
        e.printStackTrace();//打印堆栈中的异常信息
        //捕获到异常之后，响应一个标准的Result
       // System.out.println("这里是统一异常处理类"+e.getMessage());
      return AjaxResult.error(e.getMessage());
       // return AjaxResult.error("正繁忙");
    }
}
