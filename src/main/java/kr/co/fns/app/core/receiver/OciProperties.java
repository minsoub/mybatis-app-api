package kr.co.fns.app.core.receiver;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
@Getter
@Setter
@Primary
public class OciProperties {

    @Value("${oci.api-tenancy}")
    private String apiTenancy;

    @Value("${oci.api-user}")
    private String apiUser;

    @Value("${oci.region}")
    private String region;

    @Value("${oci.finger-print}")
    private String fingerPrint;

    @Value("${oci.pass-phrase}")
    private String passPhrase;

    @Value("${oci.key-file}")
    private String keyFile;

    @Value("${oci.endpoint}")
    private String endPoint;

    @Value("${oci.queue-name}")
    private String queueName;
}
