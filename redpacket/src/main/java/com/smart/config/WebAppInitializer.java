package com.smart.config;

import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

import javax.servlet.MultipartConfigElement;
import javax.servlet.ServletRegistration;

public class WebAppInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {
    /**
     * 依赖注入环境
     * @return
     */
    @Override
    protected Class<?>[] getRootConfigClasses() {
        return new Class[]{ RootConfig.class };
    }

    /**
     * DispatcherServlet环境配置
     * @return
     */
    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class[]{WebConfig.class};
    }

    /**
     * DispatchServlet拦截请求配置
     * @return
     */
    @Override
    protected String[] getServletMappings() {
        return new String[]{"*.html"};
    }

    @Override
    public void customizeRegistration(ServletRegistration.Dynamic dynamic){
        String filepath="d:/";
        Long singleMax=(long)(5*Math.pow(2,20));
        Long totalMax=(long)(10*Math.pow(2,10));
        dynamic.setMultipartConfig(new MultipartConfigElement(filepath,singleMax,totalMax,0));
    }
}
