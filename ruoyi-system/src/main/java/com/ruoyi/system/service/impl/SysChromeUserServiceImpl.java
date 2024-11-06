package com.ruoyi.system.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.SysChromeUserMapper;
import com.ruoyi.system.domain.SysChromeUser;
import com.ruoyi.system.service.ISysChromeUserService;

/**
 * Service业务层处理
 * 
 * @author ruoyi
 * @date 2024-11-06
 */
@Service
public class SysChromeUserServiceImpl implements ISysChromeUserService 
{
    @Autowired
    private SysChromeUserMapper sysChromeUserMapper;

    /**
     * 查询
     * 
     * @param userId 主键
     * @return 
     */
    @Override
    public SysChromeUser selectSysChromeUserByUserId(Long userId)
    {
        return sysChromeUserMapper.selectSysChromeUserByUserId(userId);
    }

    /**
     * 查询列表
     * 
     * @param sysChromeUser 
     * @return 
     */
    @Override
    public List<SysChromeUser> selectSysChromeUserList(SysChromeUser sysChromeUser)
    {
        return sysChromeUserMapper.selectSysChromeUserList(sysChromeUser);
    }

    /**
     * 新增
     * 
     * @param sysChromeUser 
     * @return 结果
     */
    @Override
    public int insertSysChromeUser(SysChromeUser sysChromeUser)
    {
        return sysChromeUserMapper.insertSysChromeUser(sysChromeUser);
    }

    /**
     * 修改
     * 
     * @param sysChromeUser 
     * @return 结果
     */
    @Override
    public int updateSysChromeUser(SysChromeUser sysChromeUser)
    {
        return sysChromeUserMapper.updateSysChromeUser(sysChromeUser);
    }

    /**
     * 批量删除
     * 
     * @param userIds 需要删除的主键
     * @return 结果
     */
    @Override
    public int deleteSysChromeUserByUserIds(Long[] userIds)
    {
        return sysChromeUserMapper.deleteSysChromeUserByUserIds(userIds);
    }

    /**
     * 删除信息
     * 
     * @param userId 主键
     * @return 结果
     */
    @Override
    public int deleteSysChromeUserByUserId(Long userId)
    {
        return sysChromeUserMapper.deleteSysChromeUserByUserId(userId);
    }
}
