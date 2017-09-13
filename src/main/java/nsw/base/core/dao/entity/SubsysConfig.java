/********************************************************************
 *
 * Copyright 2016-2017 CNPL(CPSCS).Co.Ltd All rights reserved
 *
 ********************************************************************/
package nsw.base.core.dao.entity;
import nsw.base.core.dao.entity.base.BaseEntity;
import nsw.base.core.dao.entity.base.FrameworkDbDialogConfig;

public class SubsysConfig extends BaseEntity implements FrameworkDbDialogConfig {

	private static final long serialVersionUID = -718184143515718096L;

	private String code;
    private String name;
    private String logo;
    private String description;
    private Boolean active = false;


    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public String getLogo() {
        return logo;
    }

    public String getDescription() {
        return description;
    }



    public void setCode(String code) {
        this.code = code;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public void setDescription(String description) {
        this.description = description;
    }

	public Boolean getActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

}
