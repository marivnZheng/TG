package com.ruoyi.system.service.impl;

import com.ruoyi.system.domain.SysAccountDetail;
import com.ruoyi.system.mapper.SysAccountDetailMapper;
import com.ruoyi.system.service.ISysAccountDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service业务层处理
 * 
 * @author ruoyi
 * @date 2024-07-17
 */
@Service
public class SysAccountDetailServiceImpl implements ISysAccountDetailService
{
    @Autowired
    private SysAccountDetailMapper accountDetailMapper;

    /**
     * 查询
     * 
     * @param leverId 主键
     * @return 
     */
    @Override
    public SysAccountDetail selectAccountDetailByLeverId(Long leverId)
    {
        return accountDetailMapper.selectAccountDetailByLeverId(leverId);
    }

    /**
     * 查询列表
     * 
     * @param accountDetail 
     * @return 
     */
    @Override
    public List<SysAccountDetail> selectAccountDetailList(SysAccountDetail accountDetail)
    {
        return accountDetailMapper.selectAccountDetailList(accountDetail);
    }

    /**
     * 新增
     * 
     * @param accountDetail 
     * @return 结果
     */
    @Override
    public int insertAccountDetail(SysAccountDetail accountDetail)
    {
        return accountDetailMapper.insertAccountDetail(accountDetail);
    }

    /**
     * 修改
     * 
     * @param accountDetail 
     * @return 结果
     */
    @Override
    public int updateAccountDetail(SysAccountDetail accountDetail)
    {
        return accountDetailMapper.updateAccountDetail(accountDetail);
    }

    /**
     * 批量删除
     * 
     * @param leverIds 需要删除的主键
     * @return 结果
     */
    @Override
    public int deleteAccountDetailByLeverIds(Long[] leverIds)
    {
        return accountDetailMapper.deleteAccountDetailByLeverIds(leverIds);
    }

    /**
     * 删除信息
     * 
     * @param leverId 主键
     * @return 结果
     */
    @Override
    public int deleteAccountDetailByLeverId(Long leverId)
    {
        return accountDetailMapper.deleteAccountDetailByLeverId(leverId);
    }
}
