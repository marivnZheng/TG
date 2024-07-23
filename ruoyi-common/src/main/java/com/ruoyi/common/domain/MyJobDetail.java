package com.ruoyi.common.domain;

import java.io.Serializable;
import java.util.Date;

public class MyJobDetail implements Serializable {

    private static final long serialVersionUID = 1L;

    private  long jobDetailId;

    private String msg;

    private  int jobDetailStatus;

    private Date jobDetailDate;

    private Long jobId;

    private String targ;

    private String targId;

    private int index;

    private  String taskClass;

    private Date nextPlanDate;


    public Date getNextPlanDate() {
        return nextPlanDate;
    }

    public void setNextPlanDate(Date nextPlanDate) {
        this.nextPlanDate = nextPlanDate;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getTaskClass() {
        return taskClass;
    }

    public void setTaskClass(String taskClass) {
        this.taskClass = taskClass;
    }

    public long getJobDetailId() {
        return jobDetailId;
    }

    public void setJobDetailId(long jobDetailId) {
        this.jobDetailId = jobDetailId;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getJobDetailStatus() {
        return jobDetailStatus;
    }

    public void setJobDetailStatus(int jobDetailStatus) {
        this.jobDetailStatus = jobDetailStatus;
    }

    public Date getJobDetailDate() {
        return jobDetailDate;
    }

    public void setJobDetailDate(Date jobDetailDate) {
        this.jobDetailDate = jobDetailDate;
    }

    public Long getJobId() {
        return jobId;
    }

    public void setJobId(Long jobId) {
        this.jobId = jobId;
    }

    public String getTarg() {
        return targ;
    }

    public void setTarg(String targ) {
        this.targ = targ;
    }

    public String getTargId() {
        return targId;
    }

    public void setTargId(String targId) {
        this.targId = targId;
    }

    @Override
    public String toString() {
        return "MyJobDetail{" +
                "jobDetailId=" + jobDetailId +
                ", msg='" + msg + '\'' +
                ", jobDetailStatus=" + jobDetailStatus +
                ", jobDetailDate=" + jobDetailDate +
                ", jobId=" + jobId +
                ", targ='" + targ + '\'' +
                ", index=" + index +
                ", taskClass='" + taskClass + '\'' +
                '}';
    }
}
