package com.ruoyi.web.controller.system;

import java.util.List;
import javax.servlet.http.HttpServletResponse;
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
import com.ruoyi.system.domain.SysChromeUser;
import com.ruoyi.system.service.ISysChromeUserService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * Controller
 * 
 * @author ruoyi
 * @date 2024-11-06
 */
@RestController
@RequestMapping("/system/ChromeUser")
public class SysChromeUserController extends BaseController
{
    @Autowired
    private ISysChromeUserService sysChromeUserService;

    /**
     * 查询列表
     */
    @PreAuthorize("@ss.hasPermi('system:user:list')")
    @GetMapping("/list")
    public TableDataInfo list(SysChromeUser sysChromeUser)
    {
        startPage();
        List<SysChromeUser> list = sysChromeUserService.selectSysChromeUserList(sysChromeUser);
        return getDataTable(list);
    }

    /**
     * 导出列表
     */
    @PreAuthorize("@ss.hasPermi('system:user:export')")
    @Log(title = "", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, SysChromeUser sysChromeUser)
    {
        List<SysChromeUser> list = sysChromeUserService.selectSysChromeUserList(sysChromeUser);
        ExcelUtil<SysChromeUser> util = new ExcelUtil<SysChromeUser>(SysChromeUser.class);
        util.exportExcel(response, list, "数据");
    }

    /**
     * 获取详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:user:query')")
    @GetMapping(value = "/{userId}")
    public AjaxResult getInfo(@PathVariable("userId") Long userId)
    {
        return success(sysChromeUserService.selectSysChromeUserByUserId(userId));
    }

    /**
     * 新增
     */
    @PreAuthorize("@ss.hasPermi('system:user:add')")
    @Log(title = "", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SysChromeUser sysChromeUser)
    {
        return toAjax(sysChromeUserService.insertSysChromeUser(sysChromeUser));
    }

    /**
     * 修改
     */
    @PreAuthorize("@ss.hasPermi('system:user:edit')")
    @Log(title = "", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SysChromeUser sysChromeUser)
    {
        return toAjax(sysChromeUserService.updateSysChromeUser(sysChromeUser));
    }

    /**
     * 删除
     */
    @PreAuthorize("@ss.hasPermi('system:user:remove')")
    @Log(title = "", businessType = BusinessType.DELETE)
	@DeleteMapping("/{userIds}")
    public AjaxResult remove(@PathVariable Long[] userIds)
    {
        return toAjax(sysChromeUserService.deleteSysChromeUserByUserIds(userIds));
    }
}
