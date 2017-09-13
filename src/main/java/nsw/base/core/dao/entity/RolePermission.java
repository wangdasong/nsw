/********************************************************************
 *
 * Copyright 2016-2017 CNPL(CPSCS).Co.Ltd All rights reserved
 *
 ********************************************************************/
package nsw.base.core.dao.entity;
import nsw.base.core.dao.entity.base.BaseEntity;
import nsw.base.core.dao.entity.base.FrameworkDbDialogConfig;

public class RolePermission extends BaseEntity implements FrameworkDbDialogConfig {

    private String roleId;
    private String permissionId;
    private Permission permission;


    public String getRoleId() {
        return roleId;
    }

    public String getPermissionId() {
        return permissionId;
    }



    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public void setPermissionId(String permissionId) {
        this.permissionId = permissionId;
    }

	public Permission getPermission() {
		return permission;
	}

	public void setPermission(Permission permission) {
		this.permission = permission;
	}



}
