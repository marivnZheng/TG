package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.SysChromeUser;

/**
 * Mapper接口
 * 
 * @author ruoyi
 * @date 2024-11-06
 */
public interface SysChromeUserMapper 
{
    /**
     * 查询
     * 
     * @param userId 主键
     * @return 
     */
    public SysChromeUser selectSysChromeUserByUserId(Long userId);

    /**
     * 查询列表
     * 
     * @param sysChromeUser 
     * @return 集合
     */
    public List<SysChromeUser> selectSysChromeUserList(SysChromeUser sysChromeUser);

    /**
     * 新增
     * 
     * @param sysChromeUser 
     * @return 结果
     */
    public int insertSysChromeUser(SysChromeUser sysChromeUser);

    /**
     * 修改
     * 
     * @param sysChromeUser 
     * @return 结果
     */
    public int updateSysChromeUser(SysChromeUser sysChromeUser);

    /**
     * 删除
     * 
     * @param userId 主键
     * @return 结果
     */
    public int deleteSysChromeUserByUserId(Long userId);

    /**
     * 批量删除
     * 
     * @param userIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteSysChromeUserByUserIds(Long[] userIds);
}
