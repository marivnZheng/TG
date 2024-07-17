package com.ruoyi.web.controller.system;

import com.alibaba.fastjson2.JSON;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.domain.MyJob;
import com.ruoyi.common.domain.MyJobDetail;
import com.ruoyi.system.service.ISysTaskService;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.spring.web.json.Json;

import java.util.HashMap;
import java.util.List;

/**
 * Controller
 *
 * @author ruoyi
 * @date 2024-05-09
 */
@RestController
@RequestMapping("/system/task")
public class SysTaskController extends BaseController
{
    @Autowired
    private ISysTaskService sysTaskService;

    /**
     * 查询列表
     */
    @PreAuthorize("@ss.hasPermi('system:account:list')")
    @PostMapping("/list")
    public TableDataInfo list(@RequestBody MyJob myJob)
    {
        startPage();
        List<MyJob> list = sysTaskService.selectSysTaskList(myJob);
        return getDataTable(list);
    }

    @PreAuthorize("@ss.hasPermi('system:task:list')")
    @PostMapping("/sendMessage")
    public AjaxResult sendMessage(@RequestBody HashMap map) {

        return sysTaskService.runTGTask(map);
    }

    @PreAuthorize("@ss.hasPermi('system:account:list')")
    @PostMapping("/sendMessagePrivate")
    public AjaxResult sendMessagePrivate(@RequestBody HashMap map)
    {
        return sysTaskService.sendPrvite(map);
    }

    /**
     * 查询列表
     */
    @PreAuthorize("@ss.hasPermi('system:account:list')")
    @PostMapping("/getJobDetail")
    public List<MyJobDetail> getJobDetail(@RequestBody MyJob myJob)
    {
        List<MyJobDetail> list = sysTaskService.selectSysTaskDetailList(myJob);
        return list;
    }

    @PreAuthorize("@ss.hasPermi('system:account:list')")
    @PostMapping("/resend")
    public AjaxResult resend(@RequestBody List<MyJob> myJob)
    {
        return sysTaskService.resend(myJob);
    }



    @PreAuthorize("@ss.hasPermi('system:account:list')")
    @PostMapping("/deleteList")
    public AjaxResult deleteList(@RequestBody List<Integer> jobIds)
    {
        return sysTaskService.deleteList(jobIds);
    }

}
