package com.ruoyi.system.util;

import com.ruoyi.system.domain.SysChromeUser;
import lombok.Data;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.concurrent.ArrayBlockingQueue;

@Configuration
@Data
public class ChromeUtil {

    ArrayBlockingQueue<WebDriver> workQueue = new ArrayBlockingQueue<>(10);
    private HashMap<String,WebDriver> ChromeMap = new HashMap<String,WebDriver>();
    public ChromeUtil() {

    }

    @Bean
    public  ArrayBlockingQueue getChromeQueue() {
        return workQueue;
    }
    public WebElement findElmentByXpath(WebDriver driver,String xpath){

        WebElement sendPhoneCodeInput = new WebDriverWait(driver, 20).until(
                ExpectedConditions.visibilityOfElementLocated(By.xpath(xpath)) // 替换为实际的元素定位
        );
         return sendPhoneCodeInput;
    }

    public WebElement findElmentByXpath(WebDriver driver,String xpath,int date){

        WebElement sendPhoneCodeInput = new WebDriverWait(driver, date).until(
                ExpectedConditions.visibilityOfElementLocated(By.xpath(xpath)) // 替换为实际的元素定位
        );
        return sendPhoneCodeInput;
    }


    public SysChromeUser getChromeLocalSession(WebDriver driver){
        JavascriptExecutor js = (JavascriptExecutor) driver;
        String dc = (String) js.executeScript("return localStorage.getItem('dc');");
        String dc1_auth_key = (String) js.executeScript("return localStorage.getItem('dc1_auth_key');");
        String dc1_hash = (String) js.executeScript("return localStorage.getItem('dc1_hash');");
        String dc2_auth_key = (String) js.executeScript("return localStorage.getItem('dc2_auth_key');");
        String dc2_hash = (String) js.executeScript("return localStorage.getItem('dc2_hash');");
        String dc4_auth_key = (String) js.executeScript("return localStorage.getItem('dc4_auth_key');");
        String dc4_hash = (String) js.executeScript("return localStorage.getItem('dc4_hash');");
        String dc5_auth_key = (String) js.executeScript("return localStorage.getItem('dc5_auth_key');");
        String dc5_hash = (String) js.executeScript("return localStorage.getItem('dc5_hash');");
        String tgme_sync = (String) js.executeScript("return localStorage.getItem('tgme_sync');");
        String tt_active_tab = (String) js.executeScript("return localStorage.getItem('tt-active-tab');");
        String tt_multi_tab = (String) js.executeScript("return localStorage.getItem('tt-multitab');");
        String user_auth = (String) js.executeScript("return localStorage.getItem('user_auth');");
        SysChromeUser chromeUser = new SysChromeUser();
        chromeUser.setDc(dc);
        chromeUser.setDc1AuthKey(dc1_auth_key);
        chromeUser.setDc1Hash(dc1_hash);
        chromeUser.setDc2AuthKey(dc2_auth_key);
        chromeUser.setDc2Hash(dc2_hash);
        chromeUser.setDc4AuthKey(dc4_auth_key);
        chromeUser.setDc4Hash(dc4_hash);
        chromeUser.setDc5AuthKey(dc5_auth_key);
        chromeUser.setDc5Hash(dc5_hash);
        chromeUser.setTgmeSync(tgme_sync);
        chromeUser.setTtActiveTab(tt_active_tab);
        chromeUser.setTtMultiTab(tt_multi_tab);
        chromeUser.setUserAuth(user_auth);
        return  chromeUser;
    }


}
