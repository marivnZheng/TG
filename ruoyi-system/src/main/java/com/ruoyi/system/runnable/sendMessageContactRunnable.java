package com.ruoyi.system.runnable;

import com.alibaba.fastjson2.JSON;
import com.ruoyi.common.domain.MyJob;
import com.ruoyi.common.domain.MyJobDetail;
import com.ruoyi.common.mapper.MyJobMapper;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.domain.SysContact;
import com.ruoyi.system.domain.SysGroup;
import com.ruoyi.system.util.TGUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Slf4j
public class sendMessageContactRunnable implements Runnable {


    private String parms;

    private MyJobDetail myJobDetail;

    private MyJobMapper myJobMapper;

    private boolean lastFlag;

    public sendMessageContactRunnable(String parms,MyJobDetail myJobDetail,MyJobMapper myJobMapper,Boolean lastFlag) {
        this.parms = parms;
        this.myJobDetail=myJobDetail;
        this.myJobMapper=myJobMapper;
        this.lastFlag=lastFlag;
    }

    public sendMessageContactRunnable() {

    }
    @Override
    public void run() {
        TGUtil tgUtil = new TGUtil();
        HashMap map = JSON.parseObject(parms, HashMap.class);
        String forWordMessage = (String) map.get("forWordMessage");
        //进入转发逻辑
        try {
            //获取消息组
            List<Map> messageGroupList= (List<Map>) map.get("messageGroup");
            Integer sendIndex = (Integer) map.get("sendIndex");
            Map messageGroup = messageGroupList.get(sendIndex);
            String executablePath = StringUtils.isEmpty((String) messageGroup.get("fileName"))?"": System.getProperty("user.dir")+"/"+messageGroup.get("fileName");
            map.put("filePath",executablePath);
            map.put("targetUser",StringUtils.isNotEmpty(myJobDetail.getTarg())?myJobDetail.getTarg():myJobDetail.getTargId());
            map.put("message",StringUtils.isEmpty((String) messageGroup.get("message"))?"None":messageGroup.get("message"));
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
                if(lastFlag){
                    //最后一组数据
                    if(messageGroupList.size()==sendIndex+1){
                        //判断是否循环，设置下次进行计划时间
                        map.put("sendIndex",0);
                        String parmsString =JSON.toJSONString(map);
                        MyJob myJob = myJobMapper.selectJobById(myJobDetail.getJobId());
                        myJob.setParms(parmsString);
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
                        myJob.setSuccessNum(myJob.getSuccessNum()+1);
                        myJobMapper.insertMyJob(myJob);
                        myJobMapper.updateMyJobAndStatusFail(myJobDetail.getJobId());
                    }else{
                        map.put("sendIndex",sendIndex+1);
                        String parmsString =JSON.toJSONString(map);
                        myJobMapper.updateMyJobMessageGroup(parmsString,myJobDetail.getJobId());
                        myJobMapper.updateMyJobFail(myJobDetail.getJobId());
                    }
                }else{
                    myJobMapper.updateMyJob(myJobDetail.getJobId());
                }
                myJobMapper.insertMyJobDetail(myJobDetail);
            }else{
                myJobDetail.setJobDetailDate(DateUtils.getNowDate());
                myJobDetail.setJobDetailStatus(1);
                myJobDetail.setMsg((String) resultMap.get("msg"));
                if(lastFlag){         //最后一组数据
                    if(messageGroupList.size()==sendIndex+1){
                        //判断是否循环，设置下次进行计划时间
                        map.put("sendIndex",0);
                        String parmsString =JSON.toJSONString(map);
                        MyJob myJob = myJobMapper.selectJobById(myJobDetail.getJobId());
                        myJob.setParms(parmsString);
                        //计算下次时间
                        if(messageGroupList.size()==sendIndex+1){
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
                        myJobMapper.updateMyJobAndStatusFail(myJobDetail.getJobId());
                    }else{
                        map.put("sendIndex",sendIndex+1);
                        String parmsString =JSON.toJSONString(map);
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
            log.info("发送信息发生错误，错误原因{}",e.fillInStackTrace());
        }
    }
}
