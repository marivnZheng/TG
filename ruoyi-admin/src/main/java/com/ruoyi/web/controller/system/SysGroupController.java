package com.ruoyi.web.controller.system;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.ruoyi.system.domain.SysAccount;
import com.ruoyi.system.domain.SysContact;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.system.domain.SysGroup;
import com.ruoyi.system.service.ISysGroupService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * Controller
 *
 * @author ruoyi
 * @date 2024-05-14
 */
@RestController
@RequestMapping("/system/group")
public class SysGroupController extends BaseController
{
    @Autowired
    private ISysGroupService sysGroupService;

    /**
     * 查询列表
     */
    @PreAuthorize("@ss.hasPermi('system:group:list')")
    @PostMapping("/list")
    public TableDataInfo list(@RequestBody SysGroup sysGroup)
    {
        startPage();
        List<SysGroup> list = sysGroupService.selectSysGroupList(sysGroup);
        return getDataTable(list);
    }

    @PreAuthorize("@ss.hasPermi('system:group:list')")
    @PostMapping("/listGroupAll")
    public TableDataInfo listGroupAll(@RequestBody SysGroup sysGroup)
    {
        List<SysGroup> list = sysGroupService.selectSysGroupList(sysGroup);
        return getDataTable(list);
    }

    /**
     * 导出列表
     */
    @PreAuthorize("@ss.hasPermi('system:group:export')")
    @Log(title = "", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, SysGroup sysGroup)
    {
        List<SysGroup> list = sysGroupService.selectSysGroupList(sysGroup);
        for (SysGroup group : list) {
            if(group.getSysGroupDetail().equals("True")){
                group.setSysGroupDetailExcel("群组");
            }else{
                group.setSysGroupDetailExcel("频道");
            }
            if(group.getSysGroupSendPhoto()==0){
                group.setSysGroupSendPhotoExcel("禁止");
            }
            if(group.getSysGroupSendMessage()==0){
                group.setSysGroupSendMessageExcel("禁止");
            }
            if(group.getSysGroupInvite()==0){
                group.setSysGroupInviteExcel("禁止");
            }
        }

        ExcelUtil<SysGroup> util = new ExcelUtil<SysGroup>(SysGroup.class);
        util.exportExcel(response, list, "数据");
    }

    /**
     * 获取详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:group:query')")
    @GetMapping(value = "/{sysGroupId}")
    public AjaxResult getInfo(@PathVariable("sysGroupId") Long sysGroupId)
    {
        return success(sysGroupService.selectSysGroupBySysGroupId(sysGroupId));
    }

    /**
     * 新增
     */
    @PreAuthorize("@ss.hasPermi('system:group:add')")
    @Log(title = "", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SysGroup sysGroup)
    {
        return toAjax(sysGroupService.insertSysGroup(sysGroup));
    }


    @PreAuthorize("@ss.hasPermi('system:contacts:list')")
    @PostMapping("/syncGroup")
    public AjaxResult syncGroup(@RequestBody List<SysAccount> sysAccountList)
    {

        return sysGroupService.syncGroup(sysAccountList);
    }

    @PreAuthorize("@ss.hasPermi('system:contacts:list')")
    @PostMapping("/getGroupMember")
    public TableDataInfo getGroupMember(@RequestBody SysGroup sysGroup)
    {
        startPage();
        List<SysContact> list = sysGroupService.getGroupMember(sysGroup);
        return getDataTable(list);
    }

    @PreAuthorize("@ss.hasPermi('system:contacts:list')")
    @PostMapping("/addConcats")
    public AjaxResult addConcats(@RequestBody HashMap map) throws InterruptedException, IOException {
        return sysGroupService.addConcats(map);

    }

    @PreAuthorize("@ss.hasPermi('system:contacts:list')")
    @PostMapping("/inviteGroup")
    public AjaxResult inviteGroup(@RequestBody HashMap map) throws InterruptedException, IOException {
        return  sysGroupService.inviteGroup(map);

    }

    @PreAuthorize("@ss.hasPermi('system:contacts:list')")
    @PostMapping("/inviteGroupList")
    public AjaxResult inviteGroupList(@RequestBody HashMap map)
    {
        return  sysGroupService.inviteGroupList(map);

    }
    /**
     * 修改
     */
    @PreAuthorize("@ss.hasPermi('system:group:edit')")
    @Log(title = "", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SysGroup sysGroup)
    {
        return toAjax(sysGroupService.updateSysGroup(sysGroup));
    }

    /**
     * 删除
     */
    @PreAuthorize("@ss.hasPermi('system:group:remove')")
    @Log(title = "", businessType = BusinessType.DELETE)
    @DeleteMapping("/{sysGroupIds}")
    public AjaxResult remove(@PathVariable Long[] sysGroupIds)
    {
        return toAjax(sysGroupService.deleteSysGroupBySysGroupIds(sysGroupIds));
    }
}
