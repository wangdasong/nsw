/********************************************************************
 *
 * Copyright 2016-2017 CNPL(CPSCS).Co.Ltd All rights reserved
 *
 ********************************************************************/
package nsw.base.core.dao.entity;
import nsw.base.core.dao.entity.base.BaseEntity;
import nsw.base.core.dao.entity.base.FrameworkDbDialogConfig;

public class Permission extends BaseEntity implements FrameworkDbDialogConfig {

    private String permissionName;
    private String permission;
    private String description;
    private short state;
    private String resourceCount;

    private String roleId;

    public String getPermissionName() {
        return permissionName;
    }

    public String getPermission() {
        return permission;
    }

    public String getDescription() {
        return description;
    }

    public double getState() {
        return state;
    }



    public void setPermissionName(String permissionName) {
        this.permissionName = permissionName;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setState(short state) {
        this.state = state;
    }

	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	public String getResourceCount() {
		return resourceCount;
	}

	public void setResourceCount(String resourceCount) {
		this.resourceCount = resourceCount;
	}



}
