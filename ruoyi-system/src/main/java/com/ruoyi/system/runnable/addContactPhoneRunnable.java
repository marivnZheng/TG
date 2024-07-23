package com.ruoyi.system.runnable;

import com.alibaba.fastjson2.JSON;
import com.ruoyi.common.domain.MyJob;
import com.ruoyi.common.domain.MyJobDetail;
import com.ruoyi.common.mapper.MyJobMapper;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.util.TGUtil;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class addContactPhoneRunnable implements Runnable {


    private String parms;


    private MyJobDetail myJobDetail;

    private MyJobMapper myJobMapper;

    private boolean lastFlag;

    private MyJob myJob;

    public addContactPhoneRunnable(String parms, MyJobDetail myJobDetail, MyJobMapper myJobMapper,Boolean lastFlag,MyJob myJob) {
        this.parms = parms;
        this.myJobDetail = myJobDetail;
        this.myJobMapper =myJobMapper;
        this.lastFlag=lastFlag;
        this.myJob=myJob;
    }
    public addContactPhoneRunnable() {

    }
    @Override
    public void run() {
        TGUtil tgUtil = new TGUtil();
        HashMap map = JSON.parseObject(parms,HashMap.class);
        map.put("userName", StringUtils.isEmpty(myJobDetail.getTarg())?myJobDetail.getTargId():myJobDetail.getTarg());
        try {
            String result = tgUtil.GenerateCommand("addContactTgPhone",map);
            Map resultMap = JSON.parseObject(result, Map.class);
            if(resultMap.get("code").equals("200")){
                waitTask();
                myJobDetail.setJobDetailDate(DateUtils.getNowDate());
                myJobDetail.setJobDetailStatus(0);
                if(lastFlag){
                    myJobMapper.updateMyJobAndStatus(myJobDetail.getJobId());
                }else{
                    myJobMapper.updateMyJob(myJobDetail.getJobId());
                }
                myJobMapper.insertMyJobDetail(myJobDetail);
            }else{
                waitTask();
                myJobDetail.setJobDetailDate(DateUtils.getNowDate());
                myJobDetail.setJobDetailStatus(1);
                myJobDetail.setMsg((String) resultMap.get("msg"));
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
