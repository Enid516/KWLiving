package com.enid.kwliving.model;

import java.net.Socket;

/**
 * Created by big_love on 2016/12/21.
 */

public class RoleModel{
    private RoleEnum roleEnum;
    private String roleName;

    public RoleModel(RoleEnum roleEnum, String roleName) {
        this.roleEnum = roleEnum;
        this.roleName = roleName;
    }

    public RoleEnum getRoleEnum() {
        return roleEnum;
    }

    public void setRoleEnum(RoleEnum roleEnum) {
        this.roleEnum = roleEnum;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }
}
