package kr.co.fns.app.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Slf4j
public class RoutingDataSource extends AbstractRoutingDataSource {

    @Override
    protected Object determineCurrentLookupKey() {
        log.info("TransactionSynchronizationManager.isCurrentTransactionReadOnly() => {}", String.valueOf(TransactionSynchronizationManager.isCurrentTransactionReadOnly()));
        return TransactionSynchronizationManager.isCurrentTransactionReadOnly() ? "slave" : "master";
    }
}
