package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.SysChromeUser;

/**
 * Service接口
 * 
 * @author ruoyi
 * @date 2024-11-06
 */
public interface ISysChromeUserService 
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
     * 批量删除
     * 
     * @param userIds 需要删除的主键集合
     * @return 结果
     */
    public int deleteSysChromeUserByUserIds(Long[] userIds);

    /**
     * 删除信息
     * 
     * @param userId 主键
     * @return 结果
     */
    public int deleteSysChromeUserByUserId(Long userId);
}
