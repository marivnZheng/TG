package com.ruoyi.system.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
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
 * 对象 sys_account
 * 
 * @author ruoyi
 * @date 2024-05-09
 */

@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
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


}
