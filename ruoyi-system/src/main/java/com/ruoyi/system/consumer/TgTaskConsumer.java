package com.ruoyi.system.consumer;

import cn.hutool.core.collection.ListUtil;
import com.alibaba.fastjson2.JSON;
import com.ruoyi.common.domain.MyJob;
import com.ruoyi.common.domain.MyJobDetail;
import com.ruoyi.common.mapper.MyJobMapper;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.mapper.SysTaskMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;


@Slf4j
@Configuration
@EnableScheduling
public class TgTaskConsumer{

        @Autowired
        private SysTaskMapper sysTaskMapper;

        @Autowired
        private MyJobMapper myJobMapper;

        private ExecutorService executor = Executors.newCachedThreadPool() ;

        @Scheduled(cron = "0/5 * * * * ?")
        public void executeTask() throws InterruptedException, ParseException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException, ClassNotFoundException {
                //查看待执行任务
                List<MyJob> myJobs = sysTaskMapper.selectTaskListByStatus();
                for (MyJob myJob : myJobs) {
                        //循环任务
                        if(myJob.getJobType().equals("2")){
                                //验证任务是否已经运行完毕
                                String parms = myJob.getParms();
                                MyJobDetail myJobDetail = sysTaskMapper.selectSysTaskDetailListOrderByIndex(myJob.getJobId());
                                if(myJobDetail==null){
                                        //任务执行完毕，判断是否循环
                                        Map map = JSON.parseObject(parms, Map.class);
                                        String startTime = (String) map.get("startTime");
                                        String endTime = (String) map.get("endTime");
                                        String endDate = (String) map.get("endDate");
                                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                                        //存在结束时间
                                        log.info("startTime:{},endTime:{},endDate:{},nowDate:{}",startTime,endTime,endDate,DateUtils.getNowDate().getTime());
                                        if(!StringUtils.isEmpty(endDate)){
                                                if(dateFormat.parse(endDate).getTime()-DateUtils.getNowDate().getTime()<0){
                                                        //设置不在循环
                                                        myJob.setLoopEnd(1);
                                                        myJob.setJobStatus("3");
                                                        myJobMapper.insertMyJob(myJob);
                                                        break;
                                                }
                                        }
                                        //存在休息时间段
                                        if(!StringUtils.isEmpty(startTime)&&!StringUtils.isEmpty(endTime)){
                                                Date startTime1 = dateFormat.parse(startTime);
                                                Date endTime1= dateFormat.parse(endTime);
                                                //区间内不执行
                                                if(isEffectiveDate(DateUtils.getNowDate(),startTime1,endTime1) ){
                                                        break;
                                                }
                                        }
                                        //比对计划是否到达计划时间，是则创建任务

                                        if(myJob.getPlanDate()!=null && myJob.getPlanDate().getTime()-DateUtils.getNowDate().getTime()<0){
                                                //大于计划时间，设置任务
                                                myJob.setJobStatus("1");
                                                myJob.setSuccessNum(0);
                                                myJobMapper.insertMyJob(myJob);
                                                List<MyJobDetail> myJobDetails = sysTaskMapper.selectSysTaskDetailList(myJob.getJobId());
                                                sysTaskMapper.deleteByJobId(myJob.getJobId());
                                                for (MyJobDetail jobDetail : myJobDetails) {
                                                        jobDetail.setJobDetailStatus(-1);
                                                }
                                                List<List<MyJobDetail>> partition = ListUtil.partition(myJobDetails, 100);
                                                partition.forEach(list -> myJobMapper.batchMyJobDetail(list));
                                        }

                                }else{
                                        if(myJobDetail.getJobDetailStatus()!=-2){
                                                myJobDetail.setJobDetailStatus(-2);
                                                myJobMapper.insertMyJobDetail(myJobDetail);
                                                log.info("执行任务类型为：{}",myJobDetail.getTaskClass());
                                                exec(parms,myJobDetail,myJob);
                                        }
                                }

                        }else{
                                String parms = myJob.getParms();
                                MyJobDetail myJobDetail = sysTaskMapper.selectSysTaskDetailListOrderByIndex(myJob.getJobId());
                                if(myJobDetail!=null){
                                        if(myJobDetail.getJobDetailStatus()!=-2){
                                                myJobDetail.setJobDetailStatus(-2);
                                                myJobMapper.insertMyJobDetail(myJobDetail);
                                                log.info("执行任务类型为：{}",myJobDetail.getTaskClass());
                                                exec(parms,myJobDetail,myJob);
                                        }
                                }else{
                                        myJob.setJobStatus("3");
                                        myJobMapper.insertMyJob(myJob);
                                }
                        }


                }
        }

        public  void exec(String parms,MyJobDetail myJobDetail,MyJob myJob) throws IllegalAccessException, InvocationTargetException, InstantiationException, ClassNotFoundException, NoSuchMethodException {

                        Class<?>runnable = Class.forName(myJobDetail.getTaskClass());
                        Class[] parameterTypes={String.class,MyJobDetail.class, MyJobMapper.class,Boolean.class,MyJob.class};
                        boolean flag = false;
                        if((myJobDetail.getIndex()+1) % myJob.getTarNum() == 0) {
                                flag=true;
                        }
                        Constructor<?> constructors = runnable.getConstructor(parameterTypes);
                        Object[] parameters={parms,myJobDetail,myJobMapper,flag,myJob};
                        Runnable o =(Runnable) constructors.newInstance(parameters);
                        executor.execute(o);
        };
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
