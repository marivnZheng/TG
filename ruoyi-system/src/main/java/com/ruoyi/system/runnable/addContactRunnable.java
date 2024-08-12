package com.ruoyi.system.runnable;

import com.alibaba.fastjson2.JSON;
import com.ruoyi.common.domain.MyJob;
import com.ruoyi.common.domain.MyJobDetail;
import com.ruoyi.common.mapper.MyJobMapper;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.mapper.SysTaskMapper;
import com.ruoyi.system.util.TGUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Slf4j
public class addContactRunnable implements Runnable {

    private String parms;


    private MyJobDetail myJobDetail;

    private MyJobMapper myJobMapper;

    private boolean lastFlag;

    private MyJob myJob;

    private  TGUtil tgUtil;

    public addContactRunnable(String parms, MyJobDetail myJobDetail, MyJobMapper myJobMapper,Boolean lastFlag,MyJob myJob,TGUtil tgUtil) {
        this.parms = parms;
        this.myJobDetail = myJobDetail;
        this.myJobMapper =myJobMapper;
        this.lastFlag=lastFlag;
        this.myJob=myJob;
        this.tgUtil=tgUtil;
    }

    public addContactRunnable() {

    }
    @Override
    public void run() {
        HashMap map = JSON.parseObject(parms,HashMap.class);
        map.put("userName", StringUtils.isEmpty(myJobDetail.getTarg())?myJobDetail.getTargId():myJobDetail.getTarg());
        try {
           String result = tgUtil.GenerateCommand("addContact",map);
            Map resultMap = JSON.parseObject(result, Map.class);
            if(resultMap.get("code").equals("200")){
                myJobDetail.setJobDetailDate(DateUtils.getNowDate());
                myJobDetail.setJobDetailStatus(0);
                countNextPlanDate();
                if(lastFlag){
                    myJobMapper.updateMyJobAndStatus(myJobDetail.getJobId());
                }else{
                    myJobMapper.updateMyJob(myJobDetail.getJobId());
                }
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
                if(lastFlag){
                    myJobMapper.updateMyJobAndStatusFail(myJobDetail.getJobId());
                }else{
                    myJobMapper.updateMyJobFail(myJobDetail.getJobId());
                }
                myJobMapper.insertMyJobDetail(myJobDetail);
            }
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
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
