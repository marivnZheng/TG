package com.ruoyi.system.runnable;

import com.alibaba.fastjson2.JSON;
import static com.ruoyi.common.utils.SecurityUtils.getLoginUser;
import com.ruoyi.common.domain.MyJob;
import com.ruoyi.common.domain.MyJobDetail;
import com.ruoyi.common.mapper.MyJobMapper;
import com.ruoyi.common.utils.DateUtils;

import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.domain.SysAccount;
import com.ruoyi.system.util.TGUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Slf4j
public class JoinGroupRunnable implements Runnable {

    private String parms;

    private MyJobDetail myJobDetail;

    private MyJobMapper myJobMapper;

    private boolean lastFlag;

    private MyJob myJob;

    public JoinGroupRunnable(String parms,MyJobDetail myJobDetail,MyJobMapper myJobMapper,Boolean lastFlag,MyJob myJob) {
        this.parms = parms;
        this.myJobDetail=myJobDetail;
        this.myJobMapper=myJobMapper;
        this.lastFlag=lastFlag;
        this.myJob=myJob;
    }

    public JoinGroupRunnable() {

    }
    @Override
    public void run() {
        TGUtil tgUtil = new TGUtil();
        HashMap map = JSON.parseObject(parms, HashMap.class);
        try {
            map.put("link", StringUtils.isNotEmpty(myJobDetail.getTarg())?myJobDetail.getTarg():myJobDetail.getTargId());
            String result=tgUtil.GenerateCommand("joinGroup",map);
            String s = result.replaceAll("\\\\", "_");
            if(!TGUtil.isJsonString(s)){
                myJobDetail.setJobDetailDate(DateUtils.getNowDate());
                waitTask();
                myJobDetail.setJobDetailStatus(1);
                myJobDetail.setMsg("未知错误");
                myJobMapper.insertMyJobDetail(myJobDetail);
                return;
            }
            Map resultMap = JSON.parseObject(s, Map.class);
            if(resultMap.get("code").equals("200")){
                myJobDetail.setJobDetailDate(DateUtils.getNowDate());
                waitTask();
                myJobDetail.setJobDetailStatus(0);
                myJobMapper.updateMyJob(myJobDetail.getJobId());
                myJobMapper.insertMyJobDetail(myJobDetail);
            }else{
                myJobDetail.setJobDetailDate(DateUtils.getNowDate());
                waitTask();
                myJobDetail.setJobDetailStatus(1);
                myJobDetail.setMsg((String) resultMap.get("msg"));
                myJobMapper.updateMyJobFail(myJobDetail.getJobId());
                myJobMapper.insertMyJobDetail(myJobDetail);
            }
        }catch (Exception e){
            myJobDetail.setJobDetailDate(DateUtils.getNowDate());
            waitTask();
            myJobDetail.setJobDetailStatus(1);
            myJobDetail.setMsg("未知错误");
            myJobDetail.setJobDetailDate(DateUtils.getNowDate());
            myJobMapper.insertMyJobDetail(myJobDetail);
            log.error("发送信息发生错误，错误原因{}",e.fillInStackTrace());

        }


        }

    private void waitTask(){
        int  intervals=Integer.valueOf(myJob.getIntervals());
        String intervalsUnit = String.valueOf(myJob.getIntervalsUnit()) ;
        try {
            if(intervalsUnit.equals("0")){
                TimeUnit.SECONDS.sleep(intervals);
            }else{
                TimeUnit.MINUTES.sleep(intervals);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

}

