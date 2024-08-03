package com.ruoyi.system.service.impl;

import cn.hutool.core.collection.ListUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.domain.MyJob;
import com.ruoyi.common.domain.MyJobDetail;
import com.ruoyi.common.mapper.MyJobMapper;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;

import com.ruoyi.system.domain.SysAccount;
import com.ruoyi.system.domain.SysContact;
import com.ruoyi.system.domain.SysGroup;
import com.ruoyi.system.dto.TgLogin;
import com.ruoyi.system.mapper.SysAccountMapper;
import com.ruoyi.system.mapper.SysContactMapper;
import com.ruoyi.system.mapper.SysGroupMapper;
import com.ruoyi.system.mapper.SysTaskMapper;
import com.ruoyi.system.service.ISysAccountService;
import com.ruoyi.system.service.ISysTaskService;
import com.ruoyi.system.util.TGUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.ruoyi.common.core.domain.AjaxResult.error;
import static com.ruoyi.common.core.domain.AjaxResult.success;
import static com.ruoyi.common.utils.SecurityUtils.getLoginUser;

/**
 * Service业务层处理
 * 
 * @author ruoyi
 * @date 2024-05-09
 */
@Service
public class SysTaskServiceImpl implements ISysTaskService
{
    @Autowired
    private SysTaskMapper sysTaskMapper;

    @Autowired
    private TGUtil tgUtil;

    @Autowired
    private SysContactMapper sysContactMapper;

    @Autowired
    private MyJobMapper myJobMapper;



    @Autowired
    private SysGroupMapper sysGroupMapper;

    @Override
    public List<MyJob> selectSysTaskList(MyJob job) {
        job.setUserId(getLoginUser().getUserId());
        return sysTaskMapper.selectSysTaskList(job);
    }

    @Override
    public List<MyJobDetail> selectSysTaskDetailList(HashMap map) {
        return sysTaskMapper.selectSysTaskDetailList(Long.valueOf(map.get("jobId").toString()));
    }

    @Override
    public AjaxResult runTGTask(HashMap map) {
        List<String> syscontacids = (List<String>) map.get("sysConcatsList");
        String selectOption = (String) map.get("selectOption");
        List<String> groupList = (List<String>) map.get("groupList");
        String min = (String) map.get("min");
        Integer minCount = (Integer) map.get("minCount");
        Integer onceMin =  (Integer) map.get("onceMin");
        List<Map> messageGroup =  (List<Map>) map.get("messageGroup");
        String onceType = (String) map.get("onceType");
        String loopType = (String) map.get("loopType");
        Integer loopMin =map.containsKey("loopMin")?(Integer) map.get("loopMin"):null;
        String hour = (String) map.get("hour");
        Integer hourCount =map.containsKey("hourCount")?(Integer) map.get("hourCount"):null;
        List<Object> accountList = (List<Object>) map.get("accountList");
        String targActive = (String) map.get("targActive");
        String sendMethod=(String) map.get("sendMethod");
        Long accountDetailId = getLoginUser().getUser().getAccountDetailId();
        long  longTime =System.currentTimeMillis();
        int tarNum = 0;
        if(syscontacids==null&&groupList==null){
            for (Object sysAccount : accountList) {
                //发送所有好友
                if(targActive.equals("1")){
                    Map mapAccount = (Map) sysAccount;
                    Integer sysAccountId = (Integer)mapAccount.get("sysAccountId");
                    List<SysContact> sysContactList = sysContactMapper.selectSysContactBySysAccount(sysAccountId);
                    selectOption="0";
                    tarNum=sysContactList.size();
                    HashMap parms = new HashMap();
                    parms.put("sessionPath",mapAccount.get("sysAccountStringSession"));
                    parms.put("messageGroup",map.get("messageGroup"));
                    parms.put("forWordMessage",map.get("forWordMessage"));
                    parms.put("sendIndex",0);
                    parms.put("endTime",map.get("endTime"));
                    parms.put("startTime",map.get("startTime"));
                    parms.put("endDate",map.get("endDate"));
                    parms.put("endDate",map.get("endDate"));
                    parms.put("isVip",accountDetailId>1?true:false);
                    MyJob myJob = new MyJob();

                    myJob.setIntervals(onceMin+"");
                    myJob.setIntervalsUnit(Integer.valueOf(onceType));
                    if(StringUtils.isNotNull(loopMin)){
                        myJob.setIntervalLoop(loopMin.toString());
                        myJob.setIntervalLoopUnit(Integer.valueOf(loopType));
                    }
                    myJob.setJobName(StringUtils.equals(selectOption,"0")?"发送信息给用户":"发送信息给频道");
                    myJob.setJobType(StringUtils.equals(sendMethod,"1")?"1":"2");
                    myJob.setTarNum(tarNum);
                    myJob.setParms(JSON.toJSONString(parms));
                    myJob.setJobClass("com.ruoyi.system.runnable.com.ruoyi.system.runnable.sendMessageContactRunnable");
                    myJob.setUserId(getLoginUser().getUserId());
                    myJob.setCreateDate(DateUtils.getNowDate());
                    myJob.setJobGroup(String.valueOf(longTime));
                    String name = (String) mapAccount.get("TgName");
                    myJob.setSysAccountName(name);
                    myJob.setUserId(getLoginUser().getUserId());
                    myJobMapper.insertMyJob(myJob);
                    //计算子任务计划执行时间
                    int index =0;
                    ArrayList list =new ArrayList();
                    for (Map map1 : messageGroup) {
                        for (SysContact sysContact : sysContactList) {
                            MyJobDetail jobDetail = new MyJobDetail();
                            jobDetail.setTarg(sysContact.getSysContactUserName());
                            jobDetail.setTargId(String.valueOf(sysContact.getSysContactId()));
                            jobDetail.setIndex(index);
                            jobDetail.setJobId(myJob.getJobId());
                            jobDetail.setTaskClass("com.ruoyi.system.runnable.sendMessageContactRunnable");
                            jobDetail.setJobDetailStatus(-1);
                            list.add(jobDetail);
                            index++;
                        }
                    }
                    List<List<MyJobDetail>> partition = ListUtil.partition(list, 100);
                    partition.forEach(e -> myJobMapper.batchMyJobDetail(e));
                }else{
                    Map mapAccount = (Map) sysAccount;
                    String sysAccountStringSession = (String)mapAccount.get("sysAccountStringSession");
                    List<SysGroup> sysContactList =sysGroupMapper.selectSysGroupByAccountString(sysAccountStringSession);
                    //发送所有群
                    selectOption="1";
                    tarNum=sysContactList.size();
                    HashMap parms = new HashMap();
                    parms.put("sessionPath",mapAccount.get("sysAccountStringSession"));
                    parms.put("messageGroup",map.get("messageGroup"));
                    parms.put("forWordMessage",map.get("forWordMessage"));
                    parms.put("sendIndex",0);
                    parms.put("isVip",accountDetailId>1?true:false);
                    parms.put("endTime",map.get("endTime"));
                    parms.put("startTime",map.get("startTime"));
                    parms.put("endDate",map.get("endDate"));
                    MyJob myJob = new MyJob();
                    myJob.setIntervals(onceMin+"");
                    myJob.setIntervalsUnit(Integer.valueOf(onceType));
                    if(StringUtils.isNotNull(loopMin)){
                        myJob.setIntervalLoop(loopMin.toString());
                        myJob.setIntervalLoopUnit(Integer.valueOf(loopType));
                    }
                    myJob.setJobName(StringUtils.equals(selectOption,"0")?"发送信息给用户":"发送信息给频道");
                    myJob.setJobType(StringUtils.equals(sendMethod,"1")?"1":"2");
                    myJob.setTarNum(tarNum);
                    myJob.setParms(JSON.toJSONString(parms));
                    myJob.setJobClass("com.ruoyi.system.runnable.sendMessageChannelRunnable");
                    myJob.setCreateDate(DateUtils.getNowDate());
                    myJob.setJobGroup(String.valueOf(longTime));
                    String name = (String) mapAccount.get("TgName");
                    myJob.setSysAccountName(name);
                    myJob.setUserId(getLoginUser().getUserId());
                    myJobMapper.insertMyJob(myJob);
                    int index =1;
                    ArrayList list =new ArrayList();
                    for (SysGroup sysGroups : sysContactList) {
                        MyJobDetail jobDetail = new MyJobDetail();
                        jobDetail.setTarg("https://t.me/"+sysGroups.getSysGroupLink());
                        jobDetail.setIndex(index);
                        jobDetail.setTargId(String.valueOf(sysGroups.getSysGroupId()));
                        jobDetail.setJobId(myJob.getJobId());
                        jobDetail.setTaskClass("com.ruoyi.system.runnable.sendMessageChannelRunnable");
                        jobDetail.setJobDetailStatus(-1);
                        list.add(jobDetail);
                        index++;
                    }
                    List<List<MyJobDetail>> partition = ListUtil.partition(list, 100);
                    partition.forEach(e -> myJobMapper.batchMyJobDetail(e));
                }
            }
        }else{
            for (Object sysAccount : accountList) {
                if(StringUtils.equals(selectOption,"0")){
                    //发送给用户
                    tarNum=syscontacids.size();
                    Map mapAccount = (Map) sysAccount;
                    MyJob myJob = new MyJob();
                    HashMap parms = new HashMap();

                    parms.put("sessionPath",mapAccount.get("sysAccountStringSession"));
                    parms.put("messageGroup",map.get("messageGroup"));
                    parms.put("sendIndex",0);
                    parms.put("endTime",map.get("endTime"));
                    parms.put("startTime",map.get("startTime"));
                    parms.put("isVip",accountDetailId>1?true:false);
                    parms.put("endDate",map.get("endDate"));
                    myJob.setJobName(StringUtils.equals(selectOption,"0")?"发送信息给用户":"发送信息给频道");
                    myJob.setJobType(StringUtils.equals(sendMethod,"1")?"1":"2");
                    myJob.setTarNum(tarNum);
                    myJob.setIntervals(minCount+"");
                    myJob.setIntervalsUnit(Integer.valueOf(min));
                    if(StringUtils.isNotNull(hourCount)){
                        myJob.setIntervalLoop(hourCount+"");
                        myJob.setIntervalLoopUnit(Integer.valueOf(hour));
                    }
                    myJob.setParms(JSON.toJSONString(parms));
                    myJob.setJobClass("com.ruoyi.system.runnable.sendMessageContactRunnable");
                    myJob.setCreateDate(DateUtils.getNowDate());
                    myJob.setJobGroup(String.valueOf(longTime));
                    String name = (String) mapAccount.get("TgName");
                    myJob.setSysAccountName(name);
                    myJob.setUserId(getLoginUser().getUserId());
                    myJobMapper.insertMyJob(myJob);
                    int index =0;
                    ArrayList list =new ArrayList();
                    for (Object sysContactId : syscontacids) {
                        MyJobDetail jobDetail = new MyJobDetail();
                        SysContact sysContact = sysContactMapper.selectSysContactBySysContactId(Long.valueOf(sysContactId.toString()),Long.valueOf(mapAccount.get("sysAccountId").toString()));
                        jobDetail.setTarg(sysContact.getSysContactUserName());
                        jobDetail.setTargId(String.valueOf(sysContact.getSysContactId()));
                        jobDetail.setIndex(index);
                        jobDetail.setJobId(myJob.getJobId());
                        jobDetail.setTaskClass("com.ruoyi.system.runnable.sendMessageContactRunnable");
                        jobDetail.setJobDetailStatus(-1);
                        list.add(jobDetail);
                        index++;
                    }
                    List<List<MyJobDetail>> partition = ListUtil.partition(list, 100);
                    partition.forEach(e -> myJobMapper.batchMyJobDetail(e));
                }else{
                    tarNum=groupList.size();
                    Map mapAccount = (Map) sysAccount;
                    HashMap parms = new HashMap();
                    parms.put("sessionPath",mapAccount.get("sysAccountStringSession"));
                    parms.put("messageGroup",map.get("messageGroup"));
                    parms.put("sendIndex",0);
                    parms.put("endTime",map.get("endTime"));
                    parms.put("startTime",map.get("startTime"));
                    parms.put("endDate",map.get("endDate"));
                    parms.put("isVip",accountDetailId>1?true:false);
                    MyJob myJob = new MyJob();
                    myJob.setIntervals(minCount.toString());
                    myJob.setIntervalsUnit(Integer.valueOf(min));
                    if(StringUtils.isNotNull(hourCount)){
                        myJob.setIntervalLoop(hourCount.toString());
                        myJob.setIntervalLoopUnit(Integer.valueOf(hour));
                    }
                    myJob.setJobName(StringUtils.equals(selectOption,"0")?"发送信息给用户":"发送信息给频道");
                    myJob.setJobType(StringUtils.equals(sendMethod,"1")?"1":"2");
                    myJob.setTarNum(tarNum);
                    myJob.setParms(JSON.toJSONString(parms));
                    myJob.setJobClass("com.ruoyi.system.runnable.sendMessageChannelRunnable");
                    myJob.setCreateDate(DateUtils.getNowDate());
                    myJob.setJobGroup(String.valueOf(longTime));
                    String name = (String) mapAccount.get("TgName");
                    myJob.setSysAccountName(name);
                    myJob.setUserId(getLoginUser().getUserId());
                    myJobMapper.insertMyJob(myJob);
                    int index =0;
                    ArrayList list =new ArrayList();
                    for (Object AccountString : groupList) {
                        MyJobDetail jobDetail = new MyJobDetail();
                        SysGroup sysGroups = sysGroupMapper.selectSysGroupBySysGroupId(Long.valueOf(AccountString.toString()),Long.valueOf(mapAccount.get("sysAccountId").toString()));
                        jobDetail.setTarg("https://t.me/"+sysGroups.getSysGroupLink());
                        jobDetail.setTargId(String.valueOf(sysGroups.getSysGroupId()));
                        jobDetail.setIndex(index);
                        jobDetail.setJobId(myJob.getJobId());
                        jobDetail.setTaskClass("com.ruoyi.system.runnable.sendMessageChannelRunnable");
                        jobDetail.setJobDetailStatus(-1);
                        list.add(jobDetail);
                        index++;
                    }
                    List<List<MyJobDetail>> partition = ListUtil.partition(list, 100);
                    partition.forEach(e -> myJobMapper.batchMyJobDetail(e));
                }
            }
        }
            return success();

    }

    @Override
    public AjaxResult sendPrvite(HashMap map) {
        String targetString = map.get("targetString").toString();
        String shareType = map.get("shareType").toString();
        String onceType = map.get("onceType").toString();
        String onceMin = map.get("onceMin").toString();
        List<Map> accountList = (List<Map>) map.get("accountList");
        //分配方式 全部
        String[] split = targetString.split("\\\n");
        List<String> list = new ArrayList<>();
        for (String s : split) {
            String[] split1 = s.split(",");
            for (String s1 : split1) {
                list.add(s1);
            }
        }
        long  longTime =System.currentTimeMillis();
        int tarNum = 0;
        Long accountDetailId = getLoginUser().getUser().getAccountDetailId();
        if(StringUtils.equals(shareType,"1")){
            for (Map account : accountList) {
                tarNum=list.size();
                HashMap parms = new HashMap();
                parms.put("sessionPath",account.get("sysAccountStringSession"));
                parms.put("messageGroup",map.get("messageGroup"));
                parms.put("sendIndex",0);
                parms.put("endTime",map.get("endTime"));
                parms.put("isVip",accountDetailId>1?true:false);
                parms.put("startTime",map.get("startTime"));
                parms.put("endDate",map.get("endDate"));
                MyJob myJob = new MyJob();
                myJob.setIntervals(onceMin+"");
                myJob.setIntervalsUnit(Integer.valueOf(onceType));
                myJob.setJobName("发送信息给用户");
                myJob.setJobType("1");
                myJob.setTarNum(tarNum);
                myJob.setParms(JSON.toJSONString(parms));
                myJob.setJobClass("com.ruoyi.system.runnable.sendMessageContactRunnable");
                myJob.setCreateDate(DateUtils.getNowDate());
                myJob.setJobGroup(String.valueOf(longTime));
                String name = (String) account.get("TgName");
                myJob.setSysAccountName(name);
                myJob.setUserId(getLoginUser().getUserId());
                myJobMapper.insertMyJob(myJob);
                int index =1;
                ArrayList listJobDetail =new ArrayList();
                for (String userName  : list) {
                    MyJobDetail jobDetail = new MyJobDetail();
                    jobDetail.setTarg(userName);
                    jobDetail.setIndex(index);
                    jobDetail.setJobId(myJob.getJobId());
                    jobDetail.setTaskClass("com.ruoyi.system.runnable.sendMessageContactRunnable");
                    jobDetail.setJobDetailStatus(-1);
                    listJobDetail.add(jobDetail);
                    index++;
                }
                List<List<MyJobDetail>> partition = ListUtil.partition(listJobDetail, 100);
                partition.forEach(e -> myJobMapper.batchMyJobDetail(e));
                return success();
            }
        }else{
            for (Map account : accountList) {
                String sysAccountStringSession = (String)account.get("sysAccountStringSession");
                tarNum=list.size();
                HashMap parms = new HashMap();
                parms.put("sessionPath",account.get("sysAccountStringSession"));
                parms.put("messageGroup",map.get("messageGroup"));
                parms.put("sendIndex",0);
                parms.put("endTime",map.get("endTime"));
                parms.put("startTime",map.get("startTime"));
                parms.put("endDate",map.get("endDate"));
                parms.put("isVip",accountDetailId>1?true:false);
                MyJob myJob = new MyJob();
                myJob.setIntervals(onceMin+"");
                myJob.setIntervalsUnit(Integer.valueOf(onceType));
                myJob.setJobName("发送信息给用户");
                myJob.setJobType("1");
                myJob.setTarNum(tarNum);
                myJob.setParms(JSON.toJSONString(parms));
                myJob.setJobClass("com.ruoyi.system.runnable.sendMessageContactRunnable");
                myJob.setCreateDate(DateUtils.getNowDate());
                myJob.setJobGroup(String.valueOf(longTime));
                String name = (String) account.get("TgName");
                myJob.setSysAccountName(name);
                myJob.setUserId(getLoginUser().getUserId());
                myJobMapper.insertMyJob(myJob);
                int index =1;
                ArrayList listJobDetail =new ArrayList();
                for (String userName  : list) {
                    MyJobDetail jobDetail = new MyJobDetail();
                    jobDetail.setTarg(userName);
                    jobDetail.setIndex(index);
                    jobDetail.setJobId(myJob.getJobId());
                    jobDetail.setTaskClass("com.ruoyi.system.runnable.sendMessageContactRunnable");
                    jobDetail.setJobDetailStatus(-1);
                    listJobDetail.add(jobDetail);
                    index++;
                }
                List<List<MyJobDetail>> partition = ListUtil.partition(listJobDetail, 100);
                partition.forEach(e -> myJobMapper.batchMyJobDetail(e));
                return success();
            }

            return  success();

        }
        return  success();
    }

    @Override
    public AjaxResult resend(List<MyJob> myJobs) {
        //再次发送
        try{
            for (MyJob myJob : myJobs) {
            if(myJob.getOption()==1){
                //修改任务及明细
                MyJob updateJob = myJobMapper.selectJobById(myJob.getJobId());
                updateJob.setJobStatus("1");
                updateJob.setSuccessNum(0);
                myJobMapper.insertMyJob(updateJob);
                List<MyJobDetail> myJobDetails = sysTaskMapper.selectSysTaskDetailList(myJob.getJobId());
                sysTaskMapper.deleteByJobId(myJob.getJobId());
                for (MyJobDetail myJobDetail : myJobDetails) {
                    myJobDetail.setJobDetailStatus(-1);
                }
                List<List<MyJobDetail>> partition = ListUtil.partition(myJobDetails, 100);
                partition.forEach(e -> myJobMapper.batchMyJobDetail(e));
            }
            //暂停
            else if(myJob.getOption()==2){
                MyJob updateJob = myJobMapper.selectJobById(myJob.getJobId());
                myJob.setJobStatus("0");
                myJobMapper.insertMyJob(updateJob);
            }
            //开始
            else if(myJob.getOption()==3){
                MyJob updateJob = myJobMapper.selectJobById(myJob.getJobId());
                updateJob.setJobStatus("1");
                myJobMapper.insertMyJob(updateJob);
            }
            }
        }catch (Exception e){
            return new AjaxResult(400,e.getMessage());
        }

        return success();
    }

    @Override
    public AjaxResult deleteList(List<Integer> myJob) {
        try{
            sysTaskMapper.BatchDeleteJobByJobId(myJob);
            sysTaskMapper.BatchDeleteJobDetailByJobId(myJob);
            return success();
        }catch (Exception e){
            return new AjaxResult(400,e.getMessage());
        }
    }
}
