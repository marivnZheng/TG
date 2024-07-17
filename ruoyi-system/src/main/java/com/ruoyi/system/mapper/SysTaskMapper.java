package com.ruoyi.system.mapper;

import com.ruoyi.common.domain.MyJob;
import com.ruoyi.common.domain.MyJobDetail;

import java.util.List;

/**
 * Mapper接口
 * 
 * @author ruoyi
 * @date 2024-05-09
 */
public interface SysTaskMapper
{


    public List<MyJob> selectSysTaskList(MyJob myJob);


    public List<MyJobDetail> selectSysTaskDetailList(Long jobId);

    public List<MyJob> selectTaskListByStatus();

    public int deleteByJobId(Long jobId);

    public int BatchDeleteJobByJobId(List<Integer> jobId);

    public int BatchDeleteJobDetailByJobId(List<Integer> jobId);


    public MyJobDetail selectSysTaskDetailListOrderByIndex(Long jobId);
}
