package com.ruoyi.system.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 对象 sys_group
 * 
 * @author ruoyi
 * @date 2024-05-14
 */
public class SysGroup extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 群id */
    private Long sysGroupId;

    /** 系统用户id */
    private Long sysUserId;

    /** 群组名称 */
    @Excel(name = "群组名称")
    private String sysGroupTitle;

    /** 群组链接 */
    @Excel(name = "群组链接")
    private String sysGroupLink;

    private String sysGroupDetail;

    @Excel(name = "群组信息")
    private String sysGroupDetailExcel;


    private Long sysGroupSendPhoto;

    /** 禁止发图片 */
    @Excel(name = "禁止发图片")
    private String sysGroupSendPhotoExcel;

    /** 禁止发信息 */

    private Long sysGroupSendMessage;

    @Excel(name = "禁止发信息")
    private String sysGroupSendMessageExcel;

    /** 邀请入群 */

    private Long sysGroupInvite;

    @Excel(name = "邀请入群")
    private String sysGroupInviteExcel;

    private String sysAccountStringSession;

    private Long sysAccountId;


    public Long getSysAccountId() {
        return sysAccountId;
    }

    public void setSysAccountId(Long sysAccountId) {
        this.sysAccountId = sysAccountId;
    }

    public String getSysGroupDetailExcel() {
        return sysGroupDetailExcel;
    }

    public void setSysGroupDetailExcel(String sysGroupDetailExcel) {
        this.sysGroupDetailExcel = sysGroupDetailExcel;
    }

    public String getSysGroupSendPhotoExcel() {
        return sysGroupSendPhotoExcel;
    }

    public void setSysGroupSendPhotoExcel(String sysGroupSendPhotoExcel) {
        this.sysGroupSendPhotoExcel = sysGroupSendPhotoExcel;
    }

    public String getSysGroupSendMessageExcel() {
        return sysGroupSendMessageExcel;
    }

    public void setSysGroupSendMessageExcel(String sysGroupSendMessageExcel) {
        this.sysGroupSendMessageExcel = sysGroupSendMessageExcel;
    }

    public String getSysGroupInviteExcel() {
        return sysGroupInviteExcel;
    }

    public void setSysGroupInviteExcel(String sysGroupInviteExcel) {
        this.sysGroupInviteExcel = sysGroupInviteExcel;
    }

    public String getSysAccountStringSession() {
        return sysAccountStringSession;
    }

    public void setSysAccountStringSession(String sysAccountStringSession) {
        this.sysAccountStringSession = sysAccountStringSession;
    }

    public void setSysGroupId(Long sysGroupId)
    {
        this.sysGroupId = sysGroupId;
    }

    public Long getSysGroupId() 
    {
        return sysGroupId;
    }
    public void setSysUserId(Long sysUserId) 
    {
        this.sysUserId = sysUserId;
    }

    public Long getSysUserId() 
    {
        return sysUserId;
    }
    public void setSysGroupTitle(String sysGroupTitle) 
    {
        this.sysGroupTitle = sysGroupTitle;
    }

    public String getSysGroupTitle() 
    {
        return sysGroupTitle;
    }
    public void setSysGroupLink(String sysGroupLink) 
    {
        this.sysGroupLink = sysGroupLink;
    }

    public String getSysGroupLink() 
    {
        return sysGroupLink;
    }
    public void setSysGroupDetail(String sysGroupDetail) 
    {
        this.sysGroupDetail = sysGroupDetail;
    }

    public String getSysGroupDetail() 
    {
        return sysGroupDetail;
    }
    public void setSysGroupSendPhoto(Long sysGroupSendPhoto) 
    {
        this.sysGroupSendPhoto = sysGroupSendPhoto;
    }

    public Long getSysGroupSendPhoto() 
    {
        return sysGroupSendPhoto;
    }
    public void setSysGroupSendMessage(Long sysGroupSendMessage) 
    {
        this.sysGroupSendMessage = sysGroupSendMessage;
    }

    public Long getSysGroupSendMessage() 
    {
        return sysGroupSendMessage;
    }


    public Long getSysGroupInvite() {
        return sysGroupInvite;
    }

    public void setSysGroupInvite(Long sysGroupInvite) {
        this.sysGroupInvite = sysGroupInvite;
    }


    @Override
    public String toString() {
        return "SysGroup{" +
                "sysGroupId=" + sysGroupId +
                ", sysUserId=" + sysUserId +
                ", sysGroupTitle='" + sysGroupTitle + '\'' +
                ", sysGroupLink='" + sysGroupLink + '\'' +
                ", sysGroupDetail='" + sysGroupDetail + '\'' +
                ", sysGroupSendPhoto=" + sysGroupSendPhoto +
                ", sysGroupSendMessage=" + sysGroupSendMessage +
                ", sysGroupInvite=" + sysGroupInvite +
                ", sysAccountStringSession='" + sysAccountStringSession + '\'' +
                '}';
    }
}
