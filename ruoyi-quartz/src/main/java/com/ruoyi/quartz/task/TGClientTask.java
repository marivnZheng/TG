package com.ruoyi.quartz.task;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.ruoyi.common.domain.MyJobDetail;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;

import com.ruoyi.common.domain.MyJob;
import com.ruoyi.common.mapper.MyJobMapper;
import com.ruoyi.system.domain.SysAccount;
import com.ruoyi.system.domain.SysContact;
import com.ruoyi.system.domain.SysGroup;
import com.ruoyi.system.mapper.SysAccountMapper;
import com.ruoyi.system.mapper.SysContactMapper;
import com.ruoyi.system.mapper.SysGroupMapper;
import com.ruoyi.system.util.TGUtil;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Calendar;

/**
 * //进行具体业务逻辑编写
 */



public class TGClientTask implements Job {


    @Autowired
    private Scheduler scheduler;

    @Autowired
    private MyJobMapper myJobMapper;

    @Autowired
    private  SysContactMapper sysContactMapper;

    @Autowired
    private SysGroupMapper sysGroupMapper;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {

        //获取到主函数中传递来的jobDetail
        JobDetail jobDetail = context.getJobDetail();
        //获取到jobDetail中传递来的参数
        JSONArray sendSource =(JSONArray)jobDetail.getJobDataMap().get("sendSource");
        JSONArray targetSource =(JSONArray)jobDetail.getJobDataMap().get("targetSource");
        String filePath = jobDetail.getJobDataMap().getString("filePath");
        String message = jobDetail.getJobDataMap().getString("message");
        String min = jobDetail.getJobDataMap().getString("min");
        Integer minCount = jobDetail.getJobDataMap().getInt("minCount");
        String startTime = jobDetail.getJobDataMap().getString("startTime");
        String endTime = jobDetail.getJobDataMap().getString("endTime");
        String endDate = jobDetail.getJobDataMap().getString("endDate");
        String selectOption = jobDetail.getJobDataMap().getString("selectOption");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        String group = jobDetail.getJobDataMap().getString("group");
        Map jobIds =(Map) jobDetail.getJobDataMap().get("jobIds");
        try {
            if(!StringUtils.isEmpty(startTime)&&!StringUtils.isEmpty(endTime)){
                Date startTime1 = dateFormat.parse(startTime);
                Date endTime1= dateFormat.parse(endTime);
                //区间内不执行
                if(isEffectiveDate(DateUtils.getNowDate(),startTime1,endTime1) ){
                    return;
                }
            }
            //关闭执行器
            if(!StringUtils.isEmpty(endDate)){
                if(dateFormat.parse(endDate).getTime()-DateUtils.getNowDate().getTime()<0){
                    JobDetail job = JobBuilder.newJob(TGClientTask.class)
                            .withIdentity("TGClientTask", group)
                            .build();
                    scheduler.pauseJob(job.getKey());
                    return;

                }
            }

        } catch (ParseException | SchedulerException e) {
            e.printStackTrace();
        }
        int count=0;
        TGUtil tgUtil = new TGUtil ();
        String executablePath =StringUtils.isEmpty(filePath)?"": System.getProperty("user.dir")+"/"+filePath;
        for (int i = 0; i < targetSource.size(); i++) {
            count++;

            Object contactId  =targetSource.get(i);
            for (int n = 0; n < sendSource.size(); n++) {
                JSONObject json = sendSource.getJSONObject(n);
                String session = (String) json.get("sysAccountStringSession");
                HashMap map = new HashMap();
                map.put("sessionPath",session);
                map.put("targetUser",contactId);
                map.put("filePath",executablePath);
                map.put("message",message);
                try {
                    String result="";
                    if(selectOption.equals("0")){
                        result=tgUtil.GenerateCommand("sendMessage",map);
                    }else {
                        result=tgUtil.GenerateCommand("sendMessageChannel",map);
                    }
                    Map resultMap = JSON.parseObject(result, Map.class);
                    if(resultMap.get("code").equals("200")){
                        MyJobDetail myJobDetail = new MyJobDetail();
                        if(selectOption.equals("0")){
                            SysContact sysAccount = sysContactMapper.selectSysContactBySysContactId(Long.valueOf(contactId.toString()));
                            myJobDetail.setTarg(sysAccount.getSysContactName());
                        }else{
                            SysGroup sysGroup = sysGroupMapper.selectSysGroupBySysGroupId(Long.valueOf((contactId.toString())));
                            myJobDetail.setTarg(String.valueOf(sysGroup.getSysGroupTitle()));
                        }
                        Integer accountId = (Integer) json.get("sysAccountId");
                        long jobId = (Long) jobIds.get(accountId);
                        myJobDetail.setJobId(jobId);
                        myJobDetail.setJobDetailDate(DateUtils.getNowDate());
                        myJobDetail.setJobDetailStatus(0);
                        if(count==targetSource.size()){
                            myJobMapper.updateMyJobAndStatus(jobId);
                        }else{
                            myJobMapper.updateMyJob(jobId);
                        }
                        myJobMapper.insertMyJobDetail(myJobDetail);
                    }else{
                        MyJobDetail myJobDetail = new MyJobDetail();
                        if(selectOption.equals("0")){

                            SysContact sysAccount = sysContactMapper.selectSysContactBySysContactId(Long.valueOf(contactId.toString()));
                            myJobDetail.setTarg(sysAccount.getSysContactName());
                        }else{
                            SysGroup sysGroup = sysGroupMapper.selectSysGroupBySysGroupId(Long.valueOf(contactId.toString()));
                            myJobDetail.setTarg(String.valueOf(sysGroup.getSysGroupTitle()));
                        }
                        Integer accountId = (Integer) json.get("sysAccountId");
                        long jobId = (Long) jobIds.get(accountId);
                        myJobDetail.setJobId(jobId);
                        myJobDetail.setJobDetailDate(DateUtils.getNowDate());
                        myJobDetail.setJobDetailStatus(1);
                        myJobDetail.setMsg((String) resultMap.get("msg"));
                        if(count==targetSource.size()){
                            myJobMapper.updateMyJobAndStatusFail(jobId);
                        }else{
                            myJobMapper.updateMyJobFail(jobId);
                        }
                        myJobMapper.insertMyJobDetail(myJobDetail);
                    }
                } catch (InterruptedException | IOException e) {
                    e.printStackTrace();
                }
            }
            if("0".equals(min)){
                try {
                    Thread.sleep(minCount*1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }else {
                try {
                    Thread.sleep(minCount*1000*60);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }



        }

    public static boolean isEffectiveDate(Date nowTime, Date startTime, Date endTime) {
        if (nowTime.getTime() == startTime.getTime()
                || nowTime.getTime() == endTime.getTime()) {
            return true;
        }

        Calendar date = Calendar.getInstance();
        date.setTime(nowTime);

        Calendar begin = Calendar.getInstance();
        begin.setTime(startTime);

        Calendar end = Calendar.getInstance();
        end.setTime(endTime);

        return date.after(begin) && date.before(end);
    }


    }

