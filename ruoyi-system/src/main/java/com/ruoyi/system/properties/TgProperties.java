package com.ruoyi.system.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @Author wuyangjian
 * @Version
 * @Date 2024/5/18 10:59
 */
@Data
@Component
@ConfigurationProperties(prefix = "telegram")
public class TgProperties {
    private String ChromeDriverPath;

}
