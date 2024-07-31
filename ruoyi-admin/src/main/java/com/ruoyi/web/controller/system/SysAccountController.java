package com.ruoyi.web.controller.system;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.ruoyi.framework.web.service.SysRegisterService;
import com.ruoyi.system.dto.TgLogin;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.system.domain.SysAccount;
import com.ruoyi.system.service.ISysAccountService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.spring.web.json.Json;

/**
 * Controller
 *
 * @author ruoyi
 * @date 2024-05-09
 */
@RestController
@RequestMapping("/system/account")
public class SysAccountController extends BaseController
{
    @Autowired
    private ISysAccountService sysAccountService;

    @Autowired
    private SysRegisterService registerService;

    /**
     * 查询列表
     */
    @PreAuthorize("@ss.hasPermi('system:account:list')")
    @PostMapping("/list")
    public TableDataInfo list(@RequestBody SysAccount sysAccount)
    {
        logger.info(sysAccount.toString());
        startPage();
        List<SysAccount> list = sysAccountService.selectSysAccountList(sysAccount);
        return getDataTable(list);
    }


    @PreAuthorize("@ss.hasPermi('system:account:list')")
    @PostMapping("/ListOnline")
    public TableDataInfo ListOnline(@RequestBody  SysAccount sysAccount)
    {
        startPage();
        List<SysAccount> list = sysAccountService.selectListOnline(sysAccount);
        return getDataTable(list);
    }

    /**
     * 获取详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:account:query')")
    @GetMapping(value = "/{sysAccountId}")
    public AjaxResult getInfo(@PathVariable("sysAccountId") Long sysAccountId)
    {
        return success(sysAccountService.selectSysAccountBySysAccountId(sysAccountId));
    }

    /**
     * 发送验证码
     */
    @PreAuthorize("@ss.hasPermi('system:account:query')")
    @GetMapping(value = "sendPhoneCode/{phoneNumber}")
    public AjaxResult getPhoneCode(@PathVariable("phoneNumber") String phoneNumber) throws InterruptedException, IOException {
        return success(sysAccountService.sendPhoneCode(phoneNumber));
    }


    @PostMapping("/getCurrentUser")
    public AjaxResult getCurrentUser()
    {
        return registerService.getCurrentUser();
    }

    @PreAuthorize("@ss.hasPermi('system:account:query')")
    @GetMapping(value = "checkPhoneAndUserJurisdiction/{phoneNumber}")
    public AjaxResult checkPhoneAndUserJurisdiction(@PathVariable("phoneNumber") String phoneNumber) throws InterruptedException, IOException {

        return sysAccountService.checkPhoneAndUserJurisdiction(phoneNumber);
    }

    @PreAuthorize("@ss.hasPermi('system:account:query')")
    @PostMapping(value = "loginAccountByPhoneCode")
    public AjaxResult loginAccountByPhoneCode(@RequestBody TgLogin tgLogin) throws InterruptedException, IOException {
        AjaxResult ajaxResult = sysAccountService.loginAccountByPhoneCode(tgLogin);
        return ajaxResult;
    }


    @PreAuthorize("@ss.hasPermi('system:account:query')")
    @PostMapping(value = "sessionFileUpload")
    public AjaxResult sessionFileUpload( @RequestParam MultipartFile file) throws IOException, InterruptedException {
        AjaxResult ajaxResult = sysAccountService.sessionFileUpload(file);
        return ajaxResult;
    }




    @PreAuthorize("@ss.hasPermi('system:account:query')")
    @PostMapping(value ="syncAccount")
    public AjaxResult syncAccount( @RequestBody List<SysAccount> accountList) throws IOException, InterruptedException {
        AjaxResult ajaxResult = sysAccountService.syncAccount(accountList);
        return ajaxResult;
    }


    @PreAuthorize("@ss.hasPermi('system:account:query')")
    @PostMapping(value ="updateListAccountOnline")
    public AjaxResult updateListAccountOnline( @RequestBody List<SysAccount> accountList) throws IOException, InterruptedException {
        AjaxResult ajaxResult = sysAccountService.updateListAccountOnline(accountList);
        return ajaxResult;
    }


    @PreAuthorize("@ss.hasPermi('system:account:query')")
    @PostMapping(value ="editAccountDetail")
    public AjaxResult editAccountDetail( @RequestBody SysAccount account) throws IOException, InterruptedException {
        AjaxResult ajaxResult = sysAccountService.editAccountDetail(account);
        return ajaxResult;
    }


    @PreAuthorize("@ss.hasPermi('system:account:query')")
    @PostMapping(value = "sessionFileLogin")
    public AjaxResult sessionFileLogin( @RequestBody String json) throws IOException, InterruptedException {
        AjaxResult ajaxResult = sysAccountService.sessionFileLogin(json);
        return ajaxResult;
    }

    /**
     * 新增
     */
    @PreAuthorize("@ss.hasPermi('system:account:add')")
    @Log(title = "", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SysAccount sysAccount)
    {
        return toAjax(sysAccountService.insertSysAccount(sysAccount));
    }

    /**
     * 修改
     */
    @PreAuthorize("@ss.hasPermi('system:account:edit')")
    @Log(title = "", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SysAccount sysAccount)
    {
        return toAjax(sysAccountService.updateSysAccount(sysAccount));
    }

    /**
     * 删除
     */
    @PreAuthorize("@ss.hasPermi('system:account:remove')")
    @Log(title = "", businessType = BusinessType.DELETE)
    @DeleteMapping("/{sysAccountIds}")
    public AjaxResult remove(@PathVariable Long[] sysAccountIds)
    {
        return toAjax(sysAccountService.deleteSysAccountBySysAccountIds(sysAccountIds));
    }
}
