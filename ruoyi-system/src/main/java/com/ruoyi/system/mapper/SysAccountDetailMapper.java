package com.ruoyi.system.mapper;

import com.ruoyi.system.domain.SysAccountDetail;

import java.util.List;

/**
 * Mapper接口
 * 
 * @author ruoyi
 * @date 2024-07-17
 */
public interface SysAccountDetailMapper
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
     * 删除
     * 
     * @param leverId 主键
     * @return 结果
     */
    public int deleteAccountDetailByLeverId(Long leverId);

    /**
     * 批量删除
     * 
     * @param leverIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteAccountDetailByLeverIds(Long[] leverIds);
}
