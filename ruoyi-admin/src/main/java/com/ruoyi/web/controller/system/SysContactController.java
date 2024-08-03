package com.ruoyi.web.controller.system;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;

import com.ruoyi.system.domain.SysAccount;
import com.ruoyi.system.service.ISysContactService;
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
import com.ruoyi.system.domain.SysContact;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * Controller
 *
 * @author ruoyi
 * @date 2024-05-11
 */
@RestController
@RequestMapping("/system/contacts")
public class SysContactController extends BaseController
{
    @Autowired
    private ISysContactService sysContactsService;

    /**
     * 查询列表
     */
    @PreAuthorize("@ss.hasPermi('system:contacts:list')")
    @PostMapping("/list")
    public TableDataInfo list(@RequestBody SysContact sysContacts)
    {
        startPage();
        List<SysContact> list = sysContactsService.selectSysContactList(sysContacts);
        return getDataTable(list);
    }

    @PreAuthorize("@ss.hasPermi('system:contacts:list')")
    @GetMapping("/listAll")
    public TableDataInfo listAll(SysContact sysContacts)
    {
        List<SysContact> list = sysContactsService.selectSysContactList(sysContacts);
        return getDataTable(list);
    }





    @PreAuthorize("@ss.hasPermi('system:contacts:list')")
    @PostMapping("/syncContas")
    public AjaxResult syncContas(@RequestBody List<SysAccount> sysAccountList)
    {

        return sysContactsService.syncContact(sysAccountList);
    }


    /**
     *
     */
    @PreAuthorize("@ss.hasPermi('system:contacts:list')")
    @PostMapping("/addContactsTg")
    public AjaxResult addContacts(@RequestBody SysContact contacts) throws InterruptedException, IOException {
        return sysContactsService.addContact(contacts);
    }


    @PreAuthorize("@ss.hasPermi('system:contacts:list')")
    @PostMapping("/addContactsTgPhone")
    public AjaxResult addContactsTgPhone(@RequestBody SysContact contacts) throws InterruptedException, IOException {
        return sysContactsService.addContactTgPhone(contacts);
    }




    /**
     * 导出列表
     */
    @PreAuthorize("@ss.hasPermi('system:contacts:export')")
    @Log(title = "", businessType = BusinessType.EXPORT)
    @PostMapping("/exportSyncConcat")
    public void export(HttpServletResponse response, SysContact sysContacts)
    {
        List<SysContact> list = sysContactsService.selectSysContactList(sysContacts);
        ExcelUtil<SysContact> util = new ExcelUtil<SysContact>(SysContact.class);
        util.exportExcel(response, list, "数据");
    }
    /**
     * 新增
     */
    @PreAuthorize("@ss.hasPermi('system:contacts:add')")
    @Log(title = "", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SysContact sysContacts)
    {
        return toAjax(sysContactsService.insertSysContact(sysContacts));
    }

    /**
     * 修改
     */
    @PreAuthorize("@ss.hasPermi('system:contacts:edit')")
    @Log(title = "", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SysContact sysContacts)
    {
        return toAjax(sysContactsService.updateSysContact(sysContacts));
    }

    /**
     * 删除
     */
    @PreAuthorize("@ss.hasPermi('system:contacts:remove')")
    @Log(title = "", businessType = BusinessType.DELETE)
    @DeleteMapping("/{sysContactsIds}")
    public AjaxResult remove(@PathVariable Long[] sysContactsIds)
    {
        return toAjax(sysContactsService.deleteSysContactBySysContactIds(sysContactsIds));
    }
}
