package com.ruoyi.system.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

import java.util.List;

/**
 * 对象 sys_concats
 * 
 * @author ruoyi
 * @date 2024-05-11
 */
public class SysContact extends BaseEntity
{
    private static final long serialVersionUID = 1L;


    private Long sysContactId;

    private Long sysAccountId;

    private Long sysUserId;

    @Excel(name = "名称")
    private String sysContactName;

    @Excel(name = "用户名")
    private String sysContactUserName;

    private Long sysMutualContact;

    private Long sysStatus;

    @Excel(name = "手机号")
    private String sysContactPhone;

    private String userNameAdd;

    private List<String> sysAccountIds;

    public String getSysContactPhone() {
        return sysContactPhone;
    }

    public void setSysContactPhone(String sysContactPhone) {
        this.sysContactPhone = sysContactPhone;
    }

    public void setSysContactId(Long sysContactId)
    {
        this.sysContactId = sysContactId;
    }

    public Long getSysContactId()
    {
        return sysContactId;
    }
    public void setSysAccountId(Long sysAccountId) 
    {
        this.sysAccountId = sysAccountId;
    }

    public Long getSysAccountId() 
    {
        return sysAccountId;
    }
    public void setSysUserId(Long sysUserId) 
    {
        this.sysUserId = sysUserId;
    }

    public Long getSysUserId() 
    {
        return sysUserId;
    }
    public void setSysContactName(String sysContactName)
    {
        this.sysContactName = sysContactName;
    }

    public String getSysContactName()
    {
        return sysContactName;
    }
    public void setSysContactUserName(String sysContactUserName)
    {
        this.sysContactUserName = sysContactUserName;
    }

    public String getSysContactUserName()
    {
        return sysContactUserName;
    }
    public void setSysMutualContact(Long sysMutualContact) 
    {
        this.sysMutualContact = sysMutualContact;
    }

    public Long getSysMutualContact()
    {
        return sysMutualContact;
    }
    public void setSysStatus(Long sysStatus) 
    {
        this.sysStatus = sysStatus;
    }

    public Long getSysStatus() 
    {
        return sysStatus;
    }

    public String getUserNameAdd() {
        return userNameAdd;
    }

    public void setUserNameAdd(String userNameAdd) {
        this.userNameAdd = userNameAdd;
    }

    public List<String> getSysAccountIds() {

        return sysAccountIds;
    }

    public void setSysAccountIds(List<String> sysAccountIds) {
        this.sysAccountIds = sysAccountIds;
    }

    @Override
    public String toString() {
        return "SysContact{" +
                "sysContactId=" + sysContactId +
                ", sysAccountId=" + sysAccountId +
                ", sysUserId=" + sysUserId +
                ", sysContactName='" + sysContactName + '\'' +
                ", sysContactUserName='" + sysContactUserName + '\'' +
                ", sysMutualContact=" + sysMutualContact +
                ", sysStatus=" + sysStatus +
                ", sysContactPhone='" + sysContactPhone + '\'' +
                ", userNameAdd='" + userNameAdd + '\'' +
                ", sysAccountIds=" + sysAccountIds +
                '}';
    }
}
