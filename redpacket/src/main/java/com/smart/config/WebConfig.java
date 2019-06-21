package com.smart.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.HandlerAdapter;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import java.util.ArrayList;
import java.util.List;

@Configuration
@ComponentScan(value="com.smart.*",includeFilters = {
        @ComponentScan.Filter(type= FilterType.ANNOTATION,value=Controller.class)
})
@EnableWebMvc
public class WebConfig {

    /***
     * 通过注解 @Bean 初始化视图解析器
     * @return ViewResolver 视图解析器
     */
    @Bean(name="internalResourceViewResolver")
    public ViewResolver initViewResolver(){
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setPrefix("/WEB-INF/jsp/");
        viewResolver.setSuffix(".jsp");
        return viewResolver;
    }

    /**
     * 初始化RequestMappingHandlerAdapter，并加载Http的Json转换器
     * @return  RequestMappingHandlerAdapter 对象
     */
    @Bean(name="requestMappingHandlerAdapter")
    public HandlerAdapter initRequestMappingHandlerAdapter(){
        RequestMappingHandlerAdapter requestMappingHandlerAdapter = new RequestMappingHandlerAdapter();
        //HTTP JSON转换器
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        //MappingJackson2HttpMessageConverter接收JSON类型消息的转换
        MediaType mediaType=MediaType.APPLICATION_JSON_UTF8;
        List<MediaType> mediaTypes = new ArrayList<>();
        mediaTypes.add(mediaType);
        //加入转换器的支持类型
        converter.setSupportedMediaTypes(mediaTypes);
        //往适配器加入json转换器
        requestMappingHandlerAdapter.getMessageConverters().add(converter);
        return requestMappingHandlerAdapter;
    }
}
