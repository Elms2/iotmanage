package com.wteam.framework.common.security;

 import com.wteam.framework.common.security.enums.UserEnums;
 import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author Khai(951992121 @ qq.com)
 * @date 2022/9/4 3:38 PM
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthUser implements Serializable {

    private static final long serialVersionUID = 582441893336003319L;

    /**
     * 用户名
     */
    private String username;

    /**
     * 昵称
     */
    private String nickName;

    /**
     * 头像
     */
    private String face;

    /**
     * id
     */
    private String id;

    /**
     * 长期有效（用于手机app登录场景或者信任场景等）
     */
    private Boolean longTerm = false;

    /**
     * @see UserEnums
     * 角色
     */
    private UserEnums role;

    /**
     * 如果角色是商家，则存在此店铺id字段
     * storeId
     */
    private String storeId;

    /**
     * 如果角色是商家，则存在此店铺名称字段
     * storeName
     */
    private String storeName;

    /**
     * 是否是超级管理员
     */
    private Boolean isSuper = false;

    public AuthUser(String username, String id, String nickName, String face, UserEnums role) {
        this.username = username;
        this.face = face;
        this.id = id;
        this.role = role;
        this.nickName = nickName;
    }

    public AuthUser(String username, String id, String face, UserEnums manager, String nickName, Boolean isSuper) {
        this.username = username;
        this.id = id;
        this.face = face;
        this.role = manager;
        this.isSuper = isSuper;
        this.nickName = nickName;
    }


}
