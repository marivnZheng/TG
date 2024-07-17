package com.ruoyi.system.service;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.system.domain.SysAccount;
import com.ruoyi.system.domain.SysContact;

/**
 * Service接口
 * 
 * @author ruoyi
 * @date 2024-05-11
 */
public interface ISysContactService
{
    /**
     * 查询
     * 
     * @param sysContactId 主键
     * @return 
     */
    public SysContact selectSysContactBySysContactId(Long sysContactId);



    public AjaxResult syncContact(List<SysAccount> sysAccountList);

    /**
     * 查询列表
     * 
     * @param sysContact
     * @return 集合
     */
    public List<SysContact> selectSysContactList(SysContact sysContact);


    public AjaxResult addContact(SysContact contact) throws InterruptedException, IOException;

    public AjaxResult addContactTgPhone(SysContact contact) throws InterruptedException, IOException;



    /**
     * 新增
     * 
     * @param sysContact
     * @return 结果
     */
    public int insertSysContact(SysContact sysContact);

    /**
     * 修改
     * 
     * @param sysContact
     * @return 结果
     */
    public int updateSysContact(SysContact sysContact);

    /**
     * 批量删除
     * 
     * @param sysContactIds 需要删除的主键集合
     * @return 结果
     */
    public int deleteSysContactBySysContactIds(Long[] sysContactIds);

    /**
     * 删除信息
     * 
     * @param sysContactId 主键
     * @return 结果
     */
    public int deleteSysContactBySysContactId(Long sysContactId);
}
