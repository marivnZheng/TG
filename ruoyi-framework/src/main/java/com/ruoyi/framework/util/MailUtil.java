package com.ruoyi.framework.util;

import com.ruoyi.common.core.domain.AjaxResult;
import org.springframework.stereotype.Component;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;
import java.util.Random;


@Component
public class MailUtil {

    public  AjaxResult sendMailGetCode(String sendTo){
        String mail ="jlongtgtg@163.com";
        String username ="jlongtgtg@163.com";
        String passWord ="WNIYXAEUUFPUSSVW";
        String host = "smtp.163.com";
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.ssl.enable", "true");
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", "465 ");
        // 获取 Session 实例
        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, passWord);
                    }
                });
        try {
            session.setDebug(true);
            // 创建一个默认的 MimeMessage 对象
            Message message = new MimeMessage(session);

            // Set From: 头部字段
            message.setFrom(new InternetAddress(mail));

            // Set To: 头部字段
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(sendTo));

            Random random = new Random();
            // 生成一个范围在 100000 到 999999 之间的随机数
            int randomNumber = 100000 + random.nextInt(900000);

            int code= randomNumber;
            // Set Subject: 头部字段
            message.setSubject("注册验证码");

            // 设置消息体
            message.setText("【九龙TG】验证码为"+code+"您正在注册平台账号，请勿将验证码告诉他人并确定是您本人操作");
            // 发送消息
            Transport.send(message);
            return  AjaxResult.success("发送成功",code);
        } catch (MessagingException e) {
            return  AjaxResult.success("请求失败");
        }
    }
}
