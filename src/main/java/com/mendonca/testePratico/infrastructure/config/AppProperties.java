package com.mendonca.testePratico.infrastructure.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Map;

@Data
@Component
@ConfigurationProperties(prefix = "app")
public class AppProperties {
    
    private File file = new File();
    private Processing processing = new Processing();
    private Security security = new Security();
    
    @Data
    public static class File {
        private String maxSize;
        private List<String> allowedFormats;
        private Validation validation = new Validation();
        
        @Data
        public static class Validation {
            private List<String> headerPatterns;
            private String secondLine;
        }
    }
    
    @Data
    public static class Processing {
        private int batchSize;
        private int threadPoolSize;
        private int queueCapacity;
        private String tempDirectory;
    }
    
    @Data
    public static class Security {
        private Jwt jwt = new Jwt();
        private Map<String, String> tokens;
        
        @Data
        public static class Jwt {
            private String secret;
            private long expiration;
        }
    }
}