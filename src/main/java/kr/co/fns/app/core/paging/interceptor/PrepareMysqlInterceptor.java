package kr.co.fns.app.core.paging.interceptor;

import kr.co.fns.app.core.paging.dto.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.DefaultReflectorFactory;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.ReflectorFactory;
import org.apache.ibatis.reflection.factory.DefaultObjectFactory;
import org.apache.ibatis.reflection.factory.ObjectFactory;
import org.apache.ibatis.reflection.wrapper.DefaultObjectWrapperFactory;
import org.apache.ibatis.reflection.wrapper.ObjectWrapperFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ObjectUtils;

import java.sql.Connection;
import java.util.Properties;


/**
 * 22/03/25
 * mybatis 실행시 select 문일때 count(*) 쿼리와 limit 붙인 페이징 쿼리로 실행한다. -jyson
 * MysqlRowBoundsInterceptor , PrepareMysqlInterceptor 와 짝이다.
 */
@Slf4j
@Intercepts({@Signature(type = StatementHandler.class, method = "prepare",
        args = {Connection.class, Integer.class})})
public class PrepareMysqlInterceptor implements Interceptor {
    private static final Logger logger = LoggerFactory.getLogger(PrepareMysqlInterceptor.class);
    private static final ObjectFactory DEFAULT_OBJECT_FACTORY = new DefaultObjectFactory();
    private static final ObjectWrapperFactory DEFAULT_OBJECT_WRAPPER_FACTORY = new DefaultObjectWrapperFactory();
    private static final ReflectorFactory DEFAULT_REFLECTOR_FACTORY = new DefaultReflectorFactory();


    /**
     * Limit 추가.
     * @param originalSql
     * @param pageInfo
     * @return
     */
    private String attachLimitQuery(String originalSql, PageInfo pageInfo) {
        //TODO , Limit 만 이용하는 방식 자체는 index를 타지않기에 추후 상황에 따라 대용량일시 변경 필요해 보인다. -jyson
        StringBuilder sb = new StringBuilder(originalSql);

        if(!ObjectUtils.isEmpty(pageInfo.getSort())){
            sb.append(" order by ");
            sb.append(pageInfo.getSort().trim());
            sb.append(" "+ pageInfo.getSortType());
        }

        sb.append(" LIMIT ");
        sb.append(pageInfo.getSize()); // 출력 size
        sb.append(" offset ");
        sb.append((pageInfo.getPage()) * pageInfo.getSize());  // 출력 page

        String attachLimitQuery = sb.toString();
        return attachLimitQuery;
    }


    /**
     * pageInfo 체크., 여기서 사용할지 말지 고민하면 될듯.
     * @param pageInfo
     * @return
     */
    private boolean isNullPageInfo(PageInfo pageInfo) {
        return pageInfo.getPage() == null || pageInfo.getSize() == null || pageInfo.getPagingStatus() == false;
    }

    /**
     * count 쿼리
     * @param originalSql
     * @param pageInfo
     * @return
     */
    private String attachCountQuery(String originalSql, PageInfo pageInfo) {
        StringBuilder sb = new StringBuilder("SELECT COUNT(*) FROM ( ");
        sb.append(originalSql);
        sb.append(" ) COUNT_TABLE ");

        String attachedCountQuery = sb.toString();
        return attachedCountQuery;
    }


    /**
     * select 쿼리문 커스텀.
     * @param originalSql
     * @param pageInfo
     * @return
     */
    private String attachQuery(String originalSql, PageInfo pageInfo) {
        if (pageInfo.getTotalCount() == -1 && pageInfo.getPagingStatus() == true) {
            return attachCountQuery(originalSql, pageInfo);
        }

        if (!isNullPageInfo(pageInfo)){
            return attachLimitQuery(originalSql, pageInfo);
        }

        return originalSql;
    }

    /**
     * select 쿼리만 실행.
     * @param metaStatementHandler
     * @return
     */
    private boolean isSelectSqlCommandType(MetaObject metaStatementHandler) {
        SqlCommandType sqlCommandType =
                (SqlCommandType) metaStatementHandler.getValue("delegate.mappedStatement.sqlCommandType");

        return sqlCommandType.equals(SqlCommandType.SELECT);
    }


    public Object intercept(Invocation invocation) throws Throwable {
        try {
            MetaObject metaStatementHandler = MetaObject.forObject(invocation.getTarget(),
                    DEFAULT_OBJECT_FACTORY, DEFAULT_OBJECT_WRAPPER_FACTORY, DEFAULT_REFLECTOR_FACTORY);

            if (isSelectSqlCommandType(metaStatementHandler)) {
                PageInfo pageInfo = (PageInfo) metaStatementHandler.getValue("delegate.rowBounds");
                String originalSql = (String) metaStatementHandler.getValue("delegate.boundSql.sql");

                // limit 추가된 query
                metaStatementHandler.setValue("delegate.boundSql.sql", attachQuery(originalSql, pageInfo));
            }
        } catch (ClassCastException e) {

        }
        return invocation.proceed();
    }


    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    public void setProperties(Properties properties) {
    }

}