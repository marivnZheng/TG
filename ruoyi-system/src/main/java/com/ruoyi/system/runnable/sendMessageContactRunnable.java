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

import java.util.*;
import java.util.concurrent.TimeUnit;


@Slf4j
public class sendMessageContactRunnable implements Runnable {


    private String parms;

    private MyJobDetail myJobDetail;

    private MyJobMapper myJobMapper;

    private boolean lastFlag;

    private MyJob myJob;

    private  TGUtil tgUtil;

    public sendMessageContactRunnable(String parms, MyJobDetail myJobDetail, MyJobMapper myJobMapper,Boolean lastFlag,MyJob myJob,TGUtil tgUtil) {
        this.parms = parms;
        this.myJobDetail = myJobDetail;
        this.myJobMapper =myJobMapper;
        this.lastFlag=lastFlag;
        this.myJob=myJob;
        this.tgUtil=tgUtil;
    }
    public sendMessageContactRunnable() {

    }
    @Override
    public void run() {
        HashMap map = JSON.parseObject(parms, HashMap.class);
        String forWordMessage = (String) map.get("forWordMessage");
        //进入转发逻辑
        try {
            //获取消息组
            Boolean isVip = (Boolean) map.get("isVip");
            List<Map> messageGroupList= (List<Map>) map.get("messageGroup");
            Integer sendIndex = (Integer) map.get("sendIndex");
            Map messageGroup = messageGroupList.get(sendIndex);
            String executablePath = StringUtils.isEmpty((String) messageGroup.get("fileName"))?"": System.getProperty("user.dir")+"/"+messageGroup.get("fileName");
            map.put("filePath",executablePath);
            map.put("targetUser",StringUtils.isNotEmpty(myJobDetail.getTarg())?myJobDetail.getTarg():myJobDetail.getTargId());
            if(isVip){
                map.put("message",StringUtils.isEmpty((String) messageGroup.get("message"))?"None":messageGroup.get("message"));
            }else{
                map.put("message",StringUtils.isEmpty((String) messageGroup.get("message"))?"None":messageGroup.get("message")+"\n \n  <b><a href=\"http://l999999999.com\">欢迎使用九龙Telegram群发测试版</a></b>");
            }
            String result="";
            if(StringUtils.isNotEmpty(forWordMessage)){
                //"https://t.me/sosoqun567/79659"
                String[] split = forWordMessage.split("/");
                String messageId=split[split.length-1];
                String charId =split[split.length-2];
                map.put("charId",charId);
                map.put("messageId",messageId);
                result=tgUtil.GenerateCommand("forWordMessage",map);
            }else{
                result=tgUtil.GenerateCommand("sendMessage",map);
            }

            String s = result.replaceAll("\\\\", "_");
            if(!TGUtil.isJsonString(s)){
                log.info("执行结果为：{}",result);
                myJobDetail.setJobDetailDate(DateUtils.getNowDate());
                myJobDetail.setJobDetailStatus(1);
                myJobDetail.setMsg("未知错误");
                myJobMapper.insertMyJobDetail(myJobDetail);
                return;
            }
            Map resultMap = JSON.parseObject(s, Map.class);
            if(resultMap.get("code").equals("200")){
                myJobDetail.setJobDetailDate(DateUtils.getNowDate());
                myJobDetail.setJobDetailStatus(0);
                countNextPlanDate();
                if(lastFlag){
                    //最后一组数据
                    if(messageGroupList.size()==sendIndex+1){
                        //判断是否循环，设置下次进行计划时间
                        map.put("sendIndex",0);
                        String parmsString =JSON.toJSONString(map);
                        MyJob myJob = myJobMapper.selectJobById(myJobDetail.getJobId());
                        myJob.setParms(parmsString);
                        myJob.setSuccessNum(myJob.getSuccessNum()+1);
                        //计算下次时间
                        countPlanDate(myJob);
                        myJobMapper.insertMyJob(myJob);
                        myJobMapper.updateMyJobAndStatusFail(myJobDetail.getJobId());
                    }else{
                        map.put("sendIndex",sendIndex+1);
                        String parmsString =JSON.toJSONString(map);
                        MyJob myJob = myJobMapper.selectJobById(myJobDetail.getJobId());
                        //计算下次时间
                        countPlanDate(myJob);
                        myJobMapper.updateMyJobMessageGroup(parmsString,myJobDetail.getJobId());
                        myJobMapper.updateMyJobFail(myJobDetail.getJobId());
                    }
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
                if(lastFlag){         //最后一组数据
                    if(messageGroupList.size()==sendIndex+1){
                        //判断是否循环，设置下次进行计划时间
                        map.put("sendIndex",0);
                        MyJob myJob = myJobMapper.selectJobById(myJobDetail.getJobId());
                        countPlanDate(myJob);
                        myJobMapper.updateMyJobAndStatusFail(myJobDetail.getJobId());
                    }else{
                        map.put("sendIndex",sendIndex+1);
                        String parmsString =JSON.toJSONString(map);
                        MyJob myJob = myJobMapper.selectJobById(myJobDetail.getJobId());
                        countPlanDate(myJob);
                        myJobMapper.updateMyJobMessageGroup(parmsString,myJobDetail.getJobId());
                        myJobMapper.updateMyJobFail(myJobDetail.getJobId());
                    }
                }else{
                    myJobMapper.updateMyJobFail(myJobDetail.getJobId());
                }
                myJobMapper.insertMyJobDetail(myJobDetail);
            }
        }catch (Exception e){
            myJobDetail.setJobDetailDate(DateUtils.getNowDate());
            myJobDetail.setJobDetailStatus(1);
            myJobDetail.setMsg("未知错误");
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



    //计算下次循环时间
    private void countPlanDate(MyJob myJob){
        //计算下次时间
        if(StringUtils.equals(myJob.getJobType(),"2")){
            String intervalLoop = myJob.getIntervalLoop();
            int intervalLoopUnit = myJob.getIntervalLoopUnit();
            Date nowDate = DateUtils.getNowDate();
            if(intervalLoopUnit==1){
                //循环间隔为分钟
                Date date = DateUtils.addMinutes(nowDate, Integer.valueOf(intervalLoop));
                myJob.setPlanDate(date);
            }else if(intervalLoopUnit==2){
                //循环间隔为小时
                Date date = DateUtils.addHours(nowDate, Integer.valueOf(intervalLoop));
                myJob.setPlanDate(date);
            }
        }
        myJobMapper.insertMyJob(myJob);

    }
}
