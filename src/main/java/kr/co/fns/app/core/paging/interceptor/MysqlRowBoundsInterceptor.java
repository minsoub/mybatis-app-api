package kr.co.fns.app.core.paging.interceptor;

import kr.co.fns.app.core.paging.dto.PagableResponse;
import kr.co.fns.app.core.paging.dto.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ResultMap;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.DefaultReflectorFactory;
import org.apache.ibatis.reflection.ReflectorFactory;
import org.apache.ibatis.reflection.factory.DefaultObjectFactory;
import org.apache.ibatis.reflection.factory.ObjectFactory;
import org.apache.ibatis.reflection.wrapper.DefaultObjectWrapperFactory;
import org.apache.ibatis.reflection.wrapper.ObjectWrapperFactory;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;


/**
 * 22/03/25
 * 해당 interceptor로 mybatis 페이징시 처리하도록 만들었다. -jyson
 * MysqlRowBoundsInterceptor , PrepareMysqlInterceptor 와 짝이다.
 */
@Slf4j
@Intercepts(@Signature(type= Executor.class, method="query", args= {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class}))
public class MysqlRowBoundsInterceptor implements Interceptor {
    private static final Logger logger = LoggerFactory.getLogger(MysqlRowBoundsInterceptor.class);
    private static final ObjectFactory DEFAULT_OBJECT_FACTORY = new DefaultObjectFactory();
    private static final ObjectWrapperFactory DEFAULT_OBJECT_WRAPPER_FACTORY = new DefaultObjectWrapperFactory();
    private static final ReflectorFactory DEFAULT_REFLECTOR_FACTORY = new DefaultReflectorFactory();


    private static String COUNT_ID_SUFFIX = "-Long";

    private List<ResultMap> createCountResultMaps(MappedStatement ms) {
        List<ResultMap> countResultMaps = new ArrayList<>();

        ResultMap countResultMap = new ResultMap.Builder(ms.getConfiguration(), ms.getId() + COUNT_ID_SUFFIX, Long.class, new ArrayList<>()).build();
        countResultMaps.add(countResultMap);

        return countResultMaps;
    }

    private MappedStatement createCountMappedStatement(MappedStatement ms) {
        List<ResultMap> countResultMaps = createCountResultMaps(ms);

        return new MappedStatement.Builder(ms.getConfiguration(), ms.getId() + COUNT_ID_SUFFIX,
                ms.getSqlSource(), ms.getSqlCommandType())
                .resource(ms.getResource())
                .parameterMap(ms.getParameterMap())
                .resultMaps(countResultMaps)
                .fetchSize(ms.getFetchSize())
                .timeout(ms.getTimeout())
                .statementType(ms.getStatementType())
                .resultSetType(ms.getResultSetType())
                .cache(ms.getCache())
                .flushCacheRequired(ms.isFlushCacheRequired())
                .useCache(true)
                .resultOrdered(ms.isResultOrdered())
                .keyGenerator(ms.getKeyGenerator())
                .keyColumn(ms.getKeyColumns() != null ? String.join(",", ms.getKeyColumns()) : null)
                .keyProperty(ms.getKeyProperties() != null ? String.join(",", ms.getKeyProperties()): null)
                .databaseId(ms.getDatabaseId())
                .lang(ms.getLang())
                .resultSets(ms.getResultSets() != null ? String.join(",", ms.getResultSets()): null)
                .build();
    }



    private PagableResponse<Object> createPagableResponse(List<Object> list, PageInfo pageInfo) {
        PagableResponse<Object> pagableResponse = new PagableResponse<>();
        pagableResponse.setList(list);
        pagableResponse.setPageInfo(pageInfo);

        return pagableResponse;
    }


    // 음수처리, 해당 처리 @이 dependency 가 안되어 일단 사용.- jyson
    private void checkPageInfo(PageInfo pageInfo){
        if(pageInfo.getPage() < 0 || pageInfo.getSize() < 0 ){
            pageInfo.setPage(0);
            pageInfo.setSize(10);
        }
    }

    public Object intercept(Invocation invocation) throws Throwable {
        try {
            PageInfo pageInfo = (PageInfo) invocation.getArgs()[2];
            checkPageInfo(pageInfo);

            MappedStatement originalMappedStatement = (MappedStatement) invocation.getArgs()[0];

            if(pageInfo.getPagingStatus()){
                MappedStatement countMappedStatement = createCountMappedStatement(originalMappedStatement);

                // COUNT
                invocation.getArgs()[0] = countMappedStatement;
                List<Long> totalCount = (List<Long>) invocation.proceed();
                pageInfo.setTotalCount((Long) totalCount.get(0));
            }

            // LIST
            invocation.getArgs()[0] = originalMappedStatement;
            List<Object> list = (List<Object>) invocation.proceed();

            return createPagableResponse(list, pageInfo);
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