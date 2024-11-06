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
 * 对象 sys_chrome_user
 * 
 * @author ruoyi
 * @date 2024-11-06
 */

@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class SysChromeUser extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    private Long userId;

    private String phoneNumber;

    private String dc;

    private String dc1AuthKey;

    private String dc1Hash;

    private String dc2AuthKey;

    private String dc2Hash;

    private String dc4AuthKey;

    private String dc4Hash;

    private String dc5AuthKey;

    private String dc5Hash;

    private String tgmeSync;

    private String ttActiveTab;

    private String ttMultiTab;

    private String userAuth;


}
