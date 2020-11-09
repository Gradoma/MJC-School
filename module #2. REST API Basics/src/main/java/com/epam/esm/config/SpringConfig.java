package com.epam.esm.config;

import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.mvc.method.annotation.ExceptionHandlerExceptionResolver;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Configuration
@EnableWebMvc
@ComponentScan("com.epam.esm")
public class SpringConfig{

    @Bean
    public JdbcTemplate getJdbcTemplate(BasicDataSource dataSource){
        return new JdbcTemplate(dataSource);
    }

    @Bean
    public BasicDataSource getDataSource(DbProperties dbProperties) {
        BasicDataSource ds = new BasicDataSource();
        ds.setDriverClassName(dbProperties.getDriver());
        ds.setUrl(dbProperties.getUrl());
        ds.setUsername(dbProperties.getUser());
        ds.setPassword(dbProperties.getPassword());
        return ds;
    }

    @Bean
    private static PropertySourcesPlaceholderConfigurer propertyConfigInDev() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    @Bean
    public MethodValidationPostProcessor validationPostProcessor() {
        return new MethodValidationPostProcessor();
    }

    @Bean
    public LocalValidatorFactoryBean validator() {
        LocalValidatorFactoryBean bean = new LocalValidatorFactoryBean();
        bean.setValidationMessageSource(messageSource());
        return bean;
    }

    @Bean
    public MessageSource messageSource() {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasenames("classpath:lang/message");
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }

    @Bean
    public AcceptHeaderLocaleResolver headerResolver(){
        AcceptHeaderLocaleResolver headerResolver = new AcceptHeaderLocaleResolver();
        headerResolver.setDefaultLocale(Locale.ENGLISH);
        return headerResolver;
    }

    @Bean
    public LocaleChangeInterceptor interceptor(){
        LocaleChangeInterceptor interceptor = new LocaleChangeInterceptor();
        interceptor.setParamName("lang");
        return interceptor;
    }

    //    @Bean
//    public ExceptionHandlerExceptionResolver exceptionResolver(MappingJackson2HttpMessageConverter converter){
//        ExceptionHandlerExceptionResolver resolver = new ExceptionHandlerExceptionResolver();
//        List<HttpMessageConverter<?>> converters = new ArrayList<>();
//        converters.add(converter);
//        resolver.setMessageConverters(converters);
//        return resolver;
//    }
//
//    @Bean
//    public MappingJackson2HttpMessageConverter jacksonConverter(){
//        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
//        List<MediaType> mediaTypeList = new ArrayList<>();
////        mediaTypeList.add(new MediaType(MediaType.TEXT_PLAIN, StandardCharsets.UTF_8));
////        mediaTypeList.add(new MediaType(MediaType.TEXT_HTML, StandardCharsets.UTF_8));
//        mediaTypeList.add(new MediaType(MediaType.APPLICATION_JSON, StandardCharsets.UTF_8));
////        mediaTypeList.add(new MediaType(MediaType.APPLICATION_FORM_URLENCODED, StandardCharsets.UTF_8));
//        converter.setSupportedMediaTypes(mediaTypeList);
//        return converter;
//    }
}
