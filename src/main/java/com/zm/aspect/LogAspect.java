package com.zm.aspect;


import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.filter.SimplePropertyPreFilter;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

@Aspect
@Component
public class LogAspect {

    private final static Logger LOG = LoggerFactory.getLogger(LogAspect.class);

    /** 定义一个切点 */
    @Pointcut("execution(public * com.zm.*.controller..*Controller.*(..))")
    public void controllerPointcut() {}



    @Before("controllerPointcut()")
    public void doBefore(JoinPoint joinPoint) throws Throwable {


        // 开始打印请求日志
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        Signature signature = joinPoint.getSignature();
        String name = signature.getName();

        // 打印请求信息
        LOG.info("------------- aop开始 -------------");
        LOG.info("请求地址: {} {}", request.getRequestURL().toString(), request.getMethod());
        LOG.info("类名方法: {}.{}", signature.getDeclaringTypeName(), name);
        LOG.info("远程地址: {}", request.getRemoteAddr());

        // 打印请求参数
        Object[] args = joinPoint.getArgs();
		// LOG.info("请求参数: {}", JSONObject.toJSONString(args));

		Object[] arguments  = new Object[args.length];
        for (int i = 0; i < args.length; i++) {
            if (args[i] instanceof ServletRequest
                    || args[i] instanceof ServletResponse
                    || args[i] instanceof MultipartFile) {
                continue;
            }
            arguments[i] = args[i];
        }
        // 排除字段，敏感字段或太长的字段不显示
        String[] excludeProperties = {"password", "file"};
        // 创建过滤器对象
        SimplePropertyPreFilter filter = new SimplePropertyPreFilter();
        filter.getExcludes().addAll(Arrays.asList(excludeProperties));

        // 打印过滤后的参数
        String json = JSON.toJSONString(arguments, filter);
        LOG.info("请求参数: {}", json);

    }

    @Around("controllerPointcut()")
    public Object doAround(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        Object result = proceedingJoinPoint.proceed();
        // 排除字段，敏感字段或太长的字段不显示

        //创建要排除的字段列表：
        String[] excludeProperties = {"password", "file"};
        //创建过滤器对象并添加排除字段：
        SimplePropertyPreFilter filter = new SimplePropertyPreFilter();
        filter.getExcludes().addAll(Arrays.asList(excludeProperties));
        //序列化JSON并打印结果：
        String resultJson = JSON.toJSONString(result, filter);
        LOG.info("返回结果: {}", resultJson);
        LOG.info("------------- 结束 耗时：{} ms -------------", System.currentTimeMillis() - startTime);
        return result;
    }

    /**
     * 使用nginx做反向代理，需要用该方法才能取到真实的远程IP
     * @param request
     * @return
     */
    public String getRemoteIp(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

}
