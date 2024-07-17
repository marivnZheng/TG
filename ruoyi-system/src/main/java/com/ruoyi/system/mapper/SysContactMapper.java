package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.SysContact;

/**
 * Mapper接口
 * 
 * @author ruoyi
 * @date 2024-05-11
 */
public interface SysContactMapper
{
    /**
     * 查询
     * 
     * @param sysContactId 主键
     * @return 
     */
    public SysContact selectSysContactBySysContactId(Long sysContactId);

    /**
     * 查询列表
     * 
     * @param sysContact
     * @return 集合
     */
    public List<SysContact> selectSysContactList(SysContact sysContact);

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

    public List<SysContact> selectSysContactBySysAccount(long accountId);

    /**
     * 删除
     * 
     * @param sysContactId 主键
     * @return 结果
     */
    public int deleteSysContactBySysContactId(long sysContactId);

    /**
     * 批量删除
     * 
     * @param sysContactIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteSysContactBySysContactIds(Long[] sysContactIds);


    public int batchInsertSysContact(List<SysContact> list);

}
