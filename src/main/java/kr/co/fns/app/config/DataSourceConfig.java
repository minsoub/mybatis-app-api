package kr.co.fns.app.config;

import com.zaxxer.hikari.HikariDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.LazyConnectionDataSourceProxy;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
@MapperScan(basePackages = {"kr/co/fns/app/api/*/mapper", "kr/co/fns/app/core/*/mapper"}, sqlSessionFactoryRef = "sqlSessionFactory")
public class DataSourceConfig {

    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.hikari.master")
    public DataSource masterDataSource() {
        return DataSourceBuilder.create().type(HikariDataSource.class).build();
    }

    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.hikari.slave")
    public DataSource slaveDataSource() {
        return DataSourceBuilder.create().type(HikariDataSource.class).build();
    }

    @Bean
    @DependsOn({"masterDataSource", "slaveDataSource"})
    public RoutingDataSource routingDataSource(@Qualifier("masterDataSource") DataSource master,
                                               @Qualifier("slaveDataSource") DataSource slave ) {



        Map<Object, Object> targetDataSources = new HashMap<>();

        targetDataSources.put("master", master);
        targetDataSources.put("slave", slave);

        RoutingDataSource routingDataSource = new RoutingDataSource();
        routingDataSource.setTargetDataSources(targetDataSources);
        routingDataSource.setDefaultTargetDataSource(master);

        return routingDataSource;
    }

    @Bean
    @Primary
    public LazyConnectionDataSourceProxy lazyRoutingDataSource(@Qualifier(value = "routingDataSource") DataSource routingDataSource) {
        return new LazyConnectionDataSourceProxy(routingDataSource);
    }

    @Bean
    public PlatformTransactionManager transactionManager(@Qualifier(value = "lazyRoutingDataSource") LazyConnectionDataSourceProxy lazyRoutingDataSource) {
        return new DataSourceTransactionManager(lazyRoutingDataSource);
    }

    //@DependsOn({"dataSource"})
    @Bean(name = "sqlSessionFactory")
    public SqlSessionFactory sqlSessionFactory(DataSource dataSource, ApplicationContext applicationContext) throws Exception {

        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSource);
        sqlSessionFactoryBean.setMapperLocations(applicationContext.getResources("classpath:mybatis/mapper/*/*.xml"));
        //sqlSessionFactoryBean.setTypeAliasesPackage("kr.co.fantoo.gdc2023");

//        mybatis:
//        mapper-locations: /mybatis/mapper/**/*Mapper.xml
//        configuration:
//        map-underscore-to-camel-case: true #camel case 설정

        org.apache.ibatis.session.Configuration configuration = new org.apache.ibatis.session.Configuration();
        //configuration.setCacheEnabled(false);
        configuration.setMapUnderscoreToCamelCase(true);
        //configuration.setUseGeneratedKeys(true);
        sqlSessionFactoryBean.setConfiguration(configuration);

        return sqlSessionFactoryBean.getObject();
    }

    @DependsOn({"sqlSessionFactory"})
    @Bean(name = "sqlSessionTemplate")
    public SqlSessionTemplate sqlSessionTemplate(SqlSessionFactory sqlSessionFactory) throws Exception {

        return new SqlSessionTemplate(sqlSessionFactory);
    }
}

