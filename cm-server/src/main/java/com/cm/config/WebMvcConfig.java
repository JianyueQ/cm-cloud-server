package com.cm.config;

import com.cm.interceptor.AdminInterceptor;
import com.cm.interceptor.StudentInterceptor;
import com.cm.interceptor.TeacherInterceptor;
import com.cm.json.JacksonObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * 自定义配置类
 * @author 31373
 */
@Configuration
@Slf4j
public class WebMvcConfig implements WebMvcConfigurer {

    @Autowired
    private AdminInterceptor adminInterceptor;
    @Autowired
    private StudentInterceptor studentInterceptor;
    @Autowired
    private TeacherInterceptor teacherInterceptor;

    /**
     * 拓展springMvc消息转换器
     */
    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        log.info("拓展消息转换器...");
        //创建消息转换器对象
        MappingJackson2HttpMessageConverter messageConverter = new MappingJackson2HttpMessageConverter();
        //设置对象转换器，底层使用Jackson将Java对象转为json
        messageConverter.setObjectMapper(new JacksonObjectMapper());
        converters.removeIf(c -> c instanceof MappingJackson2HttpMessageConverter);
        //将上面的消息转换器对象追加到mvc框架的转换器集合中
        converters.add(0, messageConverter);
    }

    /**
     * 注册自定义拦截器
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        log.info("开始注册自定义拦截器...");
        registry.addInterceptor(adminInterceptor).addPathPatterns("/admin/**").excludePathPatterns("/admin/login").order(0);
        registry.addInterceptor(studentInterceptor).addPathPatterns("/student/**").excludePathPatterns("/student/login").order(0);
        registry.addInterceptor(teacherInterceptor).addPathPatterns("/teacher/**").excludePathPatterns("/teacher/login").order(0);
    }
}
