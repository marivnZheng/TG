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

import java.util.List;

/**
 * 对象 sys_concats
 * 
 * @author ruoyi
 * @date 2024-05-11
 */

@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
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


}
