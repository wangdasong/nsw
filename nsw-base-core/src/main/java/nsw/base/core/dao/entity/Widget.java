/********************************************************************
 *
 * Copyright 2016-2017 CNPL(CPSCS).Co.Ltd All rights reserved
 *
 ********************************************************************/
package nsw.base.core.dao.entity;
import java.util.List;

import nsw.base.core.dao.entity.base.BaseEntity;
import nsw.base.core.dao.entity.base.FrameworkDbDialogConfig;

public class Widget extends BaseEntity implements FrameworkDbDialogConfig {

    private String code;
    private String name;
    private String type;
    private int sort = 9999;
    private String containerId;
    private String tmpFlg;
    private List<AttConfig> attConfigs;
    private List<Element> elements;


    public String getId() {
        return id;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String getContainerId() {
        return containerId;
    }

    public String getTmpFlg() {
        return tmpFlg;
    }




    public void setId(String id) {
        this.id = id;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setContainerId(String containerId) {
        this.containerId = containerId;
    }

    public void setTmpFlg(String tmpFlg) {
        this.tmpFlg = tmpFlg;
    }

	public List<AttConfig> getAttConfigs() {
		return attConfigs;
	}

	public void setAttConfigs(List<AttConfig> attConfigs) {
		this.attConfigs = attConfigs;
	}

	public List<Element> getElements() {
		return elements;
	}

	public void setElements(List<Element> elements) {
		this.elements = elements;
	}

	public int getSort() {
		return sort;
	}

	public void setSort(int sort) {
		this.sort = sort;
	}



}
