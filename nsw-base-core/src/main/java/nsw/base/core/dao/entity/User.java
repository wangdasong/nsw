/********************************************************************
 *
 * Copyright 2016-2017 CNPL(CPSCS).Co.Ltd All rights reserved
 *
 ********************************************************************/
package nsw.base.core.dao.entity;
import java.util.Date;
import java.util.List;

import nsw.base.core.dao.entity.base.BaseEntity;
import nsw.base.core.utils.JsonDateSerializer;

import org.codehaus.jackson.map.annotate.JsonSerialize;

public class User extends BaseEntity {

	private String username;
    private String password;
    private Short state;
    private String fullname;
    private String mobilephone;
    private String email;
    private Date birthday;
    private List<Role> roleList;
    private String roleCount;

    
    public String getUsername() {
        return username == null ? null : username.trim();
    }

    public String getPassword() {
        return password == null ? null : password.trim();
    }
	
    public Short getState() {
        return state;
    }

    public String getFullname() {
        return fullname;
    }

    public String getMobilephone() {
        return mobilephone;
    }

    public String getEmail() {
        return email;
    }

	@JsonSerialize(using=JsonDateSerializer.class) 
    public Date getBirthday() {
        return birthday;
    }

    public void setUsername(String username) {
        this.username = username == null ? null : username.trim();
    }

    public void setPassword(String password) {
    	this.password = password == null ? null : password.trim();
    }

    public void setState(Short state) {
        this.state = state;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public void setMobilephone(String mobilephone) {
        this.mobilephone = mobilephone;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

	public List<Role> getRoleList() {
		return roleList;
	}

	public void setRoleList(List<Role> roleList) {
		this.roleList = roleList;
	}

	public String getRoleCount() {
		return roleCount;
	}

	public void setRoleCount(String roleCount) {
		this.roleCount = roleCount;
	}

    
}
