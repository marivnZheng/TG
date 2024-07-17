package com.ruoyi.web.controller.system;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.system.domain.SysAccountDetail;
import com.ruoyi.system.service.ISysAccountDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Controller
 * 
 * @author ruoyi
 * @date 2024-07-17
 */
@RestController
@RequestMapping("/system/detail")
public class SysAccountDetailController extends BaseController
{
    @Autowired
    private ISysAccountDetailService accountDetailService;

    /**
     * 查询列表
     */
    @PreAuthorize("@ss.hasPermi('system:detail:list')")
    @GetMapping("/list")
    public TableDataInfo list(SysAccountDetail accountDetail)
    {
        startPage();
        List<SysAccountDetail> list = accountDetailService.selectAccountDetailList(accountDetail);
        return getDataTable(list);
    }

    /**
     * 导出列表
     */
    @PreAuthorize("@ss.hasPermi('system:detail:export')")
    @Log(title = "", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, SysAccountDetail accountDetail)
    {
        List<SysAccountDetail> list = accountDetailService.selectAccountDetailList(accountDetail);
        ExcelUtil<SysAccountDetail> util = new ExcelUtil<SysAccountDetail>(SysAccountDetail.class);
        util.exportExcel(response, list, "数据");
    }

    /**
     * 获取详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:detail:query')")
    @GetMapping(value = "/{leverId}")
    public AjaxResult getInfo(@PathVariable("leverId") Long leverId)
    {
        return success(accountDetailService.selectAccountDetailByLeverId(leverId));
    }

    /**
     * 新增
     */
    @PreAuthorize("@ss.hasPermi('system:detail:add')")
    @Log(title = "", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SysAccountDetail accountDetail)
    {
        return toAjax(accountDetailService.insertAccountDetail(accountDetail));
    }

    /**
     * 修改
     */
    @PreAuthorize("@ss.hasPermi('system:detail:edit')")
    @Log(title = "", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SysAccountDetail accountDetail)
    {
        return toAjax(accountDetailService.updateAccountDetail(accountDetail));
    }

    /**
     * 删除
     */
    @PreAuthorize("@ss.hasPermi('system:detail:remove')")
    @Log(title = "", businessType = BusinessType.DELETE)
	@DeleteMapping("/{leverIds}")
    public AjaxResult remove(@PathVariable Long[] leverIds)
    {
        return toAjax(accountDetailService.deleteAccountDetailByLeverIds(leverIds));
    }
}
