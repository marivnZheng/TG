package com.ruoyi.system.service;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.system.domain.InviteGroupDTO;
import com.ruoyi.system.domain.SysAccount;
import com.ruoyi.system.domain.SysContact;
import com.ruoyi.system.domain.SysGroup;

/**
 * Service接口
 * 
 * @author ruoyi
 * @date 2024-05-14
 */
public interface ISysGroupService 
{


    /**
     * 查询列表
     * 
     * @param sysGroup 
     * @return 集合
     */
    public List<SysGroup> selectSysGroupList(SysGroup sysGroup);

    public List<SysGroup> selectSysGroupAllList(SysGroup sysGroup);

    /**
     * 新增
     * 
     * @param sysGroup 
     * @return 结果
     */
    public int insertSysGroup(SysGroup sysGroup);

    /**
     * 修改
     * 
     * @param sysGroup 
     * @return 结果
     */
    public int updateSysGroup(SysGroup sysGroup);

    /**
     * 批量删除
     * 
     * @param sysGroupIds 需要删除的主键集合
     * @return 结果
     */
    public int deleteSysGroupBySysGroupIds(Long[] sysGroupIds);

    /**
     * 删除信息
     * 
     * @param sysGroupId 主键
     * @return 结果
     */
    public int deleteSysGroupBySysGroupId(Long sysGroupId);


    public AjaxResult syncGroup(List<SysAccount> accountList);

    public AjaxResult inviteGroup(InviteGroupDTO dto) throws InterruptedException, IOException;

    public AjaxResult inviteGroupList(HashMap map);


    public AjaxResult addConcats(HashMap map) throws InterruptedException, IOException;


    public List<SysContact> getGroupMember(SysGroup sysGroup);
}
