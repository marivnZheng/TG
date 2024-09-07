package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.SysGroup;
import org.apache.ibatis.annotations.Param;

/**
 * Mapper接口
 * 
 * @author ruoyi
 * @date 2024-05-14
 */
public interface SysGroupMapper 
{
    /**
     * 查询
     * 
     * @param sysGroupId 主键
     * @return 
     */
    public SysGroup selectSysGroupBySysGroupId(@Param("sysGroupId") Long sysGroupId,@Param("sysAccountId") Long sysAccountId);


    public List<SysGroup> selectSysGroupByAccountString(String StringSession);

    /**
     * 查询列表
     * 
     * @param sysGroup 
     * @return 集合
     */
    public List<SysGroup> selectSysGroupList(SysGroup sysGroup);

    public List<SysGroup> selectGroupAll(SysGroup sysGroup);

    /**
     * 新增
     * 
     * @param sysGroup 
     * @return 结果
     */
    public int insertSysGroup(SysGroup sysGroup);


    public int batchSysGroup(List<SysGroup> list);

    /**
     * 修改
     * 
     * @param sysGroup 
     * @return 结果
     */
    public int updateSysGroup(SysGroup sysGroup);

    /**
     * 删除
     * 
     * @param sysGroupId 主键
     * @return 结果
     */
    public int deleteSysGroupBySysGroupId(Long sysGroupId);


    /**
     * 删除
     *
     * @param sysAccountId 主键
     * @return 结果
     */
    public int deleteSysContactByAccountId(Long sysAccountId);



    /**
     * 批量删除
     * 
     * @param sysGroupIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteSysGroupBySysGroupIds(Long[] sysGroupIds);
}
