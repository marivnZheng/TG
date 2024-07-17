package com.ruoyi.system.util;


import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;

import java.util.HashMap;

public class test {
    public static void main(String[] args) {
        String jsonString = "{\"sysContactUserName\": null, \"contactNumber\": \"6281370216300\", \"sysMutualContact\": \"1\", \"stutas\": \"1\", \"firstname\": \"Rizky Emelia Karo\\\\\"\", \"lastname\": null, \"contactId\": \"1509840479\"}";
        String firstNameWithoutEscapes = jsonString.replace("\\\\\"", "\\\"");

        System.out.println(firstNameWithoutEscapes);

        JSONObject jsonObject = JSON.parseObject(firstNameWithoutEscapes);
        Object firstname = jsonObject.get("firstname");
        System.out.println(firstname);
    }
}
