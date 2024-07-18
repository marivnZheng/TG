package com.ruoyi.system.runnable;

import com.alibaba.fastjson2.JSON;
import com.ruoyi.common.domain.MyJobDetail;
import com.ruoyi.common.mapper.MyJobMapper;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.util.TGUtil;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class addContactRunnable implements Runnable {

    private String parms;


    private MyJobDetail myJobDetail;

    private MyJobMapper myJobMapper;

    private boolean lastFlag;


    public addContactRunnable(String parms, MyJobDetail myJobDetail, MyJobMapper myJobMapper,Boolean lastFlag) {
        this.parms = parms;
        this.myJobDetail = myJobDetail;
        this.myJobMapper =myJobMapper;
        this.lastFlag=lastFlag;
    }

    public addContactRunnable() {

    }
    @Override
    public void run() {
        TGUtil tgUtil = new TGUtil();
        HashMap map = JSON.parseObject(parms,HashMap.class);
        map.put("userName", StringUtils.isEmpty(myJobDetail.getTarg())?myJobDetail.getTargId():myJobDetail.getTarg());
        try {
           String result = tgUtil.GenerateCommand("addContact",map);
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
}
