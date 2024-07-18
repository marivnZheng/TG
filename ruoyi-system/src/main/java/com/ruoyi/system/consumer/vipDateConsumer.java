package com.ruoyi.system.consumer;


import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.system.mapper.SysUserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.List;

@Slf4j
@Configuration
@EnableScheduling
public class vipDateConsumer {

    @Autowired
    private SysUserMapper sysUserMapper;

    @Scheduled(cron = "0 0 0 * * ?")
    public  void execute(){
        //查询所有会员
        log.info("开始扣减会员时间");
        List<SysUser> sysUsers = sysUserMapper.selectVipUser();
        for(SysUser sysUser:sysUsers){
            //判断是否有剩余时间
            log.info("开始扣减会员时间,当前用户为：{}，等级是:{},剩余：{}天",sysUser.getUserName(),sysUser.getAccountDetailId(),sysUser.getVipDate());
            if(sysUser.getVipDate()-1>=0){
                sysUserMapper.updateUserVipDate(sysUser.getUserId(),sysUser.getVipDate()-1);
            }else{
                sysUserMapper.updateUserVip(sysUser.getUserId());
            }

        }
    }
}
