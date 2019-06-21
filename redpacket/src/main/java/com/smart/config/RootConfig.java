package com.smart.config;

import com.alibaba.druid.pool.DruidDataSource;
import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.TransactionManagementConfigurer;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@Configuration
@ComponentScan(value="com.smart.service",includeFilters = {@ComponentScan.Filter(type=FilterType.ANNOTATION,value={Service.class})})
//使用事务驱动管理器
@EnableTransactionManagement
//实现接口TransactionManagementConfigurer，这样可以配置注解驱动事务
public class RootConfig implements TransactionManagementConfigurer {

    private DataSource dataSource=null;
    /**
     *配置数据源
     * @return
     */
    @Bean(name="dataSource")
    public DataSource initDataSource(){
        if(dataSource !=null){
            return dataSource;
        }
        Properties props = new Properties();
        try(InputStream in=RootConfig.class.getClassLoader().getResourceAsStream("jdbc.properties")){
            props.load(in);
            DruidDataSource druidDataSource = new DruidDataSource();
            druidDataSource.setDriverClassName(props.getProperty("jdbc.driver"));
            druidDataSource.setUrl(props.getProperty("jdbc.url"));
            druidDataSource.setUsername(props.getProperty("jdbc.username"));
            druidDataSource.setPassword(props.getProperty("jdbc.password"));
            return druidDataSource;
        }catch (IOException e) {
            e.printStackTrace();
        }
        return dataSource;
    }

    /**
     * 配置sqlSessionFactoryBean
     * @return
     */
    @Bean(name="sqlSessionFactory")
    public SqlSessionFactoryBean initSqlSessionFactory(){
        SqlSessionFactoryBean sqlSessionFactory = new SqlSessionFactoryBean();
        sqlSessionFactory.setDataSource(initDataSource());
        Resource resource = new ClassPathResource("mybatis/mybatis-config.xml");
        sqlSessionFactory.setConfigLocation(resource);
        return sqlSessionFactory;
    }

    /**
     * 通过自动扫描，发现Mapper的接口
     * @return
     */
    @Bean
    public MapperScannerConfigurer initMapperScannerConfigurer(){
        MapperScannerConfigurer msc = new MapperScannerConfigurer();
        msc.setBasePackage("com.smart.mapper");
        msc.setSqlSessionFactoryBeanName("sqlSessionFactory");
        msc.setAnnotationClass(Mapper.class);
        return msc;
    }

    /**
     * 实现接口方法，注册注解事务，当@Transactional 使用的时候产生数据库事务
     */
    @Override
    public PlatformTransactionManager annotationDrivenTransactionManager() {
        DataSourceTransactionManager transactionManager = new DataSourceTransactionManager();
        transactionManager.setDataSource(initDataSource());
        return transactionManager;
    }
}
