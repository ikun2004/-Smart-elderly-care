package cn.yangeit.config;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONUtil;
import cn.yangeit.common.AjaxResult;
import cn.yangeit.common.JwtUtils;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

//自定义拦截器
@Component
public class LoginCheckInterceptor implements HandlerInterceptor {
    //目标资源方法执行前执行。 返回true：放行    返回false：不放行
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        System.out.println("开始拦截"+request.getRequestURI());

        //判断当前请求是否是handler()  controller中的一个方法
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
            //获取token
        String token= request.getHeader("authorization");
        if (ObjectUtil.isEmpty(token)) {
          //有错，需要告诉前端 错误信息
            throw new BaseException("用户未登录");
        }
        //解析token
        Claims claims = JwtUtils.parseJWT(token);
        Long userId = Long.valueOf(claims.get("userId").toString());
        if (ObjectUtil.isEmpty(userId)) {
          throw  new BaseException("用户未登录");
        }
        //存入到TheadLocal中
        BaseContext.setCurrentId(userId);
        //打印 当前线程ID
        System.out.println("拦截器 当前线程ID: "+Thread.currentThread().getId());
        return true;
    }

    //目标资源方法执行后执行
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        System.out.println("拦截器处理之后 ");
    }

    //视图渲染完毕后执行，最后执行
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        System.out.println("拦截器处理完成 ");
    }
}
