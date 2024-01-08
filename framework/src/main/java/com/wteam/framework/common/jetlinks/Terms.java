package com.wteam.framework.common.jetlinks;

import lombok.Data;

/**
 * @author Khai(951992121 @ qq.com)
 * @date 2023/1/18 9:17 PM
 */
@Data
public class Terms {
    /**
     * termType: "eq", column: "id", value: "dasdada"
     */
    private String termType;

    private String column;

    private String value;

    private String type;
}
