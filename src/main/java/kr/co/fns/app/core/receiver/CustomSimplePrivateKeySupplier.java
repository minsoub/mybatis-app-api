package kr.co.fns.app.core.receiver;

import com.oracle.bmc.auth.SimplePrivateKeySupplier;
import io.swagger.v3.oas.models.security.SecurityScheme;
import lombok.extern.slf4j.Slf4j;

import java.io.InputStream;

@Slf4j
public class CustomSimplePrivateKeySupplier extends SimplePrivateKeySupplier {

    private String pemFilePath;

    public CustomSimplePrivateKeySupplier(String pemFilePath) {
        super(pemFilePath);
        this.pemFilePath = pemFilePath;
    }

    @Override
    public InputStream get() {
        log.debug("permfile inpustream get....");

        return getClass().getClassLoader().getResourceAsStream(pemFilePath);
    }
}
