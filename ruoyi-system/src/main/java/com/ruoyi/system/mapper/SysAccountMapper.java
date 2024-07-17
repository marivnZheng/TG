package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.SysAccount;
import io.lettuce.core.dynamic.annotation.Param;

/**
 * Mapper接口
 * 
 * @author ruoyi
 * @date 2024-05-09
 */
public interface SysAccountMapper 
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


    public int selectCountUserId(Long sysUserId);

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


    public int updateSysAccountLoginStatus(SysAccount sysAccount);


    public int updateSysAccountProFile(SysAccount sysAccount);


    public int updateSysAccountUserName(SysAccount sysAccount);


    /**
     * 删除
     * 
     * @param sysAccountId 主键
     * @return 结果
     */
    public int deleteSysAccountBySysAccountId(Long sysAccountId);

    /**
     * 批量删除
     * 
     * @param sysAccountIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteSysAccountBySysAccountIds(Long[] sysAccountIds);

    public List<SysAccount> selectByStrings(@Param("list") List<String> sysAccountSessionStrings);
}
