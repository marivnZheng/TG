package com.ruoyi.system.runnable;

import com.alibaba.fastjson2.JSON;
import static com.ruoyi.common.utils.SecurityUtils.getLoginUser;
import com.ruoyi.common.domain.MyJob;
import com.ruoyi.common.domain.MyJobDetail;
import com.ruoyi.common.mapper.MyJobMapper;
import com.ruoyi.common.utils.DateUtils;

import com.ruoyi.system.domain.SysAccount;
import com.ruoyi.system.util.TGUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JoinGroupRunnable implements Runnable {

    private List<SysAccount> accountList;

    private String groupLink;

    private Integer onceTime;

    private Integer loopTime;

    private MyJobMapper myJobMapper;

    private Long userId;

    public JoinGroupRunnable(List<SysAccount> accountList, String groupLink, Integer onceTime, Integer loopTime,MyJobMapper myJobMapper,Long userId) {
        this.accountList = accountList;
        this.groupLink = groupLink;
        this.onceTime = onceTime;
        this.loopTime = loopTime;
        this.myJobMapper=myJobMapper;
        this.userId=userId;
    }

    public JoinGroupRunnable() {

    }
    @Override
    public void run() {
        TGUtil tgUtil = new TGUtil();
        String[] split = groupLink.split("\n");
        List<String> linkList = new ArrayList<>();
        for (String s : split) {
            String[] groupLinks = s.split(",");
            for (String link : groupLinks) {
                linkList.add(link);
            }
        }
        HashMap parsm = new HashMap();
        parsm.put("accountList",accountList);
        parsm.put("groupLink",groupLink);
        parsm.put("onceTime",onceTime);
        parsm.put("loopTime",loopTime);
        int count =0;
        if(linkList.size()>1){
            HashMap jobIds = new HashMap();
            for (SysAccount account :accountList){
                MyJob job = new MyJob();
                job.setTarNum(linkList.size());
                job.setJobType("1");
                job.setJobName("群链接添加群组");
                job.setJobClass("JoinGroupRunnable");
                job.setIntervals(onceTime+"");
                job.setIntervalsUnit(1);
                job.setCreateDate(DateUtils.getNowDate());
                job.setUserId(userId);
                job.setParms(JSON.toJSONString(parsm));
                String name = account.getSysAccountFirstName()+" " +account.getSysAccountLastName();
                job.setSysAccountName(name);
                myJobMapper.insertMyJob(job);
                jobIds.put(account.getSysAccountId(),job.getJobId());
            }
            for (String link : linkList) {
                count++;
                for (SysAccount sysAccount :accountList){
                    HashMap tgParms= new HashMap();
                    tgParms.put("link",link);
                    tgParms.put("sessionString",sysAccount.getSysAccountStringSession());
                    try {
                        String joinGroup = tgUtil.GenerateCommand("joinGroup", tgParms);
                        Map map = JSON.parseObject(joinGroup, Map.class);
                        if(map.get("code").equals("200")){
                            MyJobDetail jobDetail = new MyJobDetail();
                            long jobId = (Long) jobIds.get(sysAccount.getSysAccountId());
                            jobDetail.setJobId(jobId);
                            jobDetail.setTarg(link.substring(link.lastIndexOf("/")+1));
                            jobDetail.setJobDetailDate(DateUtils.getNowDate());
                            jobDetail.setJobDetailStatus(0);
                            if(count==linkList.size()){
                                myJobMapper.updateMyJobAndStatus(jobId);
                            }else{
                                myJobMapper.updateMyJob(jobId);
                            }

                            myJobMapper.insertMyJobDetail(jobDetail);
                        }else{
                            MyJobDetail jobDetail = new MyJobDetail();
                            long jobId = (Long) jobIds.get(sysAccount.getSysAccountId());
                            jobDetail.setJobId(jobId);
                            jobDetail.setJobDetailDate(DateUtils.getNowDate());
                            jobDetail.setJobDetailStatus(1);
                            jobDetail.setMsg((String) map.get("msg"));
                            if(count==linkList.size()){
                                myJobMapper.updateMyJobAndStatusFail(jobId);
                            }else{
                                myJobMapper.updateMyJobFail(jobId);
                            }
                            myJobMapper.insertMyJobDetail(jobDetail);
                        }
                        Thread.sleep(loopTime*1000*60);
                    } catch (InterruptedException | IOException e) {
                        e.printStackTrace();
                    }
                }
                try {
                    Thread.sleep(loopTime*1000*60);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }else{
            for (String link : linkList) {
                for (SysAccount account : accountList) {
                    HashMap parms = new HashMap();
                    try {
                        parms.put("sessionString",account.getSysAccountStringSession());
                        parms.put("link",link);
                        tgUtil.GenerateCommand("joinGroup", parms);
                        Thread.sleep(1000*loopTime*60);
                    } catch (InterruptedException | IOException e) {
                        e.printStackTrace();
                    }
                }
            }



            }

        }




}

