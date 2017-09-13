/********************************************************************
 *
 * Copyright 2016-2017 CNPL(CPSCS).Co.Ltd All rights reserved
 *
 ********************************************************************/
package nsw.base.core.dao.entity;
import nsw.base.core.dao.entity.base.BaseEntity;
import nsw.base.core.dao.entity.base.FrameworkDbDialogConfig;

public class Role extends BaseEntity implements FrameworkDbDialogConfig {

    private String roleName;
    private String roleType;
    private String description;
    private Short state;


    public String getRoleName() {
        return roleName;
    }

    public String getRoleType() {
        return roleType;
    }

    public String getDescription() {
        return description;
    }

    public Short getState() {
        return state;
    }



    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public void setRoleType(String roleType) {
        this.roleType = roleType;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setState(Short state) {
        this.state = state;
    }



}
