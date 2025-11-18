package com;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

@Configuration
public class Config {

    @Bean
    public DataSource getDataSource() {
        return new DriverManagerDataSource("jdbc:mysql://localhost:3306/emp_db", "root", "root");
    }

    @Bean("template")
    public JdbcTemplate getJDBCTemplate() {
        DataSource dataSource = getDataSource();
        return new JdbcTemplate(dataSource);
    }
}
