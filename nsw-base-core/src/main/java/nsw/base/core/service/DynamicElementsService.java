package nsw.base.core.service;

import java.util.List;

import nsw.base.core.dao.entity.AttConfig;
import nsw.base.core.dao.entity.Element;

public interface DynamicElementsService {
	List<Element> getElementsByWidgetId(String widgetId);
	List<AttConfig> getAttConfigsByElementId(String elementId);
}
