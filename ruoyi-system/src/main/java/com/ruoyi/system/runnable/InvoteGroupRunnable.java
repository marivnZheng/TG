package com.ruoyi.system.runnable;

import com.alibaba.fastjson2.JSON;
import com.ruoyi.common.domain.MyJob;
import com.ruoyi.common.domain.MyJobDetail;
import com.ruoyi.common.mapper.MyJobMapper;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.system.mapper.SysTaskMapper;
import com.ruoyi.system.util.TGUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class InvoteGroupRunnable implements Runnable {

    private String parms;


    private MyJobDetail myJobDetail;

    private MyJobMapper myJobMapper;

    private boolean lastFlag;

    private MyJob myJob;

    private TGUtil tgUtil;

    public InvoteGroupRunnable(String parms, MyJobDetail myJobDetail, MyJobMapper myJobMapper,Boolean lastFlag,MyJob myJob,TGUtil tgUtil) {
        this.parms = parms;
        this.myJobDetail = myJobDetail;
        this.myJobMapper =myJobMapper;
        this.lastFlag=lastFlag;
        this.myJob=myJob;
        this.tgUtil=tgUtil;
    }

    public InvoteGroupRunnable() {

    }
    @Override
    public void run() {
        HashMap map = JSON.parseObject(parms,HashMap.class);
        map.put("sysContactUserName",myJobDetail.getTargId());
        try {
           String result = tgUtil.GenerateCommand("InvoteGroup",map);
            Map resultMap = JSON.parseObject(result, Map.class);
            if(resultMap.get("code").equals("200")){
                myJobDetail.setJobDetailDate(DateUtils.getNowDate());
                myJobDetail.setJobDetailStatus(0);
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
}
