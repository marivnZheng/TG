package com.ruoyi.common.mapper;

import com.ruoyi.common.domain.MyJob;
import com.ruoyi.common.domain.MyJobDetail;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * 调度任务信息 数据层
 * 
 * @author ruoyi
 */
public interface MyJobMapper
{
    /**
     * 新增调度任务信息
     * 
     * @param job 调度任务信息
     * @return 结果
     */
    public int insertMyJob(MyJob job);

    public int updateMyJob(long jobId);

    public int updateMyJobFail(long jobId);

    public int updateMyJobAndStatus(long jobId);

    public int updateMyJobMessageGroup(@Param("parms") String parms ,@Param("jobId") long jobId);

    public MyJob selectJobById(@Param("jobId") long jobId);


    public int updateMyJobAndStatusFail(long jobId);

    public int batchMyJobDetail(List<MyJobDetail> list);

    public int insertMyJobDetail(MyJobDetail job);


    public int updateStatus(@Param("status") int status, @Param("jobDetailId")Long jobDetailId, @Param("nextPlanDate")Date nextPlanDate);
}
