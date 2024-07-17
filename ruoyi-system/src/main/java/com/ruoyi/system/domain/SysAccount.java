package com.ruoyi.system.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 对象 sys_account
 * 
 * @author ruoyi
 * @date 2024-05-09
 */
public class SysAccount extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 账号id */
    private Long sysAccountId;

    /** 用户id */
    @Excel(name = "用户id")
    private Long sysUserId;

    /** 账号名称 */
    @Excel(name = "账号名称")
    private String sysAccountName;

    /** 账号名称 */
    @Excel(name = "账号名")
    private String sysAccountLastName;

    /** 账号名称 */
    @Excel(name = "账号姓")
    private String sysAccountFirstName;

    /** 账号电话 */
    @Excel(name = "账号电话")
    private String sysAccountPhone;

    /** 账号会话文件 */
    @Excel(name = "账号会话文件")
    private String sysAccountSessionFile;

    /** 账号会话文本 */
    @Excel(name = "账号会话文本")
    private String sysAccountStringSession;

    /** 账号联系人数量 */
    @Excel(name = "账号联系人数量")
    private Long sysAccountConcatsNumber;

    /** 账号群组数量 */
    @Excel(name = "账号群组数量")
    private Long sysAccountGroupNumber;

    /** 账号新增时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "账号新增时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date sysAccountCreateTime;

    /** 账号新增时间时区 */
    @Excel(name = "账号新增时间时区")
    private String sysAccountCreateTimezone;

    private int sysAccountOnline;

    private String sysAccountAbout;

    private int sysAccountLoginStatus;

    public int getSysAccountLoginStatus() {
        return sysAccountLoginStatus;
    }

    public void setSysAccountLoginStatus(int sysAccountLoginStatus) {
        this.sysAccountLoginStatus = sysAccountLoginStatus;
    }

    public String getSysAccountAbout() {
        return sysAccountAbout;
    }

    public void setSysAccountAbout(String sysAccountAbout) {
        this.sysAccountAbout = sysAccountAbout;
    }

    public int getSysAccountOnline() {
        return sysAccountOnline;
    }

    public void setSysAccountOnline(int sysAccountOnline) {
        this.sysAccountOnline = sysAccountOnline;
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
    public void setSysAccountName(String sysAccountName) 
    {
        this.sysAccountName = sysAccountName;
    }

    public String getSysAccountName() 
    {
        return sysAccountName;
    }
    public void setSysAccountPhone(String sysAccountPhone) 
    {
        this.sysAccountPhone = sysAccountPhone;
    }

    public String getSysAccountPhone() 
    {
        return sysAccountPhone;
    }
    public void setSysAccountSessionFile(String sysAccountSessionFile) 
    {
        this.sysAccountSessionFile = sysAccountSessionFile;
    }

    public String getSysAccountSessionFile() 
    {
        return sysAccountSessionFile;
    }
    public void setSysAccountStringSession(String sysAccountStringSession) 
    {
        this.sysAccountStringSession = sysAccountStringSession;
    }

    public String getSysAccountStringSession() 
    {
        return sysAccountStringSession;
    }
    public void setSysAccountConcatsNumber(Long sysAccountConcatsNumber) 
    {
        this.sysAccountConcatsNumber = sysAccountConcatsNumber;
    }

    public Long getSysAccountConcatsNumber() 
    {
        return sysAccountConcatsNumber;
    }
    public void setSysAccountGroupNumber(Long sysAccountGroupNumber) 
    {
        this.sysAccountGroupNumber = sysAccountGroupNumber;
    }

    public String getSysAccountLastName() {
        return sysAccountLastName;
    }

    public void setSysAccountLastName(String sysAccountLastName) {
        this.sysAccountLastName = sysAccountLastName;
    }

    public String getSysAccountFirstName() {
        return sysAccountFirstName;
    }

    public void setSysAccountFirstName(String sysAccountFirstName) {
        this.sysAccountFirstName = sysAccountFirstName;
    }

    public Long getSysAccountGroupNumber()
    {
        return sysAccountGroupNumber;
    }
    public void setSysAccountCreateTime(Date sysAccountCreateTime) 
    {
        this.sysAccountCreateTime = sysAccountCreateTime;
    }

    public Date getSysAccountCreateTime() 
    {
        return sysAccountCreateTime;
    }
    public void setSysAccountCreateTimezone(String sysAccountCreateTimezone) 
    {
        this.sysAccountCreateTimezone = sysAccountCreateTimezone;
    }

    public String getSysAccountCreateTimezone() 
    {
        return sysAccountCreateTimezone;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("sysAccountId", getSysAccountId())
            .append("sysUserId", getSysUserId())
            .append("sysAccountName", getSysAccountName())
            .append("sysAccountFirstName", getSysAccountFirstName())
            .append("sysAccountLastName", getSysAccountLastName())
            .append("sysAccountPhone", getSysAccountPhone())
            .append("sysAccountSessionFile", getSysAccountSessionFile())
            .append("sysAccountStringSession", getSysAccountStringSession())
            .append("sysAccountConcatsNumber", getSysAccountConcatsNumber())
            .append("sysAccountGroupNumber", getSysAccountGroupNumber())
            .append("sysAccountCreateTime", getSysAccountCreateTime())
            .append("sysAccountCreateTimezone", getSysAccountCreateTimezone())
            .toString();
    }
}
