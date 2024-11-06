package com.ruoyi.system.consumer;

import com.ruoyi.system.properties.TgProperties;
import com.ruoyi.system.util.ChromeUtil;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Slf4j
@Configuration
@EnableScheduling
public class ChromeMapConsumer {

    @Autowired
    private ChromeUtil chromeUtil;

    @Autowired
    private TgProperties tgProperties;

    @Scheduled(cron = "0/20 * * * * ?")
    public  void execute() throws InterruptedException {
        while (chromeUtil.getChromeQueue().size()<1){
            System.setProperty("webdriver.chrome.driver", tgProperties.getChromeDriverPath());
            ChromeOptions options = new ChromeOptions();
            options.addArguments("--window-size=1920,1080");
            WebDriver driver = new ChromeDriver(options);
            String url = "https://web.telegram.org/a/";
            driver.get(url);
            chromeUtil.getChromeQueue().put(driver);
        }
    }
}
