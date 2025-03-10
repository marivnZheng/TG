package com.ruoyi.system.service;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.system.domain.SysAccount;
import com.ruoyi.system.dto.TgLogin;
import org.springframework.web.multipart.MultipartFile;

/**
 * Service接口
 * 
 * @author ruoyi
 * @date 2024-05-09
 */
public interface ISysAccountService 
{
    /**
     * 查询
     * 
     * @param sysAccountId 主键
     * @return
     */
    public SysAccount selectSysAccountBySysAccountId(Long sysAccountId);



    /**
     * 查询列表
     * 
     * @param sysAccount
     * @return 集合
     */
    public List<SysAccount> selectSysAccountList(SysAccount sysAccount);


    public List<SysAccount> selectListOnline(SysAccount sysAccount);

    /**
     * 新增
     * 
     * @param sysAccount
     * @return 结果
     */
    public int insertSysAccount(SysAccount sysAccount);

    /**
     * 修改
     * 
     * @param sysAccount
     * @return 结果
     */
    public int updateSysAccount(SysAccount sysAccount);

    /**
     * 批量删除
     * 
     * @param sysAccountIds 需要删除的主键集合
     * @return 结果
     */
    public int deleteSysAccountBySysAccountIds(Long[] sysAccountIds);



    public TgLogin sendPhoneCode(TgLogin tgLogin) throws InterruptedException, IOException;



    public AjaxResult checkPhoneAndUserJurisdiction(String phoneNumber);

    public AjaxResult loginAccountByPhoneCode(TgLogin tgLogin) throws InterruptedException, IOException;

    public AjaxResult sessionFileUpload(MultipartFile file) throws InterruptedException, IOException;

    public AjaxResult syncAccount(List<SysAccount> list) throws InterruptedException, IOException;

    public AjaxResult sessionFileLogin(String map) throws InterruptedException, IOException;

    public AjaxResult updateListAccountOnline(List<SysAccount> list) throws InterruptedException, IOException;

    public AjaxResult editAccountDetail(SysAccount file) throws InterruptedException, IOException;


}
