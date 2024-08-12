package com.ruoyi.system.util;


import cn.hutool.json.JSONObject;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
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

    @Value("${python.version}")
    private String python;

    @Autowired
    private RedisCache redisCache;

    public String getPythonVersion() {
        return python;
    }

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
        ArrayList <String> cmd = new ArrayList();
        if (StringUtils.equals(methodName, "sendPhoneCode")) {
            File file = new File(getSession((String) parms.get("phoneNumber")));
            if (file.exists()) {
                file.delete();
            }
            while (file.exists()) {
                Thread.sleep(1000);
                log.info("等待session 文件删除");
            }
            cmd.add(python);
            cmd.add(getFilePath(methodName));
            cmd.add(appid);
            cmd.add(appHash);
            cmd.add((String) parms.get("phoneNumber"));
        } else if (StringUtils.equals(methodName, "loginByPhone")) {
            cmd.add(python);
            cmd.add(getFilePath(methodName));
            cmd.add(appid);
            cmd.add(appHash);
            cmd.add((String) parms.get("phoneNumber"));
            cmd.add((String) parms.get("codeNumber"));
            cmd.add(parms.get("codeHash").toString().replace("\n",""));
            String[] stringArray = cmd.toArray(new String[cmd.size()]);
            return  login(stringArray,parms.get("phoneNumber").toString());
        } else if (StringUtils.equals(methodName, "loginByPhoneAndPassword")) {
            cmd.add(python);
            cmd.add(getFilePath(methodName));
            cmd.add(appid);
            cmd.add(appHash);
            cmd.add((String) parms.get("phoneNumber"));
            cmd.add((String) parms.get("codeNumber"));
            cmd.add((String) parms.get("password"));
            cmd.add(parms.get("codeHash").toString().replace("\n",""));
            String[] stringArray = cmd.toArray(new String[cmd.size()]);
            return  login(stringArray,parms.get("phoneNumber").toString());
        } else if (StringUtils.equals(methodName, "sendMessage")) {
            cmd.add(python);
            cmd.add(getFilePath(methodName));
            cmd.add(appid);
            cmd.add(appHash);
            cmd.add((String) parms.get("sessionPath"));
            cmd.add((String) parms.get("targetUser"));
            cmd.add((String) parms.get("message"));
            cmd.add(StringUtils.isEmpty((String) parms.get("filePath"))?"None":(String)parms.get("filePath"));
        }else if (StringUtils.equals(methodName, "forWordMessage")) {
            cmd.add(python);
            cmd.add(getFilePath(methodName));
            cmd.add(appid);
            cmd.add(appHash);
            cmd.add((String) parms.get("sessionPath"));
            cmd.add((String) parms.get("targetUser"));
            cmd.add((String) parms.get("messageId"));
            cmd.add((String) parms.get("charId"));
        }else if(StringUtils.equals(methodName,"sendMessageChannel")){
            cmd.add(python);
            cmd.add(getFilePath(methodName));
            cmd.add(appid);
            cmd.add(appHash);
            cmd.add((String) parms.get("sessionPath"));
            cmd.add((String) parms.get("targetUser"));
            cmd.add((String) parms.get("message"));
            cmd.add(StringUtils.isEmpty((String) parms.get("filePath"))?"None":(String) parms.get("filePath"));
        }else if(StringUtils.equals(methodName,"forWordMessageChannel")){
            cmd.add(python);
            cmd.add(getFilePath(methodName));
            cmd.add(appid);
            cmd.add(appHash);
            cmd.add((String) parms.get("sessionPath"));
            cmd.add((String) parms.get("targetUser"));
            cmd.add((String) parms.get("messageId"));
            cmd.add((String) parms.get("charId"));
        }
        else if (StringUtils.equals(methodName, "loginBySessionFile")) {
             cmd.add(python);
             cmd.add(getFilePath(methodName));
             cmd.add(appid);
             cmd.add(appHash);
             cmd.add((String) parms.get("fileName"));
            String[] stringArray = cmd.toArray(new String[cmd.size()]);
             return  login(stringArray,parms.get("fileName").toString());
        } else if (StringUtils.equals(methodName, "addContact")) {
             cmd.add(python);
             cmd.add(getFilePath(methodName));
             cmd.add(appid);
             cmd.add(appHash);
             cmd.add((String) parms.get("sessionPath"));
             cmd.add((String) parms.get("userName"));
        } else if (StringUtils.equals(methodName, "addContactTgPhone")) {
             cmd.add(python);
             cmd.add(getFilePath(methodName));
             cmd.add(appid);
             cmd.add(appHash);
             cmd.add((String) parms.get("sessionPath"));
             cmd.add((String) parms.get("phone"));
        } else if (StringUtils.equals(methodName, "syncContact")) {
             cmd.add(python);
             cmd.add(getFilePath(methodName));
             cmd.add(appid);
             cmd.add(appHash);
             cmd.add((String) parms.get("sysAccountStringSession"));
        } else if (StringUtils.equals(methodName, "syncGroup")) {
             cmd.add(python);
             cmd.add(getFilePath(methodName));
             cmd.add(appid);
             cmd.add(appHash);
             cmd.add((String) parms.get("sysAccountStringSession"));
        } else if (StringUtils.equals(methodName, "getGroupMember")) {
             cmd.add(python);
             cmd.add(getFilePath(methodName));
             cmd.add(appid);
             cmd.add(appHash);
             cmd.add((String) parms.get("sysAccountStringSession"));
             cmd.add(parms.get("sysGroupId").toString());
        } else if (StringUtils.equals(methodName, "InvoteGroup")) {
             cmd.add(python);
             cmd.add(getFilePath(methodName));
             cmd.add(appid);
             cmd.add(appHash);
             cmd.add((String) parms.get("sessionString"));
             cmd.add((String) parms.get("channelId"));
             cmd.add((String) parms.get("sysContactUserName"));
        } else if (StringUtils.equals(methodName, "joinGroup")) {
             cmd.add(python);
             cmd.add(getFilePath(methodName));
             cmd.add(appid);
             cmd.add(appHash);
             cmd.add((String) parms.get("sessionString"));
             cmd.add((String) parms.get("link"));
        } else if (StringUtils.equals(methodName, "syncAccount")) {
             cmd.add(python);
             cmd.add(getFilePath(methodName));
             cmd.add(appid);
             cmd.add(appHash);
             cmd.add((String) parms.get("sessionString"));
        } else if (StringUtils.equals(methodName, "updateProFile")) {
             cmd.add(python);
             cmd.add(getFilePath(methodName));
             cmd.add(appid);
             cmd.add(appHash);
             cmd.add(parms.get("sessionString")==null?"":(String) parms.get("sessionString"));
             cmd.add(parms.get("firstName")==null?"":(String) parms.get("firstName"));
             cmd.add(parms.get("lastName")==null?"":(String) parms.get("lastName"));
             cmd.add(parms.get("about")==null?"":(String) parms.get("about"));
             cmd.add(parms.get("userName")==null?"":(String) parms.get("userName"));
        }else if (StringUtils.equals(methodName, "updateUserName")) {
            cmd.add(python);
            cmd.add(getFilePath(methodName));
            cmd.add(appid);
            cmd.add(appHash);
            cmd.add(parms.get("sessionString")==null?"":(String) parms.get("sessionString"));
            cmd.add(parms.get("firstName")==null?"":(String) parms.get("firstName"));
            cmd.add(parms.get("lastName")==null?"":(String) parms.get("lastName"));
            cmd.add(parms.get("about")==null?"":(String) parms.get("about"));
            cmd.add(parms.get("userName")==null?"":(String) parms.get("userName"));
        }
        String[] stringArray = cmd.toArray(new String[cmd.size()]);
        return execCmd(stringArray);
    }

    public String login(String [] cmd,String key) throws InterruptedException, IOException {
        log.info("执行python 文件参数：{}", Arrays.toString(cmd));
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
        log.info("python 文件执行结果为：{}",result.toString());
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

    public String execCmd(String [] cmd) {
        log.info("执行python 文件参数：{}", Arrays.toString(cmd));
        StringBuilder cmdResult = new StringBuilder();
        try {
            Process process = Runtime.getRuntime().exec(cmd);
            InputStream fis = process.getInputStream();
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
