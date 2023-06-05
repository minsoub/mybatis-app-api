package kr.co.fns.app.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConfigurationPropertiesBinding;
import org.springframework.boot.context.properties.ConstructorBinding;

@ConfigurationPropertiesBinding
@ConfigurationProperties(prefix = "application")
@Getter
@Setter
public class ApplicationProperties {
    private String version;
    private String prefix;
    private String[] excludePrefixPath;
}
