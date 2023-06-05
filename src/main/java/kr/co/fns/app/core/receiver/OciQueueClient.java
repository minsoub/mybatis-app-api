package kr.co.fns.app.core.receiver;

import com.oracle.bmc.Region;
import com.oracle.bmc.auth.*;
import com.oracle.bmc.queue.QueueAsyncClient;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.ResourceLoader;

import java.io.IOException;
import java.io.InputStream;

@Configuration
@AllArgsConstructor
@Slf4j
public class OciQueueClient {
    private final OciProperties ociProperties;
    private final ResourceLoader resourceLoader;

    @Bean
    public QueueAsyncClient queueAsyncClient() throws IOException {
        log.debug("properties => {}", ociProperties);
        log.debug("{}, {}, {}", ociProperties.getQueueName(), ociProperties.getKeyFile(), ociProperties.getRegion());

        CustomSimplePrivateKeySupplier privateKeySupplier = new CustomSimplePrivateKeySupplier(ociProperties.getKeyFile());
        log.debug("privateKeySupplier => {}", privateKeySupplier.toString());
        log.debug("resion => {}", Region.fromRegionId(ociProperties.getRegion()));

        SimpleAuthenticationDetailsProvider simpleAuthenticationDetailsProvider = SimpleAuthenticationDetailsProvider
                .builder()
                .region(Region.fromRegionId(ociProperties.getRegion()))
                .tenantId(ociProperties.getApiTenancy())
                .userId(ociProperties.getApiUser())
                .fingerprint(ociProperties.getFingerPrint())
                .privateKeySupplier(privateKeySupplier)
                .build();

        return QueueAsyncClient.builder()
                .endpoint(ociProperties.getEndPoint())
                .build(simpleAuthenticationDetailsProvider);
    }
}
