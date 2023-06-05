package kr.co.fns.app.config;

import kr.co.fns.app.core.paging.interceptor.MysqlRowBoundsInterceptor;
import kr.co.fns.app.core.paging.interceptor.PrepareMysqlInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MybatisRowBoundConfig {

    @Bean
    public PrepareMysqlInterceptor prepareMysqlInterceptor() {
        return new PrepareMysqlInterceptor();
    }

    @Bean
    public MysqlRowBoundsInterceptor mysqlRowBoundsInterceptor() {
        return new MysqlRowBoundsInterceptor();
    }

}
