package com.ruoyi.system.service;

import com.ruoyi.system.domain.SysAccountDetail;

import java.util.List;

/**
 * Service接口
 * 
 * @author ruoyi
 * @date 2024-07-17
 */
public interface ISysAccountDetailService
{
    /**
     * 查询
     * 
     * @param leverId 主键
     * @return 
     */
    public SysAccountDetail selectAccountDetailByLeverId(Long leverId);

    /**
     * 查询列表
     * 
     * @param accountDetail 
     * @return 集合
     */
    public List<SysAccountDetail> selectAccountDetailList(SysAccountDetail accountDetail);

    /**
     * 新增
     * 
     * @param accountDetail 
     * @return 结果
     */
    public int insertAccountDetail(SysAccountDetail accountDetail);

    /**
     * 修改
     * 
     * @param accountDetail 
     * @return 结果
     */
    public int updateAccountDetail(SysAccountDetail accountDetail);

    /**
     * 批量删除
     * 
     * @param leverIds 需要删除的主键集合
     * @return 结果
     */
    public int deleteAccountDetailByLeverIds(Long[] leverIds);

    /**
     * 删除信息
     * 
     * @param leverId 主键
     * @return 结果
     */
    public int deleteAccountDetailByLeverId(Long leverId);
}
