package com.config;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@ComponentScan(basePackages = "com")
@EnableTransactionManagement
public class Config {
    @Bean
    public DataSource getDataSource(){
        return new DriverManagerDataSource("jdbc:mysql://localhost:3306/emp_db","root","root");

    }

    @Bean
    public LocalSessionFactoryBean getSessionFactory(){
        LocalSessionFactoryBean lsfb=new LocalSessionFactoryBean();
        lsfb.setDataSource(getDataSource());
        lsfb.setHibernateProperties(getHibernateProperties());
        lsfb.setPackagesToScan("com");
        return lsfb;
    }

    public Properties getHibernateProperties(){
        Properties props=new Properties();
        props.setProperty("hibernate.dialect","org.hibernate.dialect.MySQLDialect");
        props.setProperty("hibernate.showSql","true");
        return props;
    }

    @Bean
    public HibernateTransactionManager transactionManager(SessionFactory sessionFactory){
        HibernateTransactionManager txt=new HibernateTransactionManager();
        txt.setSessionFactory(sessionFactory);
        return txt;
    }
}
