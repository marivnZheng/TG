package com.ruoyi.system.domain;

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


    public Long getForwardMessage() {
        return forwardMessage;
    }

    public void setForwardMessage(Long forwardMessage) {
        this.forwardMessage = forwardMessage;
    }

    public String getVipType() {
        return vipType;
    }

    public void setVipType(String vipType) {
        this.vipType = vipType;
    }

    public void setLeverId(Long leverId)
    {
        this.leverId = leverId;
    }

    public Long getLeverId() 
    {
        return leverId;
    }
    public void setPrice(Long price) 
    {
        this.price = price;
    }

    public Long getPrice() 
    {
        return price;
    }
    public void setAccountNum(Long accountNum) 
    {
        this.accountNum = accountNum;
    }

    public Long getAccountNum() 
    {
        return accountNum;
    }
    public void setSendGroupMessage(Long sendGroupMessage) 
    {
        this.sendGroupMessage = sendGroupMessage;
    }

    public Long getSendGroupMessage() 
    {
        return sendGroupMessage;
    }
    public void setAddGroup(Long addGroup) 
    {
        this.addGroup = addGroup;
    }

    public Long getAddGroup() 
    {
        return addGroup;
    }
    public void setPrivateSendMessage(Long privateSendMessage) 
    {
        this.privateSendMessage = privateSendMessage;
    }

    public Long getPrivateSendMessage() 
    {
        return privateSendMessage;
    }
    public void setFindNearbyPeople(Long findNearbyPeople) 
    {
        this.findNearbyPeople = findNearbyPeople;
    }

    public Long getFindNearbyPeople() 
    {
        return findNearbyPeople;
    }
    public void setPullPeopleJoinGroup(Long pullPeopleJoinGroup) 
    {
        this.pullPeopleJoinGroup = pullPeopleJoinGroup;
    }

    public Long getPullPeopleJoinGroup() 
    {
        return pullPeopleJoinGroup;
    }
    public void setAutoReplayMessage(Long autoReplayMessage) 
    {
        this.autoReplayMessage = autoReplayMessage;
    }

    public Long getAutoReplayMessage() 
    {
        return autoReplayMessage;
    }
    public void setPrivateDevice(Long privateDevice) 
    {
        this.privateDevice = privateDevice;
    }

    public Long getPrivateDevice() 
    {
        return privateDevice;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("leverId", getLeverId())
            .append("price", getPrice())
            .append("accountNum", getAccountNum())
            .append("sendGroupMessage", getSendGroupMessage())
            .append("addGroup", getAddGroup())
            .append("privateSendMessage", getPrivateSendMessage())
            .append("findNearbyPeople", getFindNearbyPeople())
            .append("pullPeopleJoinGroup", getPullPeopleJoinGroup())
            .append("autoReplayMessage", getAutoReplayMessage())
            .append("privateDevice", getPrivateDevice())
            .toString();
    }
}
