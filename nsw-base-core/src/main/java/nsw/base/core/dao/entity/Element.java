/********************************************************************
 *
 * Copyright 2016-2017 CNPL(CPSCS).Co.Ltd All rights reserved
 *
 ********************************************************************/
package nsw.base.core.dao.entity;
import java.util.List;

import nsw.base.core.dao.entity.base.BaseEntity;

public class Element extends BaseEntity {

    private String code;
    private String name;
    private String parentId;
    private String widgetId;
    private int subCount;
    private List<AttConfig> attConfigs;
    private List<Element> childElements;
    private Element parentElement;

    public String getId() {
        return id;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public String getWidgetId() {
        return widgetId;
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

    public void setWidgetId(String widgetId) {
        this.widgetId = widgetId;
    }

	public List<AttConfig> getAttConfigs() {
		return attConfigs;
	}

	public void setAttConfigs(List<AttConfig> attConfigs) {
		this.attConfigs = attConfigs;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public List<Element> getChildElements() {
		return childElements;
	}

	public void setChildElements(List<Element> childElements) {
		this.childElements = childElements;
	}

	public int getSubCount() {
		return subCount;
	}

	public void setSubCount(int subCount) {
		this.subCount = subCount;
	}

	public Element getParentElement() {
		return parentElement;
	}

	public void setParentElement(Element parentElement) {
		this.parentElement = parentElement;
	}

}
