package xyz.dma.soft.conf;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Configuration
@ConfigurationProperties("service.session.storage")
public class SessionInfoStorageConfig {
    private String cachePath;
    private Long cacheDiskSize;
    private Long removeTimeout;
    private Integer evictionThreads;
    private Long evictionTimeout;
}
