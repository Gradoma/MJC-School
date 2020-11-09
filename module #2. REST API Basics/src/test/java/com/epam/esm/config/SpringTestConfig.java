package com.epam.esm.config;

import com.epam.esm.dao.impl.GiftCertificateDaoImpl;
import com.epam.esm.dao.impl.TagDaoImpl;
import com.epam.esm.dao.mapper.GiftCertificateMapper;
import com.epam.esm.dao.mapper.TagMapper;
import com.epam.esm.service.GiftCertificateService;
import com.epam.esm.service.impl.GiftCertificateServiceImpl;
import com.epam.esm.service.impl.TagServiceImpl;
import com.epam.esm.service.mapper.GiftCertificateDtoMapper;
import com.epam.esm.service.mapper.TagDtoMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;

import javax.sql.DataSource;

@Configuration
@Import({TagDaoImpl.class, GiftCertificateDaoImpl.class, TagMapper.class, GiftCertificateMapper.class,
        TagServiceImpl.class, GiftCertificateServiceImpl.class, LocalValidatorFactoryBean.class, TagDtoMapper.class,
        GiftCertificateDtoMapper.class, MethodValidationPostProcessor.class, LocalValidatorFactoryBean.class})
public class SpringTestConfig {

    @Bean
    public JdbcTemplate getJdbcTemplate(@Qualifier("h2DataSource") DataSource dataSource){
        return new JdbcTemplate(dataSource);
    }

    @Bean
    DataSource h2DataSource() {
        return new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.H2)
                .setScriptEncoding("UTF-8")
                .addScript("db/schema.sql")
                .build();
    }
}
