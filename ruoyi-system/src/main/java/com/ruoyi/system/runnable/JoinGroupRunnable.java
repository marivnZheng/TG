package com.ruoyi.system.runnable;

import com.alibaba.fastjson2.JSON;
import static com.ruoyi.common.utils.SecurityUtils.getLoginUser;
import com.ruoyi.common.domain.MyJob;
import com.ruoyi.common.domain.MyJobDetail;
import com.ruoyi.common.mapper.MyJobMapper;
import com.ruoyi.common.utils.DateUtils;

import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.domain.SysAccount;
import com.ruoyi.system.mapper.SysTaskMapper;
import com.ruoyi.system.util.TGUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Slf4j
public class JoinGroupRunnable implements Runnable {

    private String parms;

    private MyJobDetail myJobDetail;

    private MyJobMapper myJobMapper;

    private boolean lastFlag;

    private MyJob myJob;

    private TGUtil tgUtil;

    public JoinGroupRunnable(String parms, MyJobDetail myJobDetail, MyJobMapper myJobMapper,Boolean lastFlag,MyJob myJob,TGUtil tgUtil) {
        this.parms = parms;
        this.myJobDetail = myJobDetail;
        this.myJobMapper =myJobMapper;
        this.lastFlag=lastFlag;
        this.myJob=myJob;
        this.tgUtil=tgUtil;
    }

    public JoinGroupRunnable() {

    }
    @Override
    public void run() {
        HashMap map = JSON.parseObject(parms, HashMap.class);
        try {
            map.put("link", StringUtils.isNotEmpty(myJobDetail.getTarg())?myJobDetail.getTarg():myJobDetail.getTargId());
            String result=tgUtil.GenerateCommand("joinGroup",map);
            String s = result.replaceAll("\\\\", "_");
            if(!TGUtil.isJsonString(s)){
                myJobDetail.setJobDetailDate(DateUtils.getNowDate());
                myJobDetail.setJobDetailStatus(1);
                myJobDetail.setMsg("未知错误");
                myJobMapper.insertMyJobDetail(myJobDetail);
                return;
            }
            Map resultMap = JSON.parseObject(s, Map.class);
            if(resultMap.get("code").equals("200")){
                myJobDetail.setJobDetailDate(DateUtils.getNowDate());
                countNextPlanDate();
                myJobDetail.setJobDetailStatus(0);
                myJobMapper.updateMyJob(myJobDetail.getJobId());
                myJobMapper.insertMyJobDetail(myJobDetail);
            }else{
                if(resultMap.get("code").equals("444")){
                    log.error("该账号已经封禁");
                    myJob.setJobStatus("0");
                    myJobMapper.insertMyJob(myJob);
                    myJobDetail.setMsg("该账号已经封禁");
                }else{
                    myJobDetail.setMsg((String) resultMap.get("msg"));
                }
                myJobDetail.setJobDetailDate(DateUtils.getNowDate());
                myJobDetail.setJobDetailStatus(1);
                myJobMapper.updateMyJobFail(myJobDetail.getJobId());
                myJobMapper.insertMyJobDetail(myJobDetail);
            }
        }catch (Exception e){
            myJobDetail.setJobDetailDate(DateUtils.getNowDate());
            myJobDetail.setJobDetailStatus(1);
            myJobDetail.setMsg("未知错误");
            myJobDetail.setJobDetailDate(DateUtils.getNowDate());
            myJobMapper.insertMyJobDetail(myJobDetail);
            log.error("发送信息发生错误，错误原因{}",e.fillInStackTrace());

        }


        }


    private void countNextPlanDate(){
        Integer tarNum = myJob.getTarNum();
        int index = myJobDetail.getIndex();
        Date nextPlanDate=new Date();
        //不是最后一个任务计算下一个任务计划执行时间
        if(index<tarNum){
            String intervals = myJob.getIntervals();
            int intervalsUnit = myJob.getIntervalsUnit();
            Date nowDate = DateUtils.getNowDate();
            if(intervalsUnit==1){
                //循环间隔为分钟
                nextPlanDate  = DateUtils.addMinutes(nowDate, Integer.valueOf(intervals));
            }else if(intervalsUnit==2){
                //循环间隔为小时
                nextPlanDate = DateUtils.addHours(nowDate, Integer.valueOf(intervals));
            }else if (intervalsUnit==0){
                nextPlanDate = DateUtils.addSeconds(nowDate, Integer.valueOf(intervals));
            }
            myJobMapper.updateJobDetailStatus(nextPlanDate,myJobDetail.getJobId(),index+1);
        }

    }


}

