package com.ruoyi.system.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class InviteGroupDTO {

    private List<SysContact> concatsList;

    private Long groupId;

    private Long sysAccountId;

    private Long min;

    private Long minCount;
}
