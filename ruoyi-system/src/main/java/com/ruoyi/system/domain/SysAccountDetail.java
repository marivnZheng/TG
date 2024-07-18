package com.ruoyi.system.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 对象 account_detail
 * 
 * @author ruoyi
 * @date 2024-07-17
 */

@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class SysAccountDetail extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** id */
    private Long leverId;

    /** 价格 */
    @Excel(name = "价格")
    private Long price;

    /** 账号数量 */
    @Excel(name = "账号数量")
    private Long accountNum;

    /** 能否群发消息 */
    @Excel(name = "能否群发消息")
    private Long sendGroupMessage;

    /** 能否加群 */
    @Excel(name = "能否加群")
    private Long addGroup;

    /** 能否私聊 */
    @Excel(name = "能否私聊")
    private Long privateSendMessage;

    /** 能否查找附近的人 */
    @Excel(name = "能否查找附近的人")
    private Long findNearbyPeople;

    /** 能否拉人进群 */
    @Excel(name = "能否拉人进群")
    private Long pullPeopleJoinGroup;

    /** 能否自动回复信息 */
    @Excel(name = "能否自动回复信息")
    private Long autoReplayMessage;

    /** 独立服务器 */
    @Excel(name = "独立服务器")
    private Long privateDevice;

    private String vipType;

    private Long forwardMessage;


}
