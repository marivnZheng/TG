package com.ruoyi.system.util;


import cn.hutool.json.JSONObject;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Component
public class TGUtil {

    private String appid = "94575";
    private String appHash = "a3406de8d171bb422bb6ddf3bbd800e2";
    private static final Pattern UNICODE_PATTERN = Pattern.compile("\\\\x([0-9a-fA-F]{2})");

    @Autowired
    private RedisCache redisCache;


    public String getFilePath(String methodName) {
        String pythonScriptPath = "ruoyi-system/src/main/python/";
        String executablePath = System.getProperty("user.dir");
        return executablePath + "/" + pythonScriptPath + methodName + ".py";
    }

    public String getSession(String name) {
        String executablePath = System.getProperty("user.dir");
        return executablePath + "/" + name + ".session";
    }

    public String decodeJson(String json) {
        Matcher matcher = UNICODE_PATTERN.matcher(json);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            char c = (char) Integer.parseInt(matcher.group(1), 16);
            matcher.appendReplacement(sb, String.valueOf(c));
        }
        matcher.appendTail(sb);
        return new String(sb.toString().getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
    }

    public String GenerateCommand(String methodName, HashMap parms) throws InterruptedException, IOException {
        StringBuilder cmd = new StringBuilder();
        if (StringUtils.equals(methodName, "sendPhoneCode")) {
            File file = new File(getSession((String) parms.get("phoneNumber")));
            if (file.exists()) {
                file.delete();
            }
            while (file.exists()) {
                Thread.sleep(1000);
                log.info("等待session 文件删除");
            }
            cmd.append("python ").append(getFilePath(methodName) + " ").append(appid + " ").append(appHash + " ").append(parms.get("phoneNumber") + " ");
        } else if (StringUtils.equals(methodName, "loginByPhone")) {
            cmd.append("python ").append(getFilePath(methodName) + " ").append(appid + " ").append(appHash + " ").append(parms.get("phoneNumber") + " ").append(parms.get("codeNumber") + " ").append(parms.get("codeHash"));
            return  login(cmd.toString(),parms.get("phoneNumber").toString());
        } else if (StringUtils.equals(methodName, "loginByPhoneAndPassword")) {
            cmd.append("python ").append(getFilePath(methodName) + " ").append(appid + " ").append(appHash + " ").append(parms.get("phoneNumber") + " ").append(parms.get("codeNumber") + " ").append(parms.get("password") + " ").append(parms.get("codeHash"));
            return  login(cmd.toString(),parms.get("phoneNumber").toString());
        } else if (StringUtils.equals(methodName, "sendMessage")) {
            cmd.append("python ").append(getFilePath(methodName) + " ").append(appid + " ").append(appHash + " ").append(parms.get("sessionPath") + " ").append(parms.get("targetUser") + " ").append(parms.get("message")+" ").append(StringUtils.isEmpty((String) parms.get("filePath"))?"None":parms.get("filePath"));
        }else if (StringUtils.equals(methodName, "forWordMessage")) {
            cmd.append("python ").append(getFilePath(methodName) + " ").append(appid + " ").append(appHash + " ").append(parms.get("sessionPath") + " ").append(parms.get("targetUser") + " ").append(parms.get("messageId")+" ").append(parms.get("charId")+" ");
        }else if(StringUtils.equals(methodName,"sendMessageChannel")){
            cmd.append("python ").append(getFilePath(methodName)+" ").append(appid+" ").append(appHash+" ").append(parms.get("sessionPath")+" ").append(parms.get("targetUser")+" ").append(parms.get("message")+" ").append(StringUtils.isEmpty((String) parms.get("filePath"))?"None":parms.get("filePath"));
        }else if(StringUtils.equals(methodName,"forWordMessageChannel")){
            cmd.append("python ").append(getFilePath(methodName)+" ").append(appid+" ").append(appHash+" ").append(parms.get("sessionPath")+" ").append(parms.get("targetUser")+" ").append(parms.get("messageId")+" ").append(parms.get("charId")+" ");
        }
         if (StringUtils.equals(methodName, "loginBySessionFile")) {
            cmd.append("python ").append(getFilePath(methodName) + " ").append(appid + " ").append(appHash + " ").append(parms.get("fileName") + " ");
             return  login(cmd.toString(),parms.get("fileName").toString());
        } else if (StringUtils.equals(methodName, "addContact")) {
            cmd.append("python ").append(getFilePath(methodName) + " ").append(appid + " ").append(appHash + " ").append(parms.get("sessionPath") + " ").append(parms.get("userName"));
        } else if (StringUtils.equals(methodName, "addContactTgPhone")) {
            cmd.append("python ").append(getFilePath(methodName) + " ").append(appid + " ").append(appHash + " ").append(parms.get("sessionPath") + " ").append(parms.get("phone"));
        } else if (StringUtils.equals(methodName, "syncContact")) {
            cmd.append("python ").append(getFilePath(methodName) + " ").append(appid + " ").append(appHash + " ").append(parms.get("sysAccountStringSession") + " ");
        } else if (StringUtils.equals(methodName, "syncGroup")) {
            cmd.append("python ").append(getFilePath(methodName) + " ").append(appid + " ").append(appHash + " ").append(parms.get("sysAccountStringSession") + " ");
        } else if (StringUtils.equals(methodName, "getGroupMember")) {
            cmd.append("python ").append(getFilePath(methodName) + " ").append(appid + " ").append(appHash + " ").append(parms.get("sysAccountStringSession") + " ").append(parms.get("sysGroupId"));
        } else if (StringUtils.equals(methodName, "InvoteGroup")) {
            cmd.append("python ").append(getFilePath(methodName) + " ").append(appid + " ").append(appHash + " ").append(parms.get("sessionString") + " ").append(parms.get("channelId") + " ").append(parms.get("sysContactUserName"));
        } else if (StringUtils.equals(methodName, "joinGroup")) {
            cmd.append("python ").append(getFilePath(methodName) + " ").append(appid + " ").append(appHash + " ").append(parms.get("sessionString") + " ").append(parms.get("link") + " ");
        } else if (StringUtils.equals(methodName, "syncAccount")) {
            cmd.append("python ").append(getFilePath(methodName) + " ").append(appid + " ").append(appHash + " ").append(parms.get("sessionString") + " ");
        } else if (StringUtils.equals(methodName, "updateProFile")) {
            cmd.append("python ").append(getFilePath(methodName) + " ").append(appid + " ").append(appHash + " ").append(parms.get("sessionString") + " ").append(parms.get("firstName") + " ").append(parms.get("lastName") + " ").append(parms.get("about") + " ").append(parms.get("userName") + " ");
        }else if (StringUtils.equals(methodName, "updateUserName")) {
            cmd.append("python ").append(getFilePath(methodName) + " ").append(appid + " ").append(appHash + " ").append(parms.get("sessionString") + " ").append(parms.get("firstName") + " ").append(parms.get("lastName") + " ").append(parms.get("about") + " ").append(parms.get("userName") + " ");
        }
        return execCmd(cmd.toString());
    }

    public String login(String cmd,String key) throws InterruptedException, IOException {
        redisCache.setCacheObject(key,"",60,TimeUnit.SECONDS);
        Process p = null;
        try {
            p = Runtime.getRuntime().exec(cmd);

        } catch (IOException e) {
            e.printStackTrace();

        }
        while (StringUtils.equals(redisCache.getCacheObject(key).toString(),"")){
            Thread.sleep(3000);
        }
        String result =redisCache.getCacheObject(key).toString();
        log.info(result);
        redisCache.deleteObject(key);
        return  result;
    }

    public static Boolean isJsonString(String json){
        try {
            new JSONObject(json);
            return  true;
        }catch (Exception e){
            return  false;
        }
    }

    public String execCmd(String cmd) {
        log.info("执行python 文件参数：{}",cmd);
        StringBuilder cmdResult = new StringBuilder();
        Process p = null;
        try {
            p = Runtime.getRuntime().exec(cmd);
            InputStream fis = p.getInputStream();
            InputStreamReader isr = new InputStreamReader(fis, Charset.forName("gbk"));
            BufferedReader br = new BufferedReader(isr);
            String line = null;
            while ((line = br.readLine()) != null ) {
                cmdResult.append(line).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();

        }
        log.info("python 文件执行结果为：{}",cmdResult.toString());
        return cmdResult.toString();
    }

}
