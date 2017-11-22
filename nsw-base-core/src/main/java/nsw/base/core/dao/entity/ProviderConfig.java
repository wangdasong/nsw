/********************************************************************
 *
 * Copyright 2016-2017 nsw All rights reserved
 *
 ********************************************************************/
package nsw.base.core.dao.entity;
import nsw.base.core.dao.entity.base.BaseEntity;
import nsw.base.core.dao.entity.base.FrameworkDbDialogConfig;

import java.util.Date;

public class ProviderConfig extends BaseEntity implements FrameworkDbDialogConfig {

	private static final long serialVersionUID = -8297282695329983116L;
	private String code;
    private Integer weight;
    private String ip;
    private String port;
    private String remark;


    public String getCode() {
        return code;
    }

    public Integer getWeight() {
        return weight;
    }

    public String getIp() {
        return ip;
    }

    public String getPort() {
        return port;
    }

    public String getRemark() {
        return remark;
    }



    public void setCode(String code) {
        this.code = code;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }



}
