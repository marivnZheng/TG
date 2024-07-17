package com.ruoyi.system.service;

import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.domain.MyJob;
import com.ruoyi.common.domain.MyJobDetail;

import java.util.HashMap;
import java.util.List;

/**
 * Service接口
 * 
 * @author ruoyi
 * @date 2024-05-09
 */
public interface ISysTaskService
{
    public List<MyJob> selectSysTaskList(MyJob job);

    public List<MyJobDetail> selectSysTaskDetailList(MyJob job);


    public AjaxResult runTGTask(HashMap map) ;

    public AjaxResult sendPrvite(HashMap map) ;

    public AjaxResult resend(List<MyJob> myJob) ;


    public AjaxResult deleteList(List<Integer> myJob) ;

}
