package com.ruoyi.common.domain;

import java.io.Serializable;
import java.util.Date;

public class MyJob implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long jobId; //任务id

    private String sysAccountName; //执行用户名称

    private String jobName;//任务名称

    private String jobType;//任务类型  1-- 单次  2--循环

    private Integer tarNum;//目标数量

    private Integer successNum;//成功数量

    private String intervals;//单次间隔时间

    private int intervalsUnit;//单次间隔单位

    private String intervalLoop;//循环间隔

    private int intervalLoopUnit;//循环单位

    private String parms;//请求参数

    private String jobGroup;//任务组

    private String jobClass;//执行任务类

    private String jobStatus;//任务状态  0-暂停  1-待执行  2-执行中 3-已完成

    private Date createDate;

    private long userId;

    private int option;

    private Date planDate;//执行计划时间

    private int loopEnd;//

    public int getLoopEnd() {
        return loopEnd;
    }

    public void setLoopEnd(int loopEnd) {
        this.loopEnd = loopEnd;
    }

    public Date getPlanDate() {
        return planDate;
    }

    public void setPlanDate(Date planDate) {
        this.planDate = planDate;
    }

    public int getOption() {
        return option;
    }

    public void setOption(int option) {
        this.option = option;
    }

    public int getIntervalsUnit() {
        return intervalsUnit;
    }

    public void setIntervalsUnit(int intervalsUnit) {
        this.intervalsUnit = intervalsUnit;
    }

    public int getIntervalLoopUnit() {
        return intervalLoopUnit;
    }

    public void setIntervalLoopUnit(int intervalLoopUnit) {
        this.intervalLoopUnit = intervalLoopUnit;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getJobStatus() {
        return jobStatus;
    }

    public void setJobStatus(String jobStatus) {
        this.jobStatus = jobStatus;
    }

    public String getIntervals() {
        return intervals;
    }

    public void setIntervals(String intervals) {
        this.intervals = intervals;
    }

    public String getJobClass() {
        return jobClass;
    }

    public void setJobClass(String jobClass) {
        this.jobClass = jobClass;
    }

    public Long getJobId() {
        return jobId;
    }

    public void setJobId(Long jobId) {
        this.jobId = jobId;
    }

    public String getSysAccountName() {
        return sysAccountName;
    }

    public void setSysAccountName(String sysAccountName) {
        this.sysAccountName = sysAccountName;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public String getJobType() {
        return jobType;
    }

    public void setJobType(String jobType) {
        this.jobType = jobType;
    }

    public Integer getTarNum() {
        return tarNum;
    }

    public void setTarNum(Integer tarNum) {
        this.tarNum = tarNum;
    }

    public Integer getSuccessNum() {
        return successNum;
    }

    public void setSuccessNum(Integer successNum) {
        this.successNum = successNum;
    }

    public String getIntervalLoop() {
        return intervalLoop;
    }

    public void setIntervalLoop(String intervalLoop) {
        this.intervalLoop = intervalLoop;
    }

    public String getParms() {
        return parms;
    }

    public void setParms(String parms) {
        this.parms = parms;
    }

    public String getJobGroup() {
        return jobGroup;
    }

    public void setJobGroup(String jobGroup) {
        this.jobGroup = jobGroup;
    }

    @Override
    public String toString() {
        return "MyJob{" +
                "jobId=" + jobId +
                ", sysAccountName='" + sysAccountName + '\'' +
                ", jobName='" + jobName + '\'' +
                ", jobType='" + jobType + '\'' +
                ", tarNum=" + tarNum +
                ", successNum=" + successNum +
                ", intervals='" + intervals + '\'' +
                ", intervalsUnit=" + intervalsUnit +
                ", intervalLoop='" + intervalLoop + '\'' +
                ", intervalLoopUnit=" + intervalLoopUnit +
                ", parms='" + parms + '\'' +
                ", jobGroup='" + jobGroup + '\'' +
                ", jobClass='" + jobClass + '\'' +
                ", jobStatus='" + jobStatus + '\'' +
                ", createDate=" + createDate +
                ", userId=" + userId +
                '}';
    }
}
